package com.example.ahmedkhattak.hangman;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayout;

public class MainActivity extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private ImageView hangmanImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        setFocus();
        initVars();

    }

    private void setFocus() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initVars() {
        hangmanImageView = (ImageView) findViewById(R.id.hangmanImage);
        flexboxLayout=(FlexboxLayout)findViewById(R.id.flexWordContainer);
    }


    //render edittextviews according to given word
    private void renderEditTextViews(String word)
    {

    }






}
