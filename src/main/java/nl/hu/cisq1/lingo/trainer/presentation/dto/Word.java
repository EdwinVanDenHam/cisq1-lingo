package nl.hu.cisq1.lingo.trainer.presentation.dto;

import javax.validation.constraints.Pattern;

public class Word {
    @Pattern(regexp="^[A-Za-z]{1,7}$",message="The word can not be longer then 7 characters, and must only contain characters from a to z")
    public String word;

    public Word (String word){
        this.word = word;
    }

    public Word(){
    }
}
