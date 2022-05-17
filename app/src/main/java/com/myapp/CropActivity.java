package com.myapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.myapp.utils.CopyToClipBoard;
import com.myapp.utils.MyTranslator;

import java.io.IOException;

import br.vince.owlbottomsheet.OwlBottomSheet;

public class CropActivity extends AppCompatActivity {
    Button btnTranslate;
    CropImageView cropImageView;
    OwlBottomSheet mBottomSheet;
    TextView tvOriginalText, tvTranslatedText;
    ImageButton btnCopy1, btnCopy2;

    MyTranslator myTranslator;

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

        myTranslator = new MyTranslator(this);

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

    private void setEvent() {
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap croppedImage = cropImageView.getCroppedImage();
                if (croppedImage == null) {
                    Toast.makeText(CropActivity.this, "Xin hãy chọn lại hình", Toast.LENGTH_SHORT).show();
                } else {
                    //String text = getTextFromImage(croppedImage);
                    //myTranslator.translateEnglishToVietnamese(text, tvOriginalText, tvTranslatedText);
                    //mBottomSheet.expand();
                    recognizeTextAndTranslate(croppedImage);
                }
            }
        });
        btnCopy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyToClipBoard.doCopy(CropActivity.this, tvOriginalText.getText().toString());
            }
        });
        btnCopy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CopyToClipBoard.doCopy(CropActivity.this, tvTranslatedText.getText().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
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

    private void recognizeTextAndTranslate(Bitmap bitmap) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text result) {
                                // Task completed successfully
                                // ...

                                String resultText = result.getText();
                                for (Text.TextBlock block : result.getTextBlocks()) {
                                    String blockText = block.getText();
                                    Point[] blockCornerPoints = block.getCornerPoints();
                                    Rect blockFrame = block.getBoundingBox();
                                    for (Text.Line line : block.getLines()) {
                                        String lineText = line.getText();
                                        Point[] lineCornerPoints = line.getCornerPoints();
                                        Rect lineFrame = line.getBoundingBox();
                                        for (Text.Element element : line.getElements()) {
                                            String elementText = element.getText();
                                            Point[] elementCornerPoints = element.getCornerPoints();
                                            Rect elementFrame = element.getBoundingBox();
                                        }
                                    }
                                }

                                myTranslator.translateEnglishToVietnamese(resultText, tvOriginalText, tvTranslatedText);
                                mBottomSheet.expand();
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                        Toast.makeText(CropActivity.this, "Có lỗi xảy ra trong quá trình chuyển từ hình sang văn bản", Toast.LENGTH_LONG).show();
                                    }
                                });
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

    //    private String getTextFromImage(Bitmap bitmap) {
//        String result = "";
//        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
//        if (!textRecognizer.isOperational()) {
//            Toast.makeText(this, "Có lỗi xảy ra trong quá trình chuyển từ hình sang văn bản", Toast.LENGTH_LONG).show();
//        } else {
//            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < textBlockSparseArray.size(); i++) {
//                TextBlock textBlock = textBlockSparseArray.valueAt(i);
//                stringBuilder.append(textBlock.getValue());
//                stringBuilder.append("\n");
//            }
//
//            result = stringBuilder.toString();
//        }
//        return result;
//    }
}