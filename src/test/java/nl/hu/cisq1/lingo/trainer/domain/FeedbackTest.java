package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("word is guessed if all letters are correct")
    void wordIsGuessed(){
        // arrange

        // act
        String guess = "word";
        List marks = List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT);
        Feedback feedback = new Feedback(guess, marks);

        // assert
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not guessed if not all letters are correct")
    void wordIsNotGuessed(){
        // arrange

        // act
        String guess = "word";
        List marks = List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT);
        Feedback feedback = new Feedback(guess, marks);

        // assert
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("word is not invalid if the word exists, is spelled right and has the correct length")
    void guessIsNotInvalid(){
        // arrange

        // act
        String guess = "bank";
        List marks = List.of(Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.PRESENT);
        Feedback feedback = new Feedback(guess, marks);

        // assert
        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("word is invalid if the word does not exist, is not spelled correctly or does not have the correct length")
    void guessIsInvalid(){
        // arrange

        // act
        String guess = "aaaoo";
        List marks = List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID);
        Feedback feedback = new Feedback(guess, marks);

        // assert
        assertFalse(feedback.isGuessValid());
    }

    @Test
    @DisplayName("feedback length is invalid if it's not the same as the word length")
    void feedbackLengthInvalid(){
        assertThrows(
                InvalidFeedbackException.class,
                () -> new Feedback("woord", List.of(Mark.CORRECT))
        );
    }

    @ParameterizedTest
    @DisplayName("the game should provide and update a hint based on what letters are correct")
    @MethodSource("provideHintExamples")
    void provideHint(List<Character> hint, String word, List<Character> newHint){
        Feedback feedback = new Feedback(word, List.of(Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT));
        Hint provideHint = new Hint(List.of(Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT));
        assertEquals(newHint, provideHint.giveHint(hint, word));
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(List.of('.', '.', '.', '.', '.' ), "woord", List.of('.', '.', 'o', '.', 'd')),
                Arguments.of(List.of('.', '.', 'o', '.', 'd'), "woord", List.of('.', '.', 'o', '.', 'd')),
                Arguments.of(List.of('b', '.', '.', '.', 'f'), "blijf", List.of('b', '.', 'i', '.', 'f')),
                Arguments.of(List.of('b', '.', 'i', '.', 'f'), "blijf", List.of('b', '.', 'i', '.', 'f'))
        );
    }

}