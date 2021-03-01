package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {

    @Test
    void giveInitialHint() {
        Hint hint = new Hint(List.of());
        assertEquals(List.of('r','.','.','.','.'), hint.giveInitialHint("raden"));
    }

    @ParameterizedTest
    @DisplayName("the game should provide and update a hint based on what letters are correct")
    @MethodSource("provideHintExamples")
    void provideHint(List<Character> previousHint, String word, List<Character> newHint){
        Hint hint = new Hint(List.of(Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT));
        assertEquals(newHint, hint.giveHint(previousHint, word));
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of(List.of('.', '.', '.', '.', '.' ), "woord", List.of('.', '.', 'o', '.', 'd')),
                Arguments.of(List.of('.', '.', 'o', '.', 'd'), "woord", List.of('.', '.', 'o', '.', 'd')),
                Arguments.of(List.of('b', '.', '.', '.', 'f'), "blijf", List.of('b', '.', 'i', '.', 'f')),
                Arguments.of(List.of('b', '.', 'i', '.', 'f'), "blijf", List.of('b', '.', 'i', '.', 'f'))
        );
    }

    @Test
    void provideHintInvalid() {
        Hint hint = new Hint(List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID));
        assertEquals(List.of('r','.','.','.','.'), hint.giveHint(List.of('r','.','.','.','.'),"raden"));
    }

}