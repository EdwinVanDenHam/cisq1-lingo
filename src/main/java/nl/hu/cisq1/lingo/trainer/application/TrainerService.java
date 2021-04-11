package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.exception.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TrainerService {
    private WordService wordService;
    private SpringGameRepository gameRepository;
    private Game game;

    public TrainerService(WordService wordService, SpringGameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public boolean checkWordIsValid(String word, Long id) throws GameNotFoundException {
        Game newGame = findGame(id);
        if(word.length() != newGame.getLastRound().getWordToGuess().length()){
            return false;
        }
        return this.wordService.checkWordExists(word);
    }

    public Game findGame(Long id) throws GameNotFoundException {
        this.game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        return game;
    }

    public Game startNewGame(){
        Game newGame = new Game();
        String wordToGuess = this.wordService.provideRandomWord(5);

        newGame.setStatus(GameStatus.ONGOING);
        newGame.getRounds().add(new Round());
        newGame.getLastRound().provideInitialHint(wordToGuess);

        this.gameRepository.save(newGame);
        this.game = newGame;
        return game;
    }

    public boolean guessWord(String guess, Long id) throws GameNotFoundException{
        Game newGame = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        // if the game is lost
        if(newGame.getStatus() == GameStatus.GAME_OVER){
            throw new GameOverException();
        }
        // if the round is over
        if(newGame.getStatus() == GameStatus.WAITING_FOR_NEW_ROUND){
            throw new RoundOverException();
        }
        // if the word does not exist (or does not have the correct length)
         if (!checkWordIsValid(guess, id)) {
             return newGame.guessWord(guess, false);
         }
        // if the word exists and has the right length
        return newGame.guessWord(guess, true);
    }

    public void startNewRound(Long id) throws GameNotFoundException {
        Game newGame = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        // if the game is lost -> throw exception
        if(newGame.getStatus() == GameStatus.GAME_OVER) { throw new GameOverException(); }
        // if there is an ongoing round -> throw exception
        if(newGame.getLastRound().getFeedback().isEmpty() || !newGame.getLastRound().getLastFeedback().isWordGuessed()) { throw new OngoingRoundException(); }
        newGame.setStatus(GameStatus.ONGOING);
        newGame.getRounds().add(new Round());
        newGame.getLastRound().provideInitialHint(this.wordService.provideRandomWord(
                newGame.getNextWordLength(newGame.getRounds().get(newGame.getRounds().size() - 2).getWordToGuess())));
        this.gameRepository.save(newGame);
    }
}
