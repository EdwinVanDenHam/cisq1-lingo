package nl.hu.cisq1.lingo.trainer.domain.exception;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(Long id) {
        super("Could not find game with id: " + id);
    }
}