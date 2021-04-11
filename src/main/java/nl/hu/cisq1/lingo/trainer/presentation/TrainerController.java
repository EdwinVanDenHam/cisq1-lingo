package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameNotFoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.RoundOverException;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.Word;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/game")
public class TrainerController {
    private final TrainerService service;

    public TrainerController(TrainerService service) {
        this.service = service;
    }

    @PostMapping("/start")
    public GameDTO startNewGame() {
        Game game = service.startNewGame();
        return new GameDTO(game);
    }

    @GetMapping("{id}")
    public GameDTO showGame(@PathVariable("id") Long id) {
        try{
            Game game = service.findGame(id);
            return new GameDTO(game);
        }catch(GameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "De game met dit ID is niet gevonden");
        }
    }

    @PatchMapping("{id}/guess")
    public GameDTO makeGuess(@PathVariable("id") Long id, @Valid @RequestBody Word word) {
        try{
            Game game = service.findGame(id);
            service.guessWord(word.word, id);
            return new GameDTO(game);
        }catch(GameNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "De game met dit ID is niet gevonden");
        } catch(GameOverException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Je hebt het spel verloren, start een nieuw spel");
        } catch(RoundOverException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Je hebt deze ronde gewonnen, start een nieuwe ronde");
        }
    }

    @PostMapping("{id}/newRound")
    public GameDTO startNewRound(@PathVariable("id") Long id) {
        try {
            Game game = service.findGame(id);
            service.startNewRound(id);
            return new GameDTO(game);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "De game met dit ID is niet gevonden");
        } catch (OngoingRoundException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Er kan geen nieuwe ronde gestart worden, maak de vorige ronde eerst af");
        } catch(GameOverException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Je hebt het spel verloren, start een nieuw spel");
        }
    }
}