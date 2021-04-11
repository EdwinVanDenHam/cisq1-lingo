package nl.hu.cisq1.lingo.trainer.presentation.dto;

import nl.hu.cisq1.lingo.trainer.domain.*;

import java.util.ArrayList;
import java.util.List;

public class GameDTO {
    private Long gameId;
    private GameStatus status;
    private int score;
    private int roundsWon;
    private Round round;

    public GameDTO(Game game) {
        this.gameId = game.getId();
        this.status = game.getStatus();
        this.round = game.getLastRound();
        this.score = game.getScore();
        this.roundsWon = game.getRoundsWon();
    }

    public Long getGameId() {
        return gameId;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public List<List<Character>> getHints(){
        List<List<Character>> list = new ArrayList<>();
        for(Hint h : round.getHints()){
            list.add(h.getHint());
        }
        return list;
    }

    public List<List<Mark>> getFeedback(){
        List<List<Mark>> list = new ArrayList<>();
        for(Feedback f : round.getFeedback()){
            list.add(f.getFeedback());
        }
        return list;
    }
}
