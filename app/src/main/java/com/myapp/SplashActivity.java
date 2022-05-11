package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.dtbassethelper.DatabaseAccess;

public class SplashActivity extends AppCompatActivity {
    DatabaseAccess DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        },2000);
    }

    private void nextActivity() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        DB.iduser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(user==null){
            //Chưa login
            Intent intent = new Intent(this,SignInActivity.class);
            startActivity(intent);

        }else{
            Intent intent = new Intent(this,Main.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),
                    DB.iduser,
                    Toast.LENGTH_LONG)
                    .show();
        }
        finish();
    }
}