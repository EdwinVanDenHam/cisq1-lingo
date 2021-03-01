package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;

public class Hint {
    private List<Character> hint;
    private List<Mark> marks;

    public Hint(List<Mark> marks) {
        this.marks = marks;
    }

    public List<Character> giveInitialHint(String wordToGuess) {
        List<Character> hintList = new ArrayList<Character>();
        for(int i = 1; i < wordToGuess.length();i++){
            if (i == 1) {
                hintList.add(wordToGuess.charAt(0));
            }
            hintList.add('.');
        }
        hint = hintList;
        return hintList;
    }

    public List<Character> giveHint(List<Character> previousHint, String wordToGuess) {
        List<Character> hintList = new ArrayList<Character>();
        // als er al een hint gegeven is:
        if (previousHint.size() != 0) {
            hintList.addAll(previousHint);
        } else {
            // Geef de eerste letter van het woord
            giveInitialHint(wordToGuess);
        }
        // alle correcte letters op de goede plek zetten
        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i).equals(CORRECT)) {
                hintList.set(i, wordToGuess.charAt(i));
            }
        }
        this.hint = hintList;
        return hintList;
    }

    public List<Character> getHint() {
        return hint;
    }
}
