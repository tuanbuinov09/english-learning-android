package com.myapp.dictionary;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.Main;
import com.myapp.R;
import com.myapp.adapter.MeaningRecyclerAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;

public class EnWordDetailActivity extends AppCompatActivity {
    public EnWord savedWord;
    public int enWordId;
    public ImageButton buttonSpeak;
    public TextView textViewTitle;
    public TextView textViewWord;
    public TextView textViewMeaningAndExample;
    public ImageButton btnSave_UnsaveWord;
    public TextView textViewPronunciation;
    LinearLayoutManager manager;
    public RecyclerView meaningRecyclerView;
    TextToSpeech ttobj;

    public boolean unsave = true;

//    private TabLayout tabLayout;
//    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_word_detail);

        enWordId = getIntent().getIntExtra("enWordId", -1);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        savedWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();


//        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    ttobj.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });


        setControl();
        setEvent();
    }

    private void setEvent() {

        MeaningRecyclerAdapter meaningRecyclerAdapter = new MeaningRecyclerAdapter(this, savedWord.getListMeaning());
        meaningRecyclerView.setAdapter(meaningRecyclerAdapter);
        manager = new LinearLayoutManager(this);
        meaningRecyclerView.setLayoutManager(manager);
        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Main.ttobj.speak(savedWord.getWord(), TextToSpeech.QUEUE_FLUSH, null);
                Toast.makeText(EnWordDetailActivity.this, savedWord.getWord(), Toast.LENGTH_SHORT).show();
            }
        });
        btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unsave == true) {
                    //---run unsave code
//                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    unsave = !unsave;
                } else {
                    //---run save code
                    //btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    unsave = !unsave;
                }
            }
        });
        textViewTitle.setText(savedWord.getWord());
        textViewWord.setText(savedWord.getWord());
        textViewPronunciation.setText(savedWord.getPronunciation());

//        if(savedWord.getListMeaning().size()!=0){
//            String meaningContent="";
//            for(Meaning meaning : savedWord.getListMeaning()){
//                meaningContent = meaningContent + "\n- " + meaning.getMeaning();
//                String exampleContent = "";
//                if(meaning.getListExampleDetails().size()!=0){
//                    for(ExampleDetail exampleDetail : meaning.getListExampleDetails()){
//                        exampleContent = exampleContent + "\n\t+ "+ exampleDetail.getExample()+" : "+exampleDetail.getExampleMeaning();
//                    }
//                }
//                meaningContent= meaningContent + exampleContent;
//            }
//
//            textViewMeaningAndExample.setText(meaningContent);
//        }

    }

    private void setControl() {
        buttonSpeak = findViewById(R.id.buttonSpeak);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewWord = findViewById(R.id.textViewWord);
//        textViewMeaningAndExample = findViewById(R.id.textViewMeaningAndExample);
        btnSave_UnsaveWord = findViewById(R.id.btnSave_UnsaveWord);
        textViewPronunciation = findViewById(R.id.textViewPronunciation);
        meaningRecyclerView = findViewById(R.id.recyclerView);
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

}