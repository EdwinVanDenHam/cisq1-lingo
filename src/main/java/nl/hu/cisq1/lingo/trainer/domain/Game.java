package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private int score;
    private int roundsWon;
    private GameStatus status;
    private List<Round> rounds;

    public Game() {
        rounds = new ArrayList<>();
    }

    public void startRound(){
        if(status == GameStatus.GAME_OVER) { throw new GameOverException(); }
        if(rounds.size() != 0 && (getLastRound().getFeedback().size() == 0 || !getLastRound().getLastFeedback().isWordGuessed() )){ throw new OngoingRoundException(); }
            this.status = GameStatus.ONGOING;
            this.rounds.add(new Round("laden"));
            getLastRound().provideInitialHint();
    }

    public void guessWord(String guess){
        try {
            getLastRound().makeGuess(guess);
            // als het woord is geraden
            if(getLastRound().getLastFeedback().isWordGuessed()){
                roundsWon +=1;
                updateScore(getLastRound().getFeedback().size());
                status = GameStatus.WAITING_FOR_NEW_ROUND;
            }
            // geen beurten meer -> game over
        }catch (NoMoreTurnsException e){
            this.status = GameStatus.GAME_OVER;
        };
    }

    public void updateScore(int attempts){
        score += 5 * (5 - attempts) + 5;
    }

    public Round getLastRound(){
        return rounds.get(rounds.size() -1);
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public int getScore() {
        return score;
    }

    public GameStatus getStatus() {
        return status;
    }
}
