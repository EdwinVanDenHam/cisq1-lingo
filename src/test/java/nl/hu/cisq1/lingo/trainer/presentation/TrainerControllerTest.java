package nl.hu.cisq1.lingo.trainer.presentation;


import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.Word;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrainerControllerTest {
    private WordService wordService;
    private SpringGameRepository gameRepository;
    private TrainerService trainerService;
    private TrainerController controller;

    @BeforeEach
    void init(){
        wordService = mock(WordService.class);
        when(wordService.provideRandomWord(6))
                .thenReturn("speelt");
        when(wordService.provideRandomWord(5))
                .thenReturn("feest");
        when(wordService.checkWordExists("feest"))
                .thenReturn(true);
        when(wordService.checkWordExists("leest"))
                .thenReturn(true);

        gameRepository = mock(SpringGameRepository.class);
        trainerService = new TrainerService(wordService, gameRepository);
        controller = new TrainerController(trainerService);
    }

    @Test
    @DisplayName("Starts a new game")
    void startNewGame() {
        GameStatus result = controller.startNewGame().getStatus();

        assertEquals(GameStatus.ONGOING, result);
    }

    @Test
    @DisplayName("Shows a game by ID")
    void showGame() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        GameDTO gameDTO = controller.showGame(game.getId());

        assertEquals(game.getId(), gameDTO.getGameId());
    }

    @Test
    @DisplayName("Makes player do a guess")
    void makeGuess() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        assertEquals(0, game.getLastRound().getFeedback().size());

        controller.makeGuess(game.getId(), new Word("leest"));

        assertEquals(1, game.getLastRound().getFeedback().size());
    }

    @Test
    @DisplayName("Starts a new round")
    void startNewRound() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        assertEquals(1, game.getRounds().size());

        controller.makeGuess(game.getId(), new Word("feest"));
        controller.startNewRound(game.getId());

        assertEquals(2, game.getRounds().size());
    }

    @Test
    @DisplayName("Try to show a game that doesnt exist")
    void showGameDoesntExist() {
        try{
            controller.showGame(0L);
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(404, exception.getRawStatusCode());
        }
    }

    @Test
    @DisplayName("Guess word but game doesnt exist")
    void makeGuessGameNotFound() {
        try{
            controller.makeGuess(0L, new Word("feest"));
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(404, exception.getRawStatusCode());
        }
    }

    @Test
    @DisplayName("Start new round but game doesnt exist")
    void startNewRoundGameNotFound() {
       try{
            controller.startNewRound(0L);
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(404, exception.getRawStatusCode());
        }
    }

    @Test
    @DisplayName("Guess word but game is over")
    void makeGuessGameOver() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));

        try{
            controller.makeGuess(game.getId(), new Word("leest"));
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(409, exception.getRawStatusCode());
        }
    }


    @Test
    @DisplayName("Start new round but game is over")
    void startNewRoundGameOver() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));
        controller.makeGuess(game.getId(), new Word("leest"));

        try{
            controller.startNewRound(game.getId());
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(409, exception.getRawStatusCode());
        }
    }

    @Test
    @DisplayName("Guess word but round is already won")
    void makeGuessRoundWonException() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        controller.makeGuess(game.getId(), new Word("feest"));

        try{
            controller.makeGuess(game.getId(), new Word("leest"));
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(409, exception.getRawStatusCode());
        }
    }

    @Test
    @DisplayName("Start new round the previous round is not won or over")
    void startNewRoundException() {
        Game game = trainerService.startNewGame();
        when(gameRepository.findById(game.getId()))
                .thenReturn(java.util.Optional.of(game));

        controller.makeGuess(game.getId(), new Word("leest"));

        try{
            controller.startNewRound(game.getId());
            fail("Expected exception was not thrown");
        } catch(ResponseStatusException exception) {
            assertEquals(409, exception.getRawStatusCode());
        }
    }

}
