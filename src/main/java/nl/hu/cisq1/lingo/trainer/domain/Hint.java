package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.CORRECT;

public class Hint {
    private List<Mark> marks;

    public Hint(List<Mark> marks) {
        this.marks = marks;
    }

    public List<Character> giveHint(List<Character> previousHint, String wordToGuess) {
        List<Character> hintList = new ArrayList<Character>();
        if (previousHint.size() != 0) {
            hintList.addAll(previousHint);
        } else {
            for (int i = 0; i < wordToGuess.length(); i++) {
                hintList.add('.');
            }
        }

        for (int i = 0; i < marks.size(); i++) {
            if (marks.get(i).equals(CORRECT)) {
                hintList.set(i, wordToGuess.charAt(i));
            }
        }
        return hintList;
    }
}
