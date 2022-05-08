package com.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    //RadioButton rbNormal, rbSlow, rbSlower, rbUS, rbUK;
    RadioGroup rgSoundSpeed, rgSoundDefault;
    LinearLayout btnDarkTheme;
    CheckBox cbxDarkTheme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_linearlayout);

        setControl();
        setEvent();
    }

    private void setEvent() {
        btnDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbxDarkTheme.setChecked(!cbxDarkTheme.isChecked());
            }
        });

        cbxDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbxDarkTheme.setChecked(!cbxDarkTheme.isChecked());
            }
        });

        rgSoundDefault.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rbUK:
                        GlobalVariables.VOICE_LANGUAGE = Locale.UK;
                        break;
                    case R.id.rbUS:
                        GlobalVariables.VOICE_LANGUAGE = Locale.US;
                        break;
                    default:
                        break;
                }
            }
        });

        rgSoundSpeed.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rbSlower:
                        GlobalVariables.VOICE_SPEED = 0.25F;
                        break;
                    case R.id.rbSlow:
                        GlobalVariables.VOICE_SPEED = 0.5F;
                        break;
                    case R.id.rbNormal:
                        GlobalVariables.VOICE_SPEED = 1F;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setControl() {
//        rbNormal = findViewById(R.id.rbNormal);
//        rbSlow = findViewById(R.id.rbSlow);
//        rbSlower = findViewById(R.id.rbSlower);
//        rbUS = findViewById(R.id.rbUS);
//        rbUK = findViewById(R.id.rbUK);
        rgSoundSpeed = findViewById(R.id.rgSoundSpeed);
        rgSoundDefault = findViewById(R.id.rgSoundDefault);
        btnDarkTheme = findViewById(R.id.btnDarkTheme);
        cbxDarkTheme = findViewById(R.id.cbxDarkTheme);
    }

//    public void backToMain(View view){
//        Intent mainIntent = new Intent(this, Main.class);
//        startActivity(mainIntent);
//    }
}