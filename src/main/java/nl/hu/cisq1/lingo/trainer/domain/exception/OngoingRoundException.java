package nl.hu.cisq1.lingo.trainer.domain.exception;

public class OngoingRoundException extends RuntimeException {
    public OngoingRoundException() {
        super("A new round can not be started, finish the previous round first.");
    }
}
