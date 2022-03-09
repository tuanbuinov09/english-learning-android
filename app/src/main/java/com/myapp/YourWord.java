package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class YourWord extends AppCompatActivity {
    EditText searchInput = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_word_linearlayout);

        searchInput = findViewById(R.id.searchInput);
    }

    public void search(View view){
        Toast.makeText(this,"bạn vừa tìm: " + searchInput.getText().toString().trim() + "trong từ của bạn",Toast.LENGTH_LONG).show();
    }



}