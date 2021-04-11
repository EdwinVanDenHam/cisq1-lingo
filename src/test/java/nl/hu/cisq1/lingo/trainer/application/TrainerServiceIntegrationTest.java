package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {

    @Autowired
    private TrainerService service;

    @Test
    @DisplayName("Finds a game based on id")
    void findGame() throws GameNotFoundException {
        Game game = service.startNewGame();
        assertEquals(service.findGame(game.getId()).getId(), game.getId());
    }

    @Test
    @DisplayName("Throws exception if game is not found")
    void findGameNotFound() {
        assertThrows(
                GameNotFoundException.class,
                () -> service.findGame(0L)
        );
    }

    @Test
    @DisplayName("Starts a new game")
    void startNewGame() throws GameNotFoundException {
        Game game = service.startNewGame();
        assertEquals(service.findGame(game.getId()).getId(), game.getId());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    @DisplayName("Guessed the word")
    void guessWordRight() throws GameNotFoundException {
        Long id = service.startNewGame().getId();
        String wordToGuess = service.findGame(id).getLastRound().getWordToGuess();
        service.guessWord(wordToGuess, id);
        assertEquals(1, service.findGame(id).getRoundsWon());
        assertEquals(GameStatus.WAITING_FOR_NEW_ROUND, service.findGame(id).getStatus());
    }

    @Test
    @DisplayName("Did not guess the word")
    void guessWordWrong() throws GameNotFoundException {
        Long id = service.startNewGame().getId();
        service.guessWord("aap", id);
        assertEquals(0, service.findGame(id).getRoundsWon());
        assertEquals( GameStatus.ONGOING, service.findGame(id).getStatus());
        assertEquals(1, service.findGame(id).getLastRound().getFeedback().size());
    }

    @Test
    @DisplayName("Starts a new round")
    void startNewRound() throws GameNotFoundException {
        Game game = service.startNewGame();
        assertEquals(1, game.getRounds().size());
        service.guessWord(game.getLastRound().getWordToGuess(), game.getId());
        service.startNewRound(game.getId());
        assertEquals(1, service.findGame(game.getId()).getRoundsWon());
        assertEquals(2, service.findGame(game.getId()).getRounds().size());
    }
}

