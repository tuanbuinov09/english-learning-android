package com.myapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.myapp.dialog.CustomDialog;
import com.myapp.model.TranslationHistory;
import com.myapp.sqlite.DatabaseHelper;
import com.myapp.sqlite.dao.TranslationHistoryDao;
import com.myapp.utils.SoftKeyboard;
import com.myapp.utils.TTS;

import java.time.LocalDate;

public class TranslateTextActivity extends AppCompatActivity implements CustomDialog.Listener {
    ImageButton btnMic, btnCamera, btnImage, btnDelete, btnDownload, btnDeleteText, btnSpeak, btnSpeak2, btnCopy;
    Button btnTranslateEnglishToVietnamese, btnTranslateVietnameseToEnglish, btnTranslationHistory;
    EditText etText, etTranslatedText;
    ImageView imageView;


    private static final int REQUEST_RECOGNITION = 11;

    TranslatorOptions englishVietnameseTranslatorOptions = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build();
    final Translator englishVietnameseTranslator =
            Translation.getClient(englishVietnameseTranslatorOptions);

    TranslatorOptions vietnameseEnglishTranslatorOptions = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.VIETNAMESE)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build();
    final Translator vietnameseEnglishTranslator =
            Translation.getClient(vietnameseEnglishTranslatorOptions);

    TTS tts;

    DatabaseHelper databaseHelper;
    TranslationHistoryDao translationHistoryDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);
        setControl();
        setEvent();

        downloadModelTranslator();

        tts = new TTS(this);
        databaseHelper = DatabaseHelper.getInstance(this);
        translationHistoryDao = new TranslationHistoryDao(databaseHelper);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);
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

    private String getTextFromImage(Bitmap bitmap) {
        String result = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(TranslateTextActivity.this).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(TranslateTextActivity.this, "Error Occurred!", Toast.LENGTH_LONG).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }

            result = stringBuilder.toString();

            etText.setText(result);
        }
        return result;
    }

    private void downloadModelTranslator() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(TranslateTextActivity.this, "English Vietnamese model translator is downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslateTextActivity.this, "Failed to download English Vietnamese model translator", Toast.LENGTH_SHORT).show();
                    }
                });

        vietnameseEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(TranslateTextActivity.this, "Vietnamese English model translator is downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslateTextActivity.this, "Failed to download Vietnamese English model translator", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void translateEnglishToVietnamese(String text) {
        englishVietnameseTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        etTranslatedText.setText(translatedText);

                        TranslationHistory history = new TranslationHistory(text, translatedText, LocalDate.now());
                        boolean result = translationHistoryDao.insertOne(history);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslateTextActivity.this, "Cannot translate", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void translateVietnameseToEnglish(String text) {
        vietnameseEnglishTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        etTranslatedText.setText(translatedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TranslateTextActivity.this, "Cannot translate", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(TranslateTextActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
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
                copyToClipBoard(text);
            }
        });
        btnTranslateVietnameseToEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(TranslateTextActivity.this, "Please input text to translate", Toast.LENGTH_SHORT).show();
                    return;
                }
                translateVietnameseToEnglish(text);
            }
        });
        btnTranslateEnglishToVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString();
                if (text.isEmpty()) {
                    Toast.makeText(TranslateTextActivity.this, "Please input text to translate", Toast.LENGTH_SHORT).show();
                    return;
                }
                translateEnglishToVietnamese(text);
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
        getLifecycle().addObserver(englishVietnameseTranslator);
        englishVietnameseTranslator.close();
        getLifecycle().addObserver(vietnameseEnglishTranslator);
        vietnameseEnglishTranslator.close();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

