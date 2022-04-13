package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.flexbox.FlexboxLayout;
import com.myapp.R;

import java.util.ArrayList;

public class ArrangeWordsActivity extends AppCompatActivity {
    private FlexboxLayout flexboxLayoutYourAnswerSection, flexboxLayoutGivenWordsSection;
    private ImageButton imageButtonClose;
    private ArrayList<String> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words);
        
        setControl();
        setEvent();

        initData();
        addButtonsToChooseSection();
    }

    private void setEvent() {
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        flexboxLayoutYourAnswerSection = findViewById(com.myapp.R.id.flexboxLayoutYourAnswerSection);
        flexboxLayoutGivenWordsSection = findViewById(com.myapp.R.id.flexboxLayoutGivenWordsSection);
        imageButtonClose = findViewById(R.id.imageButtonClose);
    }

    private void initData() {
        words = new ArrayList<>();
        words.add("the");
        words.add("You");
        words.add("scared");
        words.add("shit");
        words.add("me");
        words.add("motherfucker");
        words.add("out");
        words.add("of");
    }

    private void addButtonsToChooseSection() {
        for (int i = 0; i < words.size(); i++) {
            Button button = new Button(this);
            setButtonAttributes(button);
            button.setText(words.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setEnabled(false);

                    // add a duplicate button to the answer section when a button is clicked
                    Button duplicate = new Button(ArrangeWordsActivity.this);
                    setButtonAttributes(duplicate);
                    duplicate.setText(button.getText());
                    duplicate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            button.setEnabled(true);
                            flexboxLayoutYourAnswerSection.removeView(duplicate);
                        }
                    });
                    flexboxLayoutYourAnswerSection.addView(duplicate);
                }
            });

            flexboxLayoutGivenWordsSection.addView(button);
        }
    }

    private void setButtonAttributes(Button button) {
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 15, 15);
        button.setLayoutParams(layoutParams);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setAllCaps(false);
        button.setTextSize(20);
        button.setTypeface(null, Typeface.NORMAL);
        button.setBackgroundResource(com.myapp.R.drawable.tk_button);
    }
}