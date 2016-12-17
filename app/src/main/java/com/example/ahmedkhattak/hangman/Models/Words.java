package com.example.ahmedkhattak.hangman.Models;

/**
 * Created by Ahmed Khattak on 12/17/2016.
 */

public class Words {

    private String word;
    private String hint;

    public Words(String word , String hint){
        this.word=word;
        this.hint=hint;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
