package com.myapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.canhub.cropper.CropImageView;
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

import java.io.IOException;

import br.vince.owlbottomsheet.OwlBottomSheet;

public class CropActivity extends AppCompatActivity {
    Button btnTranslate;
    CropImageView cropImageView;
    OwlBottomSheet mBottomSheet;
    TextView tvOriginalText, tvTranslatedText;
    ImageButton btnCopy1, btnCopy2;

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

    private static final int REQUEST_IMAGE_CODE = 200;
    public static final int OPEN_CAMERA_CODE = 105;
    public static final int OPEN_GALLERY_CODE = 106;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);

        setControl();
        setEvent();

        cropImageView.setGuidelines(CropImageView.Guidelines.ON);
        cropImageView.setFixedAspectRatio(false);
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
        cropImageView.setAutoZoomEnabled(true);

        downloadModelTranslator();

        int request = this.getIntent().getIntExtra("request", OPEN_CAMERA_CODE);
        if (request == OPEN_CAMERA_CODE) {
            ImagePicker.with(this)
                    .cameraOnly()
                    .start(REQUEST_IMAGE_CODE);
        } else if (request == OPEN_GALLERY_CODE) {
            ImagePicker.with(this)
                    .galleryOnly()
                    .start(REQUEST_IMAGE_CODE);
        }

    }

    // basic usage
    private void setupView() {

        //used to calculate some animations. it's required
        mBottomSheet.setActivityView(this);

        //icon to show in collapsed sheet
        mBottomSheet.setIcon(R.drawable.ic_baseline_keyboard_arrow_up_24);

        //bottom sheet color
        mBottomSheet.setBottomSheetColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //view shown in bottom sheet
        mBottomSheet.attachContentView(R.layout.activty_translation_result);

        //getting close button from view shown
        mBottomSheet.getContentView().findViewById(R.id.comments_sheet_close_button)
                .setOnClickListener(v -> mBottomSheet.collapse());

        mBottomSheet.setBottomSheetColor(getColor(R.color.azureish_white));

        tvOriginalText = (TextView) mBottomSheet.getContentView().findViewById(R.id.tvOriginalText);
        tvTranslatedText = (TextView) mBottomSheet.getContentView().findViewById(R.id.tvTranslatedText);
        btnCopy1 = mBottomSheet.getContentView().findViewById(R.id.btnCopy1);
        btnCopy2 = mBottomSheet.getContentView().findViewById(R.id.btnCopy2);
    }

    private void setEvent() {
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedImage = cropImageView.getCroppedImage();
                if (croppedImage == null) {
                    Toast.makeText(CropActivity.this, "Xin hãy chọn lại hình", Toast.LENGTH_SHORT).show();
                } else {
                    String text = getTextFromImage(croppedImage);
                    translateEnglishToVietnamese(text);
                    mBottomSheet.expand();
                }
            }
        });
        btnCopy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipBoard(tvOriginalText.getText().toString());
            }
        });
        btnCopy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipBoard(tvTranslatedText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CODE) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                cropImageView.setImageBitmap(bitmap);
                cropImageView.rotateImage(90);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.crop_acticvity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCamera:
                ImagePicker.with(this)
                        .cameraOnly()
                        .start(REQUEST_IMAGE_CODE);
                break;
            case R.id.menuPicture:
                ImagePicker.with(this)
                        .galleryOnly()
                        .start(REQUEST_IMAGE_CODE);
                break;
            case R.id.menuRotate:
                cropImageView.rotateImage(90);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Đã sao chép văn bản", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(CropActivity.this, "Đã sao chép văn bản vào bộ nhớ", Toast.LENGTH_SHORT).show();
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

    private void translateEnglishToVietnamese(String text) {
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
                        Toast.makeText(CropActivity.this, "Không thể dịch được", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void translateVietnameseToEnglish(String text) {
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
                        //Toast.makeText(TranslateTextActivity.this, "Cannot translate", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getTextFromImage(Bitmap bitmap) {
        String result = "";
        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, "Có lỗi xảy ra trong quá trình chuyển từ hình sang văn bản", Toast.LENGTH_LONG).show();
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

            //etText.setText(result);
        }
        return result;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // collapse bottom sheet when back button pressed
    @Override
    public void onBackPressed() {
        if (!mBottomSheet.isExpanded())
            super.onBackPressed();
        else
            mBottomSheet.collapse();
    }

    private void setControl() {
        btnTranslate = findViewById(R.id.btnTranslate);
        mBottomSheet = findViewById(R.id.owl_bottom_sheet);
        cropImageView = findViewById(R.id.cropImageView);
        setupView();
    }
}