package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.ExercisesRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.Exercise;
import com.myapp.learnenglish.fragment.home.model.Question;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrangeWordsExercisesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_exercises);
        setTitle(getIntent().getExtras().get("title").toString());

        ArrayList<Exercise> exercises = (ArrayList<Exercise>) getIntent().getSerializableExtra("exercises");
        initRecyclerView(exercises);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_exercise_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<Exercise> exercises) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        ExercisesRecyclerViewAdapter adapter = new ExercisesRecyclerViewAdapter(this, exercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}