package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Round implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String wordToGuess;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name = "round_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Hint> hints;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "round_id")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Feedback> feedback;

    public Round() {
        this.hints = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }

    public void provideInitialHint(String wordToGuess){
        this.wordToGuess = wordToGuess;
        Hint hint = new Hint();
        hint.giveInitialHint(wordToGuess, List.of());
        hints.add(hint);
    }

    public void provideHint(List<Mark> marks, List<Character> previousHint){
        Hint hint = new Hint();
        hint.giveHint(previousHint, wordToGuess, marks);
        hints.add(hint);
    }

    public void provideFeedback(String guess){
        Feedback fb = new Feedback();
        fb.giveFeedback(guess, wordToGuess);
        this.feedback.add(fb);
    }

    public void makeGuess(String guess){
        provideFeedback(guess);
        // provide a hint with the new feedback and the old hint
        provideHint(getLastFeedback().getFeedback(), getLastHint().getHint());
        if(feedback.size() == 5 && !getLastFeedback().isWordGuessed()){
            throw new NoMoreTurnsException();
        }
    }

    public void makeGuessInvalid(String guess){
        Feedback fb = new Feedback();
        fb.giveFeedbackInvalid(guess, wordToGuess);
        this.feedback.add(fb);
        // provide a hint with the new feedback and the old hint
        provideHint(getLastFeedback().getFeedback(), getLastHint().getHint());
        if(feedback.size() == 5 && !getLastFeedback().isWordGuessed()){
            throw new NoMoreTurnsException();
        }
    }

    public Hint getLastHint(){
        return hints.get(hints.size()-1);
    }

    public Feedback getLastFeedback(){
        return feedback.get(feedback.size()-1);
    }

    public List<Hint> getHints() {
        return hints;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }
}
