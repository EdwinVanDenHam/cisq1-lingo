package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundOverException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private TrainerService trainerService;
    private WordService wordService;
    private SpringGameRepository repository;

    @BeforeEach
    void init(){
        wordService = mock(WordService.class);
        when(wordService.provideRandomWord(5))
                .thenReturn("feest");
        when(wordService.checkWordExists("feest"))
                .thenReturn(true);
        when(wordService.checkWordExists("zeven"))
                .thenReturn(true);
        when(wordService.checkWordExists("zevbn"))
                .thenReturn(false);

        repository = mock(SpringGameRepository.class);
        trainerService = new TrainerService(wordService, repository);
    }

    @Test
    @DisplayName("Validate the word the player guessed")
    void validationValidWord() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        boolean result = trainerService.checkWordIsValid("zeven", game.getId());

        assertTrue(result);
    }

    @Test
    @DisplayName("Validation is invalid")
    void validationInvalidWord() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        boolean result = trainerService.checkWordIsValid("zevbn", game.getId());

        assertFalse(result);
    }

    @Test
    @DisplayName("Validation is invalid due to word length")
    void validationInvalidWordLength() throws GameNotFoundException {
        when(wordService.checkWordExists("feesten"))
                .thenReturn(true);
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        boolean result = trainerService.checkWordIsValid("feesten", game.getId());

        assertFalse(result);
    }

    @Test
    @DisplayName("Find a game")
    void findGame() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        assertEquals(game.getId(), trainerService.findGame(game.getId()).getId());
    }

    @Test
    @DisplayName("Find a game, not found")
    void findGameNotFound() {
        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.findGame(0L)
        );
    }

    @ParameterizedTest
    @DisplayName("Attempts to guess the word")
    @MethodSource("provideGuessExamples")
    void guessWord(Game game, String word, boolean isValid) throws GameNotFoundException {
        when(wordService.checkWordExists("blijf"))
                .thenReturn(true);
        when(wordService.checkWordExists("feese"))
                .thenReturn(false);

        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));
        TrainerService trainerService = new TrainerService(wordService, repository);

        assertEquals(isValid, trainerService.guessWord(word, game.getId()));
    }

    static Stream<Arguments> provideGuessExamples() {
        WordService wordService = mock(WordService.class);
        when(wordService.provideRandomWord(5))
                .thenReturn("feest");
        when(wordService.checkWordExists("feest"))
                .thenReturn(true);

        SpringGameRepository repository = mock(SpringGameRepository.class);
        TrainerService trainerService = new TrainerService(wordService, repository);
        Game game = trainerService.startNewGame();
        game.guessWord("ronde", true);
        game.guessWord("ronde", true);
        game.guessWord("ronde", true);

        Game game2 = trainerService.startNewGame();
        game2.guessWord("ronde", true);
        game2.guessWord("ronde", true);
        game2.guessWord("ronde", true);

        return Stream.of(
                Arguments.of(game, "zeven", true),
                Arguments.of(game, "zeven", false),
                Arguments.of(game2, "zevbn", true),
                Arguments.of(game2, "zevbn", false)
        );
    }

    @Test
    @DisplayName("Try to guess the word but the game is not found")
    void guessGameNotFound() {
        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.guessWord("ronde", 0L)
        );
    }

    @Test
    @DisplayName("Try to guess the word when the game is over")
    void guessGameOver() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());

        assertThrows(
                GameOverException.class,
                () -> trainerService.guessWord("zeven", game.getId())
        );
    }

    @Test
    @DisplayName("Try to guess the word when the round is already over")
    void guessRoundOver() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        trainerService.guessWord("feest", game.getId());

        assertThrows(
                RoundOverException.class,
                () -> trainerService.guessWord("feest", game.getId())
        );
    }

    @Test
    @DisplayName("Starts a new game")
    void startNewGame() {
        Game game = trainerService.startNewGame();

        assertEquals(List.of('f', '.', '.', '.', '.'), game.getLastRound().getLastHint().getHint());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }

    @Test
    @DisplayName("Starts a new round")
    void startNewRound() throws GameNotFoundException {
        WordService wordService = mock(WordService.class);
        when(wordService.provideRandomWord(6))
                .thenReturn("wegens");

        Game game = new Game();
        game.startRound("zeven");
        game.guessWord("zeven", true);

        SpringGameRepository repository = mock(SpringGameRepository.class);
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(game));

        TrainerService trainerService = new TrainerService(wordService, repository);
        trainerService.startNewRound(0L);

        assertEquals(List.of('w', '.', '.', '.', '.', '.'), game.getLastRound().getLastHint().getHint());
        assertEquals(GameStatus.ONGOING, game.getStatus());
    }


    @Test
    @DisplayName("Try to start a round when game is over")
    void startRoundGameOver() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());
        trainerService.guessWord("zeven", game.getId());

        assertThrows(
                GameOverException.class,
                () -> trainerService.startNewRound(game.getId())
        );
    }

    @Test
    @DisplayName("Start a new round while the previous round is not over")
    void startRoundOngoing() throws GameNotFoundException {
        Game game = trainerService.startNewGame();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        trainerService.guessWord("zeven", game.getId());

        assertThrows(
                OngoingRoundException.class,
                () -> trainerService.startNewRound(game.getId())
        );
    }

    @Test
    @DisplayName("Start a new round but could not find the game")
    void startRoundGameNotFound() {
        assertThrows(
                GameNotFoundException.class,
                () -> trainerService.startNewRound(0L)
        );
    }

}