package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        // arrange
        String guess = "word";
        String wordToGuess = "word";
        Feedback feedback = new Feedback();

        // act
        feedback.giveFeedback(guess, wordToGuess);

        // assert
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        // arrange
        String guess = "word";
        String wordToGuess = "oord";
        Feedback feedback = new Feedback();

        // act
        feedback.giveFeedback(guess, wordToGuess);

        // assert
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not invalid if the word exists, is spelled right and has the correct length")
    void guessIsNotInvalid(){
        // arrange
        String guess = "bank";
        String wordToGuess = "kamp";
        Feedback feedback = new Feedback();

        // act
        feedback.giveFeedback(guess, wordToGuess);

        // assert
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("word is invalid if the word does not exist, is not spelled correctly or does not have the correct length")
    void guessIsInvalid(){
        // arrange
        String guess = "aaaoo";
        String wordToGuess = "lamp";
        Feedback feedback = new Feedback();

        // act
        feedback.giveFeedbackInvalid(guess, wordToGuess);

        // assert
        assertFalse(feedback.isGuessValid());
    }



    @Test
    @DisplayName("Provide feedback based on the occurrence of the letters")
    void provideFeedback(){
        // arrange
        String guess = "friet";
        String wordToGuess = "fiets";
        Feedback feedback = new Feedback();

        // act/ assert
        assertEquals(List.of(Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT), feedback.giveFeedback(guess, wordToGuess));
    }

    @Test
    @DisplayName("Feedback is invalid if the length is different then the to be guessed word")
    void FeedbackInvalid(){
        // arrange
        String guess = "toveren";
        String wordToGuess = "fiets";
        Feedback feedback = new Feedback();

        // act/ assert
        assertEquals(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID), feedback.giveFeedbackInvalid(guess, wordToGuess));
    }
}