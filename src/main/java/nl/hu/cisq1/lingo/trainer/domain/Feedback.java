package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.*;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;
import static nl.hu.cisq1.lingo.trainer.domain.Mark.INVALID;

public class Feedback {
    private String guess;
    private String wordToGuess;
    private List<Mark> feedback;

    public Feedback(String guess, String wordToGuess) {
        this.guess = guess;
        this.wordToGuess = wordToGuess;
    }

    public List<Mark> giveFeedback(){
        List<Mark> feedbackList = new ArrayList<Mark>();
        //check lengte en of woord bestaat, indien niet correct -> return invalid
        if(wordToGuess.length() != guess.length()){
            for(int i = 0; i < guess.length(); i++){
                feedbackList.add(Mark.INVALID);
            }
            this.feedback = feedbackList;
            return feedbackList;
        }

        // elke char van de guessedWord langsgaan
        for (int i = 0; i < guess.length(); i++){
            // als de char voorkomt in het te raden woord:
            if(wordToGuess.indexOf(guess.charAt(i)) != -1){
                // als de index hetzelfde is, is het correct
                if(wordToGuess.charAt(i) == guess.charAt(i)){
                    feedbackList.add(Mark.CORRECT);
                }
                // anders is het present
                else{
                    feedbackList.add(Mark.PRESENT);
                }
            }
            // char komt niet voor -> absent
            else{
                feedbackList.add(Mark.ABSENT);
            }
        }
        this.feedback = feedbackList;
        return feedbackList;
    }

    public boolean isWordGuessed(){
        return feedback.stream().allMatch(mark -> mark == CORRECT);
    }

    public boolean isGuessValid(){
        return feedback.stream().noneMatch(mark -> mark == INVALID);
    }

    public void setFeedback(List<Mark> feedback) {
        if(feedback.size() != wordToGuess.length()){
            throw new InvalidFeedbackException(guess.length(), wordToGuess.length());
        }
        this.feedback = feedback;
    }

    public List<Mark> getFeedback() {
        return feedback;
    }

}

