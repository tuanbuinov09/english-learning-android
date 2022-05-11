package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText EmailF;
    private Button btnResetP;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EmailF = (EditText) findViewById(R.id.etEmailForgot);
        btnResetP = (Button) findViewById(R.id.btnResetPass);

        mAuth = FirebaseAuth.getInstance();

        btnResetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        String email = EmailF.getText().toString().trim();

        if(email.isEmpty()){
            EmailF.setError("Hãy nhập Email của bạn!");
            EmailF.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailF.setError("Hãy nhập đúng Email!");
            EmailF.requestFocus();
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Hãy kiểm tra (Hộp thư đến) của bạn để tiến hành thiết lập lại mật khẩu!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent);
             }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,"KHÔNG THÀNH CÔNG!Hãy kiểm tra lại Email của bạn và thử lại!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}