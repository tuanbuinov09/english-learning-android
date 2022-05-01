package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_linearlayout);
    }

    public void backToMain(View view){
        Intent mainIntent = new Intent(this, Main.class);
        startActivity(mainIntent);
    }
}