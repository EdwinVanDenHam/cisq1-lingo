package nl.hu.cisq1.lingo.trainer.domain.exception;

public class GameOverException extends RuntimeException {
    public GameOverException() {
        super("The game is over, you can not start a new round.");
    }
}
