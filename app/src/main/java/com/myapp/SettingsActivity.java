package com.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.model.Settings;
import com.myapp.model.VoiceSpeed;
import com.myapp.utils.FileIO;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    RadioButton rbNormal, rbSlow, rbSlower, rbUS, rbUK;
    RadioGroup rgSoundSpeed, rgSoundDefault;
    LinearLayout btnDarkTheme, lyMail;
    CheckBox cbxDarkTheme;
    Settings settings;

    SettingsRemindWordsFragment settingsRemindWordsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_linearlayout);

        setControl();
        setEvent();

        settings = FileIO.readFromFile(this);

        initRadioGroupButtons();
    }

    private void setEvent() {
        btnDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbxDarkTheme.setChecked(!cbxDarkTheme.isChecked());
            }
        });
        lyMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"abc@gmail.com"});
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Your subject here...");
//                intent.putExtra(Intent.EXTRA_TEXT,"Your message here...");
                startActivity(intent);
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
                        settings.setVoiceLanguage(Locale.UK);
                        FileIO.writeToFile(settings, SettingsActivity.this);
                        break;
                    case R.id.rbUS:
                        GlobalVariables.VOICE_LANGUAGE = Locale.US;
                        settings.setVoiceLanguage(Locale.UK);
                        FileIO.writeToFile(settings, SettingsActivity.this);
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
                        GlobalVariables.VOICE_SPEED = VoiceSpeed.SLOWER;
                        settings.setVoiceSpeed(VoiceSpeed.SLOWER);
                        FileIO.writeToFile(settings, SettingsActivity.this);
                        break;
                    case R.id.rbSlow:
                        GlobalVariables.VOICE_SPEED = VoiceSpeed.SLOW;
                        settings.setVoiceSpeed(VoiceSpeed.SLOW);
                        FileIO.writeToFile(settings, SettingsActivity.this);
                        break;
                    case R.id.rbNormal:
                        GlobalVariables.VOICE_SPEED = VoiceSpeed.NORMAL;
                        settings.setVoiceSpeed(VoiceSpeed.NORMAL);
                        FileIO.writeToFile(settings, SettingsActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initRadioGroupButtons() {
        if (settings.getVoiceLanguage() == Locale.US) {
            rbUS.setChecked(true);
        }
        if (settings.getVoiceLanguage() == Locale.UK) {
            rbUK.setChecked(true);
        }
        if (settings.getVoiceSpeed() == VoiceSpeed.SLOWER) {
            rbSlower.setChecked(true);
        }
        if (settings.getVoiceSpeed() == VoiceSpeed.SLOW) {
            rbSlow.setChecked(true);
        }
        if (settings.getVoiceSpeed() == VoiceSpeed.NORMAL) {
            rbNormal.setChecked(true);
        }
    }

    private void setControl() {
        rbNormal = findViewById(R.id.rbNormal);
        rbSlow = findViewById(R.id.rbSlow);
        rbSlower = findViewById(R.id.rbSlower);
        rbUS = findViewById(R.id.rbUS);
        rbUK = findViewById(R.id.rbUK);
        rgSoundSpeed = findViewById(R.id.rgSoundSpeed);
        rgSoundDefault = findViewById(R.id.rgSoundDefault);
        btnDarkTheme = findViewById(R.id.btnDarkTheme);
        cbxDarkTheme = findViewById(R.id.cbxDarkTheme);
        lyMail = findViewById(R.id.lyMail);
    }


    public Settings getSettings() {
        return settings;
    }

//    public void backToMain(View view){
//        Intent mainIntent = new Intent(this, Main.class);
//        startActivity(mainIntent);
//    }
}