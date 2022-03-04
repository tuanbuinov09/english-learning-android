package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class SignIn extends AppCompatActivity {
    EditText editUsername =  null;
    EditText editPassword =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editUsername =  (EditText) findViewById(R.id.editTextUsername);
        editPassword =  (EditText) findViewById(R.id.editTextPassword);
    }

    public boolean checkLogin(){
        if(editUsername.getText().toString().trim().equalsIgnoreCase("tuanbui")
                &&editPassword.getText().toString().trim().equalsIgnoreCase("123456")){
            return true;
        }
        return false;
    }


    public void handleSignInClick(View view) {
        if(checkLogin()){
            Intent mainIntent = new Intent(this, Main.class);
            startActivity(mainIntent);
        }
    }
    public void goToSignUp(View view) {
            Intent signUpIntent = new Intent(this, SignUp.class);
            startActivity(signUpIntent);
    }
}