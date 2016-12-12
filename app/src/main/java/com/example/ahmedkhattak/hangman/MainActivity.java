package com.example.ahmedkhattak.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FlexboxLayout flexboxLayout;
    private ImageView hangmanImageView;
    private TextView hintTextView;
    Button guessButton;
    EditText wordInputEditText;
    List<HangmanCharacter> hangmanCharacterList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        setFocus();
        initVars();
        removeViews();
        setHintTextView("Its a me mariiio a very noice hint !");
        renderEditTextViews("Ahmed Khattak");

    }

    private void setFocus() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initVars() {
        hangmanImageView = (ImageView) findViewById(R.id.hangmanImage);
        flexboxLayout = (FlexboxLayout) findViewById(R.id.flexWordContainer);
        hintTextView = (TextView) findViewById(R.id.wordHint);
        wordInputEditText = (EditText) findViewById(R.id.wordInput);
        guessButton = (Button) findViewById(R.id.guessButton);
    }

    private void setListeners() {
        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    //render edittextviews according to given word a limit on how many characters will be shown
    // as hints the words character count is a final const say about 10 characters
    private void renderEditTextViews(String word) {

        //remove spaces the input will be sanitized before but this is just be on the safe side
        String hangWord = removeSpacesFromWord(word);
        //parse word
        char[] ch = stringToCharArray(hangWord);
        //gen random hints from word
        List<Integer> hintPositonal = getRandom(ch.length, 3); //limit must be <= to char.length no checks inplace !
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
        //first clear data
        if (hangmanCharacterList != null) {
            hangmanCharacterList.clear();
        }
        hangmanCharacterList = null;
        //then get rid of all views that represent the data
        flexboxLayout.removeViews(0, flexboxLayout.getChildCount());

    }

    private EditText builEditTextView(int id) {
        EditText editText = new EditText(this);
        editText.setVisibility(View.VISIBLE);
        editText.setCursorVisible(false);
        editText.setGravity(Gravity.CENTER);
        editText.setFocusable(true);
        editText.setSaveEnabled(true);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1)
        });
        editText.setId(id);
        editText.setLayoutParams(createDefaultLayoutParams());




        /*
         android:cursorVisible="false"
                android:gravity="center"
                android:imeOptions="actionDone"
                android:inputType="textCapCharacters|textNoSuggestions"
                android:maxLength="1"
                android:saveEnabled="false
         */


        return editText;

    }

    private EditText builEditTextViewWithHint(Character text, int id) {
        EditText editText = new EditText(this);
        editText.setVisibility(View.VISIBLE);
        editText.setCursorVisible(false);
        editText.setGravity(Gravity.CENTER);
        editText.setFocusable(false);
        editText.setSaveEnabled(true);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(1)
        });
        editText.setId(id);
        editText.setLayoutParams(createDefaultLayoutParams());
        editText.setText(text.toString());




        /*
       android:cursorVisible="false"
                android:focusable="false"
                android:gravity="center"
                android:inputType="none|"
                android:maxLength="1"
                android:saveEnabled="false"
         */


        return editText;
    }


    private void setHangmanImageView() {

    }


    private void failOrWinConditionCheck() {

    }

    private void showWinImage() {

    }

    private void showLoseImage() {

    }

    private void resetGame() {

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

                Toast.makeText(this, "restart game clicked !", Toast.LENGTH_SHORT).show();
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
}
