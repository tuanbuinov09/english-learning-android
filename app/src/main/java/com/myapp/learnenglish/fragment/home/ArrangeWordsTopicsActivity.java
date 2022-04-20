package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.TopicsRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.Exercise;
import com.myapp.learnenglish.fragment.home.model.Question;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.ArrayList;

public class ArrangeWordsTopicsActivity extends AppCompatActivity {
    private ArrayList<Topic> tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_topics);

        tempData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ArrayList<Exercise> exercises = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ArrayList<Question> questions = new ArrayList<>();
                questions.add(new Question(1, "test", "test"));
                questions.add(new Question(1, "test", "test"));
                questions.add(new Question(1, "test", "test"));
                exercises.add(new Exercise(j, "Test", questions));
            }
            tempData.add(new Topic(i, "Test", exercises));
        }

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_topic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUnits);
        TopicsRecyclerViewAdapter adapter = new TopicsRecyclerViewAdapter(this, tempData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}