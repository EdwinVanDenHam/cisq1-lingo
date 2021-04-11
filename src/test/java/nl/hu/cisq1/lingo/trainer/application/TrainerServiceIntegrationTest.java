package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {

    @Autowired
    private TrainerService service;

    @ParameterizedTest
    @DisplayName("Checks if words are valid")
    @MethodSource("provideCheckExamples")
    void checkWordIsValid(String word, boolean isValid) throws GameNotFoundException {
        Game game = service.startNewGame();
        assertEquals(isValid, service.checkWordIsValid(word, game.getId()));
    }

    static Stream<Arguments> provideCheckExamples() {
        return Stream.of(
                Arguments.of("blijf", true),
                Arguments.of("bliqf", false),
                Arguments.of("feesten", false),
                Arguments.of("feeeeee", false)
        );
    }

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
        assertEquals(game.getStatus(), GameStatus.ONGOING);
    }

    @Test
    @DisplayName("Guessed the word")
    void guessWordRight() throws GameNotFoundException {
        Long id = service.startNewGame().getId();
        String wordToGuess = service.findGame(id).getLastRound().getWordToGuess();
        service.guessWord(wordToGuess, id);
        assertEquals(service.findGame(id).getRoundsWon(), 1);
        assertEquals(GameStatus.WAITING_FOR_NEW_ROUND, service.findGame(id).getStatus());
    }

    @Test
    @DisplayName("Did not guess the word")
    void guessWordWrong() throws GameNotFoundException {
        Long id = service.startNewGame().getId();
        service.guessWord("aap", id);
        assertEquals(service.findGame(id).getRoundsWon(), 0);
        assertEquals( GameStatus.ONGOING, service.findGame(id).getStatus());
        assertEquals(service.findGame(id).getLastRound().getFeedback().size(), 1);
    }

    @Test
    @DisplayName("Starts a new round")
    void startNewRound() throws GameNotFoundException {
        Game game = service.startNewGame();
        assertEquals(game.getRounds().size(),1);
        service.guessWord(game.getLastRound().getWordToGuess(), game.getId());
        service.startNewRound(game.getId());
        assertEquals(1, service.findGame(game.getId()).getRoundsWon());
        assertEquals(2, service.findGame(game.getId()).getRounds().size());
    }
}

