package com.myapp.learnenglish.fragment.home.activity.listening;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.arrangewords.ExercisesRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.adapter.listening.RVListeningExerciseAdapter;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningExercise;

import java.util.ArrayList;

public class ListeningExerciseActivity extends AppCompatActivity {
    private String path;
    public static RVListeningExerciseAdapter exercisesRecyclerViewAdapter;
    public static ArrayList<ListeningExercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_listening_exercise);

        path = getIntent().getExtras().get("path").toString();
        setTitle(getIntent().getExtras().get("title").toString());

        exercises = (ArrayList<ListeningExercise>) getIntent().getSerializableExtra("exercises");
        initRecyclerView(exercises);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_exercise_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<ListeningExercise> exercises) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExercises);
        exercisesRecyclerViewAdapter = new RVListeningExerciseAdapter(this, exercises, path);
        recyclerView.setAdapter(exercisesRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}