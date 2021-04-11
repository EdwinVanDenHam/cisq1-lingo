package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;

@Entity
public class Hint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ElementCollection
    private List<Character> characters;
    @ElementCollection
    private List<Mark> marks;

    public Hint() {
        this.marks = new ArrayList<>();
    }

    public List<Character> giveInitialHint(String wordToGuess, List<Mark> marks) {
        List<Character> hintList = new ArrayList<>();
        for(int i = 1; i < wordToGuess.length();i++){
            if (i == 1) {
                hintList.add(wordToGuess.charAt(0));
            }
            hintList.add('.');
        }
        characters = hintList;
        return hintList;
    }

    public List<Character> giveHint(List<Character> previousHint, String wordToGuess, List<Mark> marks) {
        List<Character> hintList;
        // if a hint has been given already
        if (!previousHint.isEmpty()) {
            hintList = new ArrayList<>(previousHint);
        } else {
            return giveInitialHint(wordToGuess, marks);
        }
        // give feedback in chars
        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i).equals(CORRECT)) {
                hintList.set(i, wordToGuess.charAt(i));
            }
        }
        this.characters = hintList;
        return hintList;
    }

    public List<Character> getHint() {
        return characters;
    }
}
