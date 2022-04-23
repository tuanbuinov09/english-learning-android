package com.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;

import java.util.Locale;

public class EnWordDetailActivity extends AppCompatActivity {
    public EnWord savedWord;

    public ImageButton buttonSpeak;
    public TextView textViewTitle;
    public TextView textViewWord;
    public TextView textViewMeaningAndExample;
    public ImageButton btnSave_UnsaveWord;
    public TextView textViewPronunciation;
    TextToSpeech ttobj;

    public boolean unsave = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_word_detail);

        savedWord = (EnWord) getIntent().getSerializableExtra("enWord");

        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttobj.setLanguage(Locale.ENGLISH);
                }
            }
        });

        setControl();
        setEvent();
    }

    private void setEvent() {
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttobj.speak(savedWord.getWord(), TextToSpeech.QUEUE_FLUSH, null);
                Toast.makeText(EnWordDetailActivity.this, savedWord.getWord(), Toast.LENGTH_SHORT).show();
            }
        });
        btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unsave==true){
                    //---run unsave code
                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    unsave = !unsave;
                }else{
                    //---run save code
                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    unsave = !unsave;
                }
            }
        });
        textViewTitle.setText(savedWord.getWord());
        textViewWord.setText(savedWord.getWord());
        textViewPronunciation.setText(savedWord.getPronunciation());

        if(savedWord.getListMeaning().size()!=0){
            String meaningContent="";
            for(Meaning meaning : savedWord.getListMeaning()){
                meaningContent = meaningContent + "\n-" + meaning.getMeaning();
                String exampleContent = "";
                if(meaning.getListExampleDetails().size()!=0){
                    for(ExampleDetail exampleDetail : meaning.getListExampleDetails()){
                        exampleContent = exampleContent + "\n\t+ "+ exampleDetail.getExample()+"\n\t: "+exampleDetail.getExampleMeaning();
                    }
                }
                meaningContent= meaningContent + exampleContent;
            }

            textViewMeaningAndExample.setText(meaningContent);
        }

    }

    private void setControl() {
        buttonSpeak = findViewById(R.id.buttonSpeak);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewWord = findViewById(R.id.textViewWord);
        textViewMeaningAndExample = findViewById(R.id.textViewMeaningAndExample);
        btnSave_UnsaveWord = findViewById(R.id.btnSave_UnsaveWord);
        textViewPronunciation = findViewById(R.id.textViewPronunciation);
    }

}