package com.example.ahmedkhattak.hangman;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedkhattak.hangman.Models.HangmanCharacter;
import com.example.ahmedkhattak.hangman.Models.Words;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FlexboxLayout flexboxLayout, bakedKeyboardLayout;
    private ImageView hangmanImageView;
    private TextView hintTextView, guessedWordsTextView;
    Button guessButton;
    EditText wordInputEditText;
    List<HangmanCharacter> hangmanCharacterList = null;
    List<Words> wordsList = null;
    Words word;
    Random randomWordGenerator;
    AppCompatEditText lastFocusedView;
    int looseCounter = 0;
    final int looseCount = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        setFocus();
        initVars();
        generateWords();
        removeViews();
        word = getRandomWord();
        setHintTextView(word.getHint());
        renderEditTextViews(word.getWord());


    }

    private void setFocus() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initVars() {
        hangmanImageView = (ImageView) findViewById(R.id.hangmanImage);
        flexboxLayout = (FlexboxLayout) findViewById(R.id.flexWordContainer);
        hintTextView = (TextView) findViewById(R.id.wordHint);
        bakedKeyboardLayout = (FlexboxLayout) findViewById(R.id.bakedKeyboard);
        randomWordGenerator = new Random();

    }


    //render edittextviews according to given word a limit on how many characters will be shown
    // as hints the words character count is a final const say about 10 characters
    private void renderEditTextViews(String word) {

        //remove spaces the input will be sanitized before but this is just be on the safe side
        String hangWord = removeSpacesFromWord(word);
        //parse word
        char[] ch = stringToCharArray(hangWord);
        //gen random hints from word
        List<Integer> hintPositonal = getRandom(ch.length, 4); //limit must be <= to char.length no checks inplace !
        hangmanCharacterList = new ArrayList<>();
        for (int i = 0; i < ch.length; i++) {
            //generate HangmanCharacter List
            hangmanCharacterList.add(new HangmanCharacter(ch[i], false));
        }

        for (Integer integer : hintPositonal) {
            hangmanCharacterList.get(integer).setHint(true);
        }

        for (HangmanCharacter hangmanCharacter : hangmanCharacterList) {
            Log.d(TAG, hangmanCharacter.getCharacter().toString() + hangmanCharacter.isHint());
        }


        //loop through each char and create appropriate edittext
        int x = 0;
        for (HangmanCharacter hangmanCharacter : hangmanCharacterList) {

            //set appropriate attribs of edit text and maybe set watchers or listeners for each edittext separately

            if (hangmanCharacter.isHint()) {

                //make disabled edittext
                flexboxLayout.addView(builEditTextViewWithHint(hangmanCharacter.getCharacter(), x));
                Log.d(TAG, "added view with hint");

            } else {
                //make enable edittext
                flexboxLayout.addView(builEditTextView(x));
                Log.d(TAG, "added view with no hint");
            }
            x++;


        }


    }

    private void setHintTextView(String hint) {
        hintTextView.setText(hint);
    }

    private String removeSpacesFromWord(String word) {
        return word.replaceAll("\\s+", "");
    }

    private char[] stringToCharArray(String word) {
        return word.toCharArray();
    }

    private List<Integer> getRandom(int length, int limit) {
        List<Integer> noice = new ArrayList<>();
        //note a single Random object is reused here
        Random randomGenerator = new Random();
        for (int x = 0; x < limit; x++) {
            noice.add(randomGenerator.nextInt(length));
        }
        return noice;
    }


    //destroy any child views if they exist
    private void removeViews() {
        //then get rid of all views that represent the data
        flexboxLayout.removeViews(0, flexboxLayout.getChildCount());
        //enable buttons
        for (int x = 0; x < bakedKeyboardLayout.getChildCount(); x++) {
            bakedKeyboardLayout.getChildAt(x).setEnabled(true);
        }


    }

    private AppCompatEditText builEditTextView(int id) {
        final AppCompatEditText editTextView = new AppCompatEditText(this);
        editTextView.setVisibility(View.VISIBLE);
        editTextView.setCursorVisible(false);
        editTextView.setGravity(Gravity.CENTER);
        editTextView.setFocusable(false);
        editTextView.setSaveEnabled(true);
        editTextView.setInputType(InputType.TYPE_NULL);
        editTextView.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1)
        });
        editTextView.setId(id);
        editTextView.setEnabled(true);
        editTextView.setLayoutParams(createDefaultLayoutParams());
        return editTextView;

    }

    private AppCompatEditText builEditTextViewWithHint(Character text, int id) {
        final AppCompatEditText textView = new AppCompatEditText(this);
        textView.setVisibility(View.VISIBLE);
        textView.setCursorVisible(false);
        textView.setGravity(Gravity.CENTER);
        textView.setFocusable(false);
        textView.setSaveEnabled(true);
        textView.setInputType(InputType.TYPE_NULL);
        textView.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1)
        });
        textView.setId(id);
        textView.setEnabled(true);
        textView.setLayoutParams(createDefaultLayoutParams());
        textView.setText(text.toString());
        return textView;
    }


    private void setHangmanImageView() {

    }


    private void failOrWinConditionCheck() {
        if (looseCounter == looseCount) {
            //player lost the game show loose dialog and reset every thing after user clicks or dismisses dialog
            Log.d(TAG, "LOOSE !!!");
            showLoseDialog();
        } else {
            //get each edittext and get its text and transform it to a string and then do a simple string compare
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < flexboxLayout.getChildCount(); x++) {
                AppCompatEditText edditext = (AppCompatEditText) flexboxLayout.getChildAt(x);
                stringBuilder.append(edditext.getText());
            }
            if (word.getWord().equals(stringBuilder.toString())) {
                Log.d(TAG, "WIN !!!");
                showWinDialog();
            }

        }


    }


    private void resetGame() {
        //first clear data
        if (hangmanCharacterList != null) {
            hangmanCharacterList.clear();
        }
        hangmanCharacterList = null;
        word = null;
        looseCounter = 0;
        lastFocusedView = null;
        removeViews();
        word = getRandomWord();
        setHintTextView(word.getHint());
        renderEditTextViews(word.getWord());
        hangmanImageView.setImageResource(R.drawable.gallows);

    }

    private FlexboxLayout.LayoutParams createDefaultLayoutParams() {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(
                100, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        return lp;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
           /* case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "setings clicked !", Toast.LENGTH_SHORT).show();
                return true;*/

            case R.id.action_restart:

                resetGame();
                return true;

            case R.id.action_about:

                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showLoseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loose_dialog, null);
        builder.setView(dialogView)
                .setTitle("YOU LOSE !")
                .setMessage("The word was " + word.getWord())
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        resetGame();
                    }
                }).create().show();
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.win_dialog, null);
        builder.setView(dialogView)
                .setCancelable(false)
                .setTitle("YOU WIN !")
                .setMessage("Congratulations")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                    }
                }).create().show();
    }


    public void keyboardClick(View view) {

        int id;
        AppCompatButton button = (AppCompatButton) view;
        id = button.getId();
        switch (id) {

            case R.id.a:
                checkWord("a", button);
                break;

            case R.id.b:
                checkWord("b", button);
                break;

            case R.id.c:
                checkWord("c", button);
                break;

            case R.id.d:
                checkWord("d", button);
                break;

            case R.id.e:
                checkWord("e", button);
                break;

            case R.id.f:
                checkWord("f", button);
                break;

            case R.id.g:
                checkWord("g", button);
                break;

            case R.id.h:
                checkWord("h", button);
                break;

            case R.id.i:
                checkWord("i", button);
                break;

            case R.id.j:
                checkWord("j", button);
                break;

            case R.id.k:
                checkWord("k", button);
                break;

            case R.id.l:
                checkWord("l", button);
                break;


            case R.id.m:
                checkWord("m", button);
                break;

            case R.id.n:
                checkWord("n", button);
                break;

            case R.id.o:
                checkWord("o", button);
                break;

            case R.id.p:
                checkWord("p", button);
                break;

            case R.id.q:
                checkWord("q", button);
                break;

            case R.id.r:
                checkWord("r", button);
                break;


            case R.id.s:
                checkWord("s", button);
                break;

            case R.id.t:
                checkWord("t", button);
                break;

            case R.id.u:
                checkWord("u", button);
                break;


            case R.id.v:
                checkWord("v", button);
                break;

            case R.id.w:
                checkWord("w", button);
                break;


            case R.id.x:
                checkWord("x", button);
                break;


            case R.id.y:
                checkWord("y", button);
                break;


            case R.id.z:
                checkWord("z", button);
                break;


            default:
                Toast.makeText(this, "this should not happen !", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //make words nondynamically a better way would be to have sqlite db with these columns
    //category,word,hint. Here only words and hints are given
    public void generateWords() {

        wordsList = new ArrayList<>();
        wordsList.add(new Words("karachi", "City of lights"));
        wordsList.add(new Words("madinamarket", "Has best chicken karahi around !"));
        wordsList.add(new Words("savourfoods", "Has best pulao around town (not really)"));
        wordsList.add(new Words("lahore", "The Walled City"));
        wordsList.add(new Words("google", "Portal of unlimited knowledge"));
        wordsList.add(new Words("fingers", "Attached to your hands"));
        wordsList.add(new Words("eyes", "Window to the soul"));
        wordsList.add(new Words("android", "Half human half machine"));
        wordsList.add(new Words("apple", "Saib"));
        wordsList.add(new Words("achtung", "Attention in german"));
        wordsList.add(new Words("arnold", "Hastalavista"));
        wordsList.add(new Words("assignments", "Bane of university life"));
    }

    public Words getRandomWord() {
        int idx = randomWordGenerator.nextInt(wordsList.size());
        return wordsList.get(idx);
    }

    private void checkWord(String charsequence, AppCompatButton button) {
        if (word.getWord().contains(charsequence)) {

            //since each edit text is given an integer id starting from 0 which is same as
            //each index of a character in a given word so we correlate that to find which edittext needs to be
            //set with some text

            //find all occurences of each char in the word and set the related edditext accordingly !

            for (int index = word.getWord().indexOf(charsequence); index >= 0; index = word.getWord().indexOf(charsequence, index + 1)) {
                Log.d(TAG, String.valueOf(index));
                AppCompatEditText edittext = (AppCompatEditText) flexboxLayout.getChildAt(index);
                edittext.setText(charsequence);
            }
            failOrWinConditionCheck();

        } else {
            //change color to indicate the character selected was wrong also maybe disable it ?
            button.setEnabled(false);
            //check fail or win condition
            looseCounter++;
            showHangman();
            failOrWinConditionCheck();
        }
    }


    private void showHangman() {
        if (looseCounter == 1) {
            hangmanImageView.setImageResource(R.drawable.hangmanhead);
        } else if (looseCounter == 2) {
            hangmanImageView.setImageResource(R.drawable.hangman_head_leftarm);
        } else if (looseCounter == 3) {
            hangmanImageView.setImageResource(R.drawable.hangman_head_leftarm_righarm);
        } else if (looseCounter == 4) {
            hangmanImageView.setImageResource(R.drawable.hangman_head_leftarm_rightarm_torso);
        } else if (looseCounter == 5) {
            hangmanImageView.setImageResource(R.drawable.hangman_body_leftleg);
        } else if (looseCounter == 6) {
            hangmanImageView.setImageResource(R.drawable.fullhangman);
        }
    }
}
