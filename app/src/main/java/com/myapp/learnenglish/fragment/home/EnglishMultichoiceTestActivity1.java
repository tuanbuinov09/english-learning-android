package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.EnglishML1RecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.adapter.ExercisesRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.Exercise;
import com.myapp.learnenglish.fragment.home.model.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EnglishMultichoiceTestActivity1 extends AppCompatActivity {
    private ArrayList<String> tempData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.english_multichoice_test_1);

        tempData = new ArrayList<>();
        tempData.add("Topic 1");
        tempData.add("Topic 4");
        tempData.add("Topic 5");
        tempData.add("Topic 6");
        initRecyclerView(tempData);
    }


    private void initRecyclerView(ArrayList<String> exercises) {
        RecyclerView recyclerView = findViewById(R.id.EnglishT1);
        EnglishML1RecyclerViewAdapter adapter = new EnglishML1RecyclerViewAdapter(this, exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
