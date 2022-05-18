package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.dialog.CustomDialog;
import com.myapp.dictionary.EnWordDetailActivity2;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.sqlite.DatabaseHelper;
import com.myapp.sqlite.dao.TranslationHistoryDao;
import com.myapp.utils.CopyToClipBoard;
import com.myapp.utils.MyTranslator;
import com.myapp.utils.SoftKeyboard;
import com.myapp.utils.TTS;

public class TranslateTextActivity extends AppCompatActivity implements CustomDialog.Listener {
    ImageButton btnMic, btnCamera, btnImage, btnDelete, btnDownload, btnDeleteText, btnSpeak, btnSpeak2, btnCopy;
    Button btnTranslateEnglishToVietnamese, btnTranslateVietnameseToEnglish, btnTranslationHistory;
    EditText etText, etTranslatedText;
    ImageView imageView;


    private static final int REQUEST_RECOGNITION = 11;

    MyTranslator myTranslator;
    TTS tts;

    DatabaseHelper databaseHelper;
    TranslationHistoryDao translationHistoryDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);
        setControl();
        setEvent();

        tts = new TTS(this);
        databaseHelper = DatabaseHelper.getInstance(this);
        translationHistoryDao = new TranslationHistoryDao(databaseHelper);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);

        myTranslator = new MyTranslator(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_RECOGNITION && resultCode == RESULT_OK) {
            String speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            etText.setText(speechText);
        }
    }

    private void handleBtnCameraClicked(View view) {
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("request", CropActivity.OPEN_CAMERA_CODE);
        startActivity(intent);
    }

    private void handleBtnMicClicked(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bắt đầu nói");
        startActivityForResult(intent, 111);
    }

    private void handleBtnImageClicked(View view) {
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("request", CropActivity.OPEN_GALLERY_CODE);
        startActivity(intent);
    }

    private void handleBtnDeleteClicked(View view) {

    }

    private void handleBtnDownloadClicked(View view) {

    }

    private void setEvent() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBtnCameraClicked(view);
            }
        });
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBtnMicClicked(view);
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBtnImageClicked(view);
            }
        });
        btnDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etText.setText("");
                SoftKeyboard.showKeyboard(etText, TranslateTextActivity.this);
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString().trim();
                tts.speak(text);
            }
        });
        btnSpeak2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etTranslatedText.getText().toString().trim();
                tts.speak(text);
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etTranslatedText.getText().toString().trim();
                CopyToClipBoard.doCopy(TranslateTextActivity.this, text);
            }
        });
        btnTranslateVietnameseToEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(TranslateTextActivity.this, "Hãy nhập văn bản", Toast.LENGTH_SHORT).show();
                    return;
                }
                myTranslator.translateVietnameseToEnglish(text.trim(), etTranslatedText, translationHistoryDao);
            }
        });
        btnTranslateEnglishToVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(TranslateTextActivity.this, "Hãy nhập văn bản", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] wordList = text.trim().split(" ");
                if (wordList.length == 1) {
                    EnWord enWord = getEnWord(text);
                    if (enWord != null) {
                        Intent intent = new Intent(TranslateTextActivity.this, EnWordDetailActivity2.class);
                        Integer enWordId = enWord.getId();
                        intent.putExtra("enWordId", enWordId);
                        startActivity(intent);
                        return;
                    }
                }
                myTranslator.translateEnglishToVietnamese(text, etTranslatedText, translationHistoryDao);
            }
        });
        btnTranslationHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TranslateTextActivity.this, TranslationHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControl() {
        btnCamera = findViewById(R.id.btnCamera);
        btnMic = findViewById(R.id.btnMic);
        btnImage = findViewById(R.id.btnImage);
        btnDelete = findViewById(R.id.btnDelete);
        btnDownload = findViewById(R.id.btnDownload);
        etText = findViewById(R.id.etText);
        etTranslatedText = findViewById(R.id.etTranslatedText);
        imageView = findViewById(R.id.imageView);
        btnDeleteText = findViewById(R.id.btnDeleteText);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnSpeak2 = findViewById(R.id.btnSpeak2);
        btnCopy = findViewById(R.id.btnCopy);
        btnTranslateEnglishToVietnamese = findViewById(R.id.btnTranslateEnglishToVietnamese);
        btnTranslateVietnameseToEnglish = findViewById(R.id.btnTranslateVietnameseToEnglish);
        btnTranslationHistory = findViewById(R.id.btnTranslationHistory);
    }

    @Override
    public void sendDialogResult(CustomDialog.Result result, String request) {
        if (result == CustomDialog.Result.OK) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTranslator.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private EnWord getEnWord(String keyword) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        EnWord enWord = databaseAccess.getOneEnWord(keyword);
        databaseAccess.close();
        return enWord;
    }
}

