package nl.hu.cisq1.lingo.trainer.domain.exception;

public class InvalidFeedbackException extends RuntimeException {
    public InvalidFeedbackException(Integer wordLength, Integer feedbackLength) {
        super("The length of the feedback (" + feedbackLength + ") is not the same as the word length (" + wordLength + ")" );
    }
}
