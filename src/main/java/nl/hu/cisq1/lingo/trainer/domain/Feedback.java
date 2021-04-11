package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;
import static nl.hu.cisq1.lingo.trainer.domain.Mark.INVALID;

@Entity
public class Feedback implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String guess;
    private String wordToGuess;
    @ElementCollection
    private List<Mark> marks;

    public Feedback() {
        this.marks = new ArrayList<>();
    }

    public List<Mark> giveFeedback(String guess, String wordToGuess){
        this.guess = guess;
        this.wordToGuess = wordToGuess;
        List<Mark> feedbackList = new ArrayList<>();
        // for each char in guessed word
        for (int i = 0; i < guess.length(); i++){
            // if char is in the word to guess
            if(wordToGuess.indexOf(guess.charAt(i)) != -1){
                // if index is the same -> correct
                if(wordToGuess.charAt(i) == guess.charAt(i)){
                    feedbackList.add(Mark.CORRECT);
                }
                else{
                    feedbackList.add(Mark.PRESENT);
                }
            }
            // char not in word to guess -> absent
            else{
                feedbackList.add(Mark.ABSENT);
            }
        }
        this.marks = feedbackList;
        return feedbackList;
    }

    public List<Mark> giveFeedbackInvalid(String guess, String wordToGuess){
        this.guess = guess;
        this.wordToGuess = wordToGuess;
        List<Mark> feedbackList = new ArrayList<>();
        for(int i = 0; i < guess.length(); i++){
            feedbackList.add(Mark.INVALID);
        }
        this.marks = feedbackList;
        return feedbackList;
    }

    public boolean isWordGuessed(){
        return marks.stream().allMatch(mark -> mark == CORRECT);
    }

    public boolean isGuessValid(){
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    public List<Mark> getFeedback() {
        return marks;
    }

}

