package com.myapp;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class TranslateTextActivity extends AppCompatActivity implements CustomDialog.Listener {
    ImageButton btnMic, btnCamera, btnImage, btnDelete, btnDownload, btnDeleteText, btnSpeak, btnSpeak2, btnCopy;
    Button btnTranslateEnglishToVietnamese, btnTranslateVietnameseToEnglish, btnTranslationHistory;
    EditText etText, etTranslatedText;
    ImageView imageView;


    private static final int REQUEST_CAMERA_CODE = 100;
    private static final int CODE_IMG_GALLERY = 500;
    private static final int REQUEST_IMAGE_CODE = 200;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == RESULT_OK) {
            String speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            etText.setText(speechText);
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//
//            cropImageActivityResultLauncher.launch(options);
//        }

//        if(requestCode == CODE_IMG_GALLERY && resultCode == RESULT_OK) {
//
//        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);

            Uri imageUri = data.getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();

            imageView.setImageURI(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                String result = getTextFromImage(bitmap);
                etText.setText(result);
                translateEnglishToVietnamese(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = "hello.jpg";
        UCrop ucrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        ucrop.withAspectRatio(16, 9);
        ucrop.withMaxResultSize(450, 450);
        ucrop.withOptions(getCropOptions());
        ucrop.start(TranslateTextActivity.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getColor(R.color.space_cadet));
        options.setToolbarColor(getColor(R.color.steel_blue));
        options.setToolbarTitle("hello");
        return options;
    }

    private void handleBtnCameraClicked(View view) {
//        ImageCapture imageCapture =
//                new ImageCapture.Builder()
//                        .setTargetRotation(view.getDisplay().getRotation())
//                        .build();
//
//        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageCapture, imageAnalysis, preview);
//
//        ImageCapture.OutputFileOptions outputFileOptions =
//                new ImageCapture.OutputFileOptions.Builder(new File("/")).build();
//        imageCapture.takePicture(outputFileOptions, cameraExecutor,
//                new ImageCapture.OnImageSavedCallback() {
//                    @Override
//                    public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
//                        // insert your code here.
//                    }
//                    @Override
//                    public void onError(ImageCaptureException error) {
//                        // insert your code here.
//                    }
//                }
//        );

//        UCrop.of(sourceUri, destinationUri)
//                .withAspectRatio(16, 9)
//                .withMaxResultSize(maxWidth, maxHeight)
//                .start(context);

        ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .start(REQUEST_IMAGE_CODE);
    }

    private void handleBtnMicClicked(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking...");
        startActivityForResult(intent, 111);
    }

    private void handleBtnImageClicked(View view) {
        if (ContextCompat.checkSelfPermission(TranslateTextActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TranslateTextActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

//        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
//        startActivityForResult(intent, PHOTO_PICKER_REQUEST_CODE);

        ImagePicker.with(this)
                .galleryOnly()
                .start(REQUEST_IMAGE_CODE);
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

    private void requestPermission() {

    }

    Bitmap selectedImage = null;

    private void openImagePicker() {
//        TedBottomSheetDialogFragment.OnImageSelectedListener listener = new TedBottomSheetDialogFragment.OnImageSelectedListener() {
//            @Override
//            public void onImageSelected(Uri uri) {
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    selectedImage = bitmap;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
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
                translateVietnameseToEnglish(text);
            }
        });
        btnTranslateEnglishToVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = etText.getText().toString();
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
}

