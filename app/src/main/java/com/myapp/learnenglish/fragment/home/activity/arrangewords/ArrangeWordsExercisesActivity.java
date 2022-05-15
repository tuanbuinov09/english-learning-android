package com.myapp.learnenglish.fragment.home.activity.arrangewords;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.arrangewords.ExercisesRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;

import java.util.ArrayList;

public class ArrangeWordsExercisesActivity extends AppCompatActivity {
    private String path;
    public static ExercisesRecyclerViewAdapter exercisesRecyclerViewAdapter;
    public static ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_exercises);

        path = getIntent().getExtras().get("path").toString();
        setTitle(getIntent().getExtras().get("title").toString());

        exercises = (ArrayList<Exercise>) getIntent().getSerializableExtra("exercises");
        initRecyclerView(exercises);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_exercise_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<Exercise> exercises) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        exercisesRecyclerViewAdapter = new ExercisesRecyclerViewAdapter(this, exercises, path);
        recyclerView.setAdapter(exercisesRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}