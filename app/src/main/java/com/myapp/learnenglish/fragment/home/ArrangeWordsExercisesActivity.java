package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.ExercisesRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.adapter.UnitsRecyclerViewAdapter;

import java.util.ArrayList;

public class ArrangeWordsExercisesActivity extends AppCompatActivity {
    private ArrayList<String> tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_exercises);

        tempData = new ArrayList<>();
        tempData.add("Exercise 1");
        tempData.add("Exercise 2");
        tempData.add("Exercise 3");
        tempData.add("Exercise 4");
        tempData.add("Exercise 5");

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_exercise_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(this, tempData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}