package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTest {

    @Test
    @DisplayName("the game should provide the first hint when a round starts")
    void startRound() {
        Game game = new Game();
        game.startRound("laden");

        assertEquals(List.of('l', '.', '.', '.', '.'), game.getLastRound().getHints().get(0).getHint());
    }

    @Test
    @DisplayName("If the word is guessed one round is won")
    void roundWonTrue() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("laden", true);
        assertEquals(1, game.getRoundsWon());
    }

    @Test
    @DisplayName("If the word is not guessed the round is not won")
    void roundWonFalse() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("raden", true);
        assertEquals(0, game.getRoundsWon());
    }

    @Test
    @DisplayName("The game is over if the word is not guessed in 5 turns")
    void gameOver() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        assertEquals(GameStatus.GAME_OVER, game.getStatus());
    }

    @Test
    @DisplayName("The game should be ongoing if the limit of 5 turns is not reached")
    void gameNotOver() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    @DisplayName("The state should change to waiting for new round if the round is won")
    void roundWonStatus() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("laden", true);
        assertEquals(GameStatus.WAITING_FOR_NEW_ROUND, game.getStatus());
    }


    @Test
    @DisplayName("The score should update every round till the end of the game")
    void updateScore() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("laden", true);
        game.startRound("laden");
        game.guessWord("laden", true);
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        assertEquals(45, game.getScore());
    }

    @Test
    @DisplayName("When the game is over, a new round can not be started")
    void gameOverException() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        assertThrows(
                GameOverException.class, () -> game.startRound("laden")
        );
    }

    @Test
    @DisplayName("When a round has started, a new round can not be started if the previous one is not completed")
    void ongoingRoundException() {
        Game game = new Game();
        game.startRound("laden");

        assertThrows(
                OngoingRoundException.class, () -> game.startRound("laden")
        );
    }

    @Test
    @DisplayName("When a round has started, a new round can not be started if the previous one is not completed")
    void ongoingRoundException2() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);

        assertThrows(
                OngoingRoundException.class, () -> game.startRound("laden")
        );
    }

    @ParameterizedTest
    @DisplayName("Calculate the next word length")
    @MethodSource("provideWordExamples")
    void provideHint(String previousWord, int result){
        Game game = new Game();
        assertEquals(result, game.getNextWordLength(previousWord));
    }

    static Stream<Arguments> provideWordExamples() {
        return Stream.of(
                Arguments.of("water", 6),
                Arguments.of("straat", 7),
                Arguments.of("betalen", 5)
        );
    }

}