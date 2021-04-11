package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("The game should throw an exception if the player is out of turns")
    void guessWordNoTurnsLeft(){
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        assertThrows(
                NoMoreTurnsException.class,
                () -> game.getLastRound().makeGuess("staal")
        );
    }

    @Test
    @DisplayName("The round should keep track of the hints that have been given")
    void trackHints(){
        Round round = new Round();
        round.provideInitialHint("laden");
        round.makeGuess("raden");

        assertEquals(2, round.getHints().size());
    }

    @Test
    @DisplayName("The round should keep track of the feedback that has been given")
    void trackFeedback(){
        Round round = new Round();
        round.provideInitialHint("laden");
        round.makeGuess("roven");
        round.makeGuess("doden");
        round.makeGuess("raden");

        assertEquals(3, round.getFeedback().size());
    }

    @Test
    @DisplayName("The word should be guessed if it is the same on the last attempt")
    void guessWordLastAttempt() {
        Game game = new Game();
        game.startRound("laden");
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);
        game.guessWord("staal", true);

        game.guessWord("laden", true);

        assertTrue(game.getLastRound().getLastFeedback().isWordGuessed());
    }
}
