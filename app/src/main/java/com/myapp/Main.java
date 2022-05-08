package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.learnenglish.LearnEnglishActivity;

import java.util.Locale;

public class Main extends AppCompatActivity {

    private Button buttonLearnEnglish, btnToAllWord, btnToYourWord, buttonTranslateText, buttonSettings, buttonAccount;

    EditText searchInput = null;
    public static TextToSpeech ttobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linearlayout);

        setControl();
        setEvent();

        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttobj.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    private void setEvent() {
        buttonLearnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickLearnEnglish(view);
            }
        });

        btnToAllWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAllWordClick(view);
            }
        });
        btnToYourWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleYourWordClick(view);
            }
        });

        buttonTranslateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonTranslateTextClick(view);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonSettingsClick(view);
            }
        });
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAccount(view);
            }
        });
    }

    private void setControl() {
        buttonLearnEnglish = findViewById(R.id.buttonLearnEnglish);

        btnToAllWord = findViewById(R.id.btnToAllWord);
        btnToYourWord = findViewById(R.id.buttonToYourWord);

        buttonTranslateText = findViewById(R.id.buttonTranslateText);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonAccount = findViewById(R.id.buttonAccount);
        searchInput = findViewById(R.id.searchInput);
    }

    public void search(View view) {
        Toast.makeText(this, "bạn vừa tìm: " + searchInput.getText().toString().trim(), Toast.LENGTH_LONG).show();
    }

    public void toAccount(View view) {
        if (GlobalVariables.username == null) {
            // go to sign in
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivity(signInIntent);
        } else {
            Toast.makeText(this, "đăng nhập thành công", Toast.LENGTH_LONG).show();
        }
    }

    public void handleYourWordClick(View view) {
        Intent yourWordIntent = new Intent(this, YourWordActivity.class);
        startActivity(yourWordIntent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleAllWordClick(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleClickLearnEnglish(View view) {
        Intent intent = new Intent(this, LearnEnglishActivity.class);
        startActivity(intent);
    }

    private void handleButtonTranslateTextClick(View view) {
        Intent intent = new Intent(this, TranslateTextActivity.class);
        startActivity(intent);
    }

    private void handleButtonSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}