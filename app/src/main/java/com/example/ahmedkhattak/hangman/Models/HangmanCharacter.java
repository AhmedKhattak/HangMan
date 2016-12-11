package com.example.ahmedkhattak.hangman.Models;

/**
 * Created by Ahmed Khattak on 12/12/2016.
 */

public class HangmanCharacter {

    private Character character;
    private boolean isHint;

    public  HangmanCharacter(Character character , boolean isHint) {
        this.isHint=isHint;
        this.character=character;

    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean hint) {
        isHint = hint;
    }
}
