package com.myapp.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.myapp.model.TranslationHistory;
import com.myapp.sqlite.dao.TranslationHistoryDao;

import java.time.LocalDate;

public class MyTranslator {
    private Context context;
    private TranslatorOptions englishVietnameseTranslatorOptions = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.VIETNAMESE)
            .build();
    private final Translator englishVietnameseTranslator =
            Translation.getClient(englishVietnameseTranslatorOptions);

    private TranslatorOptions vietnameseEnglishTranslatorOptions = new TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.VIETNAMESE)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build();
    private final Translator vietnameseEnglishTranslator =
            Translation.getClient(vietnameseEnglishTranslatorOptions);

    public MyTranslator(Context context) {
        this.context = context;
        downloadModelTranslator();
    }

    private void downloadModelTranslator() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(CropActivity.this, "English Vietnamese model translator is downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(CropActivity.this, "Failed to download English Vietnamese model translator", Toast.LENGTH_SHORT).show();
                    }
                });

        vietnameseEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(CropActivity.this, "Vietnamese English model translator is downloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(CropActivity.this, "Failed to download Vietnamese English model translator", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void translateEnglishToVietnamese(String text, TextView tvOriginalText, TextView tvTranslatedText) {
        englishVietnameseTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        tvOriginalText.setText(text);
                        tvTranslatedText.setText(translatedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Không thể dịch được", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void translateVietnameseToEnglish(String text) {
        vietnameseEnglishTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        //etTranslatedText.setText(translatedText);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Không thể dịch được", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void translateEnglishToVietnamese(String text, EditText etTranslatedText, TranslationHistoryDao translationHistoryDao) {
        englishVietnameseTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        etTranslatedText.setText(translatedText);

                        TranslationHistory history = new TranslationHistory(text, translatedText, LocalDate.now());
                        translationHistoryDao.insertOne(history);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Cannot translate", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void translateVietnameseToEnglish(String text, EditText etTranslatedText, TranslationHistoryDao translationHistoryDao) {
        vietnameseEnglishTranslator.translate(text)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(@NonNull String translatedText) {
                        etTranslatedText.setText(translatedText);
                        TranslationHistory history = new TranslationHistory(text, translatedText, LocalDate.now());
                        translationHistoryDao.insertOne(history);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Không thể dịch", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void finish() {
        englishVietnameseTranslator.close();
        vietnameseEnglishTranslator.close();
    }
}
