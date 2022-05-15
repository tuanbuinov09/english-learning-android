package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.EnglishML1RecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.multichoice.ExerciseML;

import java.util.ArrayList;

public class EnglishMultichoiceTestActivity1 extends AppCompatActivity {
        private String path;
        public static EnglishML1RecyclerViewAdapter englishML1RecyclerViewAdapter;
        public static ArrayList<ExerciseML> exercises;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.english_multichoice_test_1);

            path = getIntent().getExtras().get("path").toString();
            setTitle(getIntent().getExtras().get("title").toString());

            exercises = (ArrayList<ExerciseML>) getIntent().getSerializableExtra("exercises");
            initRecyclerView(exercises);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.tk_exercise_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        private void initRecyclerView(ArrayList<ExerciseML> exercises) {
            RecyclerView recyclerView = findViewById(R.id.EnglishT1);
            englishML1RecyclerViewAdapter = new EnglishML1RecyclerViewAdapter(this, exercises, path);
            recyclerView.setAdapter(englishML1RecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
}