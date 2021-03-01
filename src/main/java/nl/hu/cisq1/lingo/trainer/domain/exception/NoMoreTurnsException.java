package nl.hu.cisq1.lingo.trainer.domain.exception;

public class NoMoreTurnsException extends RuntimeException {
    public NoMoreTurnsException() {
        super("The word was not guessed in 5 turns, no more turns are left");
    }
}
