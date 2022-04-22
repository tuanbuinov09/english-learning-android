package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.myapp.learnenglish.LearnEnglishActivity;

public class Main extends AppCompatActivity {
    private Button buttonLearnEnglish;
    EditText searchInput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linearlayout);

        setControl();
        setEvent();
    }

    private void setEvent() {
        buttonLearnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickLearnEnglish(view);
            }
        });
    }

    private void setControl() {
        buttonLearnEnglish = findViewById(R.id.buttonLearnEnglish);
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
        }else{
            Toast.makeText(this, "đăng nhập thành công", Toast.LENGTH_LONG).show();
        }
    }

    public void handleYourWordClick(View view) {
        Intent yourWordIntent = new Intent(this, YourWordActivity.class);
        startActivity(yourWordIntent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleClickLearnEnglish(View view) {
        Intent intent = new Intent(this, LearnEnglishActivity.class);
        startActivity(intent);
    }
}