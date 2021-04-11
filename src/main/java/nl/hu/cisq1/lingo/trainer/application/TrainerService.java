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
        Game game = findGame(id);
        if(word.length() != game.getLastRound().getWordToGuess().length()){
            return false;
        }
        return this.wordService.checkWordExists(word);
    }

    public Game findGame(Long id) throws GameNotFoundException {
        this.game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        return game;
    }

    public Game startNewGame(){
        Game game = new Game();
        String wordToGuess = this.wordService.provideRandomWord(5);

        game.setStatus(GameStatus.ONGOING);
        game.getRounds().add(new Round());
        game.getLastRound().provideInitialHint(wordToGuess);

        this.gameRepository.save(game);
        this.game = game;
        return game;
    }

    public boolean guessWord(String guess, Long id) throws GameNotFoundException{
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        // if the game is lost
        if(game.getStatus() == GameStatus.GAME_OVER){
            throw new GameOverException();
        }
        // if the round is over
        if(game.getStatus() == GameStatus.WAITING_FOR_NEW_ROUND){
            throw new RoundOverException();
        }
        // if the word does not exist (or does not have the correct length)
         if (!checkWordIsValid(guess, id)) {
             return game.guessWord(guess, false);
         }
        // if the word exists and has the right length
        return game.guessWord(guess, true);
    }

    public void startNewRound(Long id) throws GameNotFoundException {
        Game game = gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
        // if the game is lost -> throw exception
        if(game.getStatus() == GameStatus.GAME_OVER) { throw new GameOverException(); }
        // if there is an ongoing round -> throw exception
        if(game.getLastRound().getFeedback().isEmpty() || !game.getLastRound().getLastFeedback().isWordGuessed()) { throw new OngoingRoundException(); }
        game.setStatus(GameStatus.ONGOING);
        game.getRounds().add(new Round());
        game.getLastRound().provideInitialHint(this.wordService.provideRandomWord(
                game.getNextWordLength(game.getRounds().get(game.getRounds().size() - 2).getWordToGuess())));
        this.gameRepository.save(game);
    }
}
