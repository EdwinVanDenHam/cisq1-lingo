package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        // arrange

        // act
        String guess = "word";
        String wordToGuess = "word";
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.giveFeedback();

        // assert
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        // arrange

        // act
        String guess = "word";
        String wordToGuess = "oord";
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.giveFeedback();

        // assert
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not invalid if the word exists, is spelled right and has the correct length")
    void guessIsNotInvalid(){
        // arrange

        // act
        String guess = "bank";
        String wordToGuess = "kamp";
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.giveFeedback();

        // assert
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("word is invalid if the word does not exist, is not spelled correctly or does not have the correct length")
    void guessIsInvalid(){
        // arrange

        // act
        String guess = "aaaoo";
        String wordToGuess = "lamp";
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.giveFeedback();

        // assert
        assertFalse(feedback.isGuessValid());
    }

    @Test
    @DisplayName("feedback length is invalid if it's not the same as the word length")
    void feedbackLengthInvalid(){
        String guess = "staal";
        String wordToGuess = "stoel";
        Feedback feedback = new Feedback(guess, wordToGuess);

        assertThrows(
                InvalidFeedbackException.class,
                () -> feedback.setFeedback(List.of(Mark.CORRECT))
        );
    }

    @Test
    @DisplayName("feedback length is valid if it's the same as the word length")
    void feedbackLengthValid(){
        String guess = "staal";
        String wordToGuess = "stoel";
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.setFeedback(List.of(Mark.CORRECT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT,Mark.CORRECT));
        assertEquals(List.of(Mark.CORRECT,Mark.CORRECT,Mark.ABSENT,Mark.ABSENT,Mark.CORRECT),feedback.getFeedback());
    }

    @Test
    @DisplayName("Provide feedback based on the occurrence of the letters")
    void provideFeedback(){
        // arrange

        // act
        String guess = "friet";
        String wordToGuess = "fiets";
        Feedback feedback = new Feedback(guess, wordToGuess);

        // assert
        assertEquals(List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT), feedback.giveFeedback());
    }

    @Test
    @DisplayName("Feedback is invalid if the length is different then the to be guessed word")
    void FeedbackInvalid(){
        // arrange

        // act
        String guess = "toveren";
        String wordToGuess = "fiets";
        Feedback feedback = new Feedback(guess, wordToGuess);

        // assert
        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID), feedback.giveFeedback());
    }
}