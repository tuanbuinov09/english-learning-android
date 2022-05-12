package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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