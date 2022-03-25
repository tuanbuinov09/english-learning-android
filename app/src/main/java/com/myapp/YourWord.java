package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.myapp.model.En_word;

import java.util.ArrayList;
import java.util.Locale;

public class YourWord extends AppCompatActivity {
    EditText searchInput = null;
    ListView listViewYourWord;
    ArrayList<En_word> enWordList;
    TextToSpeech ttobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_word_linearlayout);
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
        YourWordAdapter adapter = new
                YourWordAdapter(this, this.enWordList, R.layout.yourword_listview_item, ttobj);
        listViewYourWord.setAdapter(adapter);
        listViewYourWord.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast.makeText(YourWord.this, "Bạn chọn " +
                       enWordList.get(position).getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setControl() {
        enWordList = new ArrayList<>();
        En_word en_word1 = new En_word(1, "stupid", "/'stooped/", "ngu");
        En_word en_word2 = new En_word(1, "smart", "/'stooped/ 1", "ngu 1");
        En_word en_word3 = new En_word(1, "strike", "/'stooped/ 2", "ngu 2");

        enWordList.add(en_word1);
        enWordList.add(en_word2);
        enWordList.add(en_word3);
        searchInput = findViewById(R.id.searchInput);
        listViewYourWord = findViewById(R.id.listViewYourWord);
    }

    public void search(View view){
        Toast.makeText(this,"bạn vừa tìm: " + searchInput.getText().toString().trim() + "trong từ của bạn",Toast.LENGTH_LONG).show();
    }

    public void backToMain(View view){
        Intent mainIntent = new Intent(this, Main.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
}