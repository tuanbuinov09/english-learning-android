package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class SignUp extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    TextView labelErrorUsername = null;
    TextView labelErrorPassword = null;
    TextView labelErrorConfirmPassword = null;
    TextView labelErrorFullname = null;
    TextView labelErrorEmail = null;

    TextView editTextUsername = null;
    TextView editTextPassword = null;
    TextView editTextConfirmPassword = null;
    TextView editTextFullname = null;
    TextView editTextEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_linearlayout);
        System.out.println("SignUp created");

        labelErrorUsername = findViewById(R.id.labelErrorUsername);
        labelErrorPassword = findViewById(R.id.labelErrorPassword);
        labelErrorConfirmPassword = findViewById(R.id.labelErrorConfirmPassword);
        labelErrorFullname = findViewById(R.id.labelErrorFullname);
        labelErrorEmail = findViewById(R.id.labelErrorEmail);

        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
        labelErrorConfirmPassword.setText("");
        labelErrorFullname.setText("");
        labelErrorEmail.setText("");

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextFullname = findViewById(R.id.editTextFullname);
        editTextEmail = findViewById(R.id.editTextEmail);
    }

    public void goToSignIn(View view) {
        Intent signInIntent = new Intent(this, SignIn.class);
        startActivity(signInIntent);
    }

    public boolean validateSignUp() {
        boolean check = true;
        if(editTextUsername.getText().toString().trim().isEmpty()){
            labelErrorUsername.setText("*Hãy nhập tên đăng nhập");
            check = false;
        }else if(editTextUsername.getText().toString().trim().length()<6){
            labelErrorUsername.setText("*Tên đăng nhập tối thiểu 6 ký tự");
            check = false;
        }

        if(editTextPassword.getText().toString().trim().isEmpty()){
            labelErrorPassword.setText("*Hãy nhập mật khẩu");
            check = false;
        }else if(editTextPassword.getText().toString().trim().length()<6){
            labelErrorPassword.setText("*Mật khẩu tối thiểu 6 ký tự");
            check = false;
        }

        if(!editTextConfirmPassword.getText().toString().trim().equals(editTextPassword.getText().toString().trim())){
            labelErrorConfirmPassword.setText("*Xác nhận mật khẩu không khớp");
            check = false;
        }

        if(editTextFullname.getText().toString().trim().isEmpty()){
            labelErrorFullname.setText("*Hãy nhập họ tên");
            check = false;
        }

        if(editTextEmail.getText().toString().trim().isEmpty()){
            labelErrorEmail.setText("*Hãy nhập email");
            check = false;
        }else if(!validateEmail(editTextEmail.getText().toString().trim())){
            labelErrorEmail.setText("*Email không hợp lệ");
            check = false;
        }

        return check;
    }

    public void handleSignUpClick(View view) {
        labelErrorUsername.setText("");
        labelErrorPassword.setText("");
        labelErrorConfirmPassword.setText("");
        labelErrorFullname.setText("");
        labelErrorEmail.setText("");

        if (validateSignUp()) {

//            GlobalVariables.username = editTextUsername.getText().toString().trim();

            Intent mainIntent = new Intent(this, Main.class);
            startActivity(mainIntent);
        }
    }
}