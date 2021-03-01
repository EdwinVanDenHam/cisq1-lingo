package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @BeforeEach
    public void initialize() {
    }


    @Test
    @DisplayName("the game should provide the first hint when a round starts")
    void startRound() {
        Game game = new Game();
        game.startRound();

        assertEquals(List.of('l', '.', '.', '.', '.'), game.getLastRound().getHints().get(0).getHint());
    }

    @Test
    @DisplayName("If the word is guessed one round is won")
    void roundWonTrue() {
        Game game = new Game();
        game.startRound();
        game.guessWord("laden");
        assertEquals(1, game.getRoundsWon());
    }

    @Test
    @DisplayName("If the word is not guessed the round is not won")
    void roundWonFalse() {
        Game game = new Game();
        game.startRound();
        game.guessWord("raden");
        assertEquals(0, game.getRoundsWon());
    }

    @Test
    @DisplayName("The game is over if the word is not guessed in 5 turns")
    void gameOver() {
        Game game = new Game();
        game.startRound();
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");

        assertEquals(GameStatus.GAME_OVER, game.getStatus());
    }

    @Test
    @DisplayName("The game should be ongoing if the limit of 5 turns is not reached")
    void gameNotOver() {
        Game game = new Game();
        game.startRound();
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");

        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    @DisplayName("The state should change to waiting for new round if the round is won")
    void roundWonStatus() {
        Game game = new Game();
        game.startRound();
        game.guessWord("laden");
        assertEquals(GameStatus.WAITING_FOR_NEW_ROUND, game.getStatus());
    }


    @Test
    @DisplayName("The score should update every round till the end of the game")
    void updateScore() {
        Game game = new Game();
        game.startRound();
        game.guessWord("staal");
        game.guessWord("laden");
        game.startRound();
        game.guessWord("laden");
        game.startRound();
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");

        assertEquals(45, game.getScore());
    }

    @Test
    @DisplayName("When the game is over, a new round can not be started")
    void gameOverException() {
        Game game = new Game();
        game.startRound();
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");
        game.guessWord("staal");

        assertThrows(
                GameOverException.class,
                game::startRound
        );
    }

    @Test
    @DisplayName("When a round has started, a new round can not be started if the previous one is not completed")
    void ongoingRoundException() {
        Game game = new Game();
        game.startRound();

        assertThrows(
                OngoingRoundException.class,
                game::startRound
        );
    }

    @Test
    @DisplayName("When a round has started, a new round can not be started if the previous one is not completed")
    void ongoingRoundException2() {
        Game game = new Game();
        game.startRound();
        game.guessWord("staal");

        assertThrows(
                OngoingRoundException.class,
                game::startRound
        );
    }
}