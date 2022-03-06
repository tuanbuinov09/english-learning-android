package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    EditText searchInput = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linearlayout);

        searchInput = findViewById(R.id.searchInput);
    }

    public void search(View view){
        Toast.makeText(this,"bạn vừa tìm: " + searchInput.getText().toString().trim(),Toast.LENGTH_LONG).show();
    }
}