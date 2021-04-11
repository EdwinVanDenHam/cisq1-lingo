package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.GameOverException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;
import nl.hu.cisq1.lingo.trainer.domain.exception.OngoingRoundException;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "game")
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int score;
    private int roundsWon;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Round> rounds;

    public Game() {
        rounds = new ArrayList<>();
    }

    public void startRound(String wordToGuess){
        // if game is over -> throw exception
        if(status == GameStatus.GAME_OVER) { throw new GameOverException(); }
        // if round is over -> throw exception
        if(!rounds.isEmpty() && (getLastRound().getFeedback().isEmpty() || !getLastRound().getLastFeedback().isWordGuessed() )){ throw new OngoingRoundException(); }
            this.status = GameStatus.ONGOING;
            this.rounds.add(new Round());
            getLastRound().provideInitialHint(wordToGuess);
    }

    public boolean guessWord(String guess, boolean isValid){
        try {
            if(isValid) {
                getLastRound().makeGuess(guess);
            }
            if(!isValid){
                getLastRound().makeGuessInvalid(guess);
            }
            // if the word is guessed
            if(getLastRound().getLastFeedback().isWordGuessed()){
                roundsWon +=1;
                updateScore(getLastRound().getFeedback().size());
                status = GameStatus.WAITING_FOR_NEW_ROUND;
            }
            return true;
            // no more turns -> game over
        }catch (NoMoreTurnsException e){
            this.status = GameStatus.GAME_OVER;
            return false;
        }
    }

    public int getNextWordLength(String previousWordToGuess){
        if (previousWordToGuess.length() < 7 && previousWordToGuess.length() > 4){
            return previousWordToGuess.length() + 1;
        }
        return 5;
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

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Long getId() {
        return id;
    }

}
