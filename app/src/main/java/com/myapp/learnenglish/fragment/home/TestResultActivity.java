package com.myapp.learnenglish.fragment.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.R;

public class TestResultActivity extends AppCompatActivity {
    private TextView tvObtainedStars, tvInTotal;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_test_result);

        setControl();
        setEvent();

        tvInTotal.setText(getIntent().getExtras().get("total").toString());
        tvObtainedStars.setText(getIntent().getExtras().get("obtainedStars").toString());
    }

    private void setEvent() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        tvObtainedStars = findViewById(R.id.tvObtainedStars);
        tvInTotal = findViewById(R.id.tvInTotal);
        btnContinue = findViewById(R.id.btnContinue);
    }
}