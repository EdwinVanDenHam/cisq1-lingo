package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.NoMoreTurnsException;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private String wordToGuess;
    private List<Hint> hints;
    private List<Feedback> feedback;

    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.hints = new ArrayList<>();
        this.feedback = new ArrayList<>();
    }

    public void provideInitialHint(){
        Hint hint = new Hint(List.of());
        hint.giveInitialHint(wordToGuess);
        hints.add(hint);
    }

    public void provideHint(List<Mark> marks, List<Character> previousHint){
        Hint hint = new Hint(marks);
        hint.giveHint(previousHint, wordToGuess);
        hints.add(hint);
    }

    public void provideFeedback(String guess){
        Feedback feedback = new Feedback(guess, wordToGuess);
        feedback.giveFeedback();
        this.feedback.add(feedback);
    }

    public void makeGuess(String guess){
        provideFeedback(guess);
        // hint geven door de nieuwe feedback en oude hint mee te geven d.m.v. de lijsten
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
}
