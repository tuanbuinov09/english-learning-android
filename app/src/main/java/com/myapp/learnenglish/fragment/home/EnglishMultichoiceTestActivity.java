package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.EnglishMLRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.adapter.TopicsRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.Exercise;
import com.myapp.learnenglish.fragment.home.model.Question;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.ArrayList;

public class EnglishMultichoiceTestActivity extends AppCompatActivity {
    private ArrayList<String> tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.english_multichoice_test);

        tempData = new ArrayList<>();
        tempData.add("Topic 1");
        tempData.add("Topic 4");
        tempData.add("Topic 5");
        tempData.add("Topic 6");

        initRecyclerView();
    }



    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.EnglishT);
        EnglishMLRecyclerViewAdapter adapter = new EnglishMLRecyclerViewAdapter(this, tempData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
