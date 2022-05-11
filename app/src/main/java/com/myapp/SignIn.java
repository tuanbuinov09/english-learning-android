package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {
    EditText editUsername =  null;
    EditText editPassword =  null;
    TextView labelError = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_linearlayout);
        editUsername =  findViewById(R.id.editTextUsername);
        editPassword =  findViewById(R.id.editTextPassword);
        labelError= findViewById(R.id.labelError);
        labelError.setText("");
        setEvent();
    }

    private void setEvent() {
//        TextView btnSignUp = (TextView) findViewById(R.id.textViewSignUp);
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Homescreen.this, SignUp.class);
//            startActivity(intent);
//            }
//        });
//
//        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Homescreen.this, SignUp.class);
//                startActivity(intent);
//            }
//        });
    }

    public boolean checkLogin(){
        labelError.setText("");
        if(editUsername.getText().toString().trim().equalsIgnoreCase("tuanbui")
                &&editPassword.getText().toString().trim().equalsIgnoreCase("123456")){
            return true;
        }
        labelError.setText("*Thông tin đăng nhập không đúng");
        return false;
    }

    public void handleSignInClick(View view) {
        if(checkLogin()){
//            GlobalVariables.username = editUsername.getText().toString().trim();
            Intent mainIntent = new Intent(this, Main.class);
            startActivity(mainIntent);
        }else{

        }
    }

    public void goToSignUp(View view) {
            Intent signUpIntent = new Intent(this, SignUp.class);
            startActivity(signUpIntent);
    }
}