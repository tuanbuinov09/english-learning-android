package com.myapp.learnenglish.fragment.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.TopicsRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.dao.TopicDao;
import com.myapp.learnenglish.fragment.home.model.Exercise;
import com.myapp.learnenglish.fragment.home.model.Question;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrangeWordsTopicsActivity extends AppCompatActivity {
    private TopicDao topicDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_topics);

        topicDao = new TopicDao();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_topic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<Topic> data) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUnits);
        TopicsRecyclerViewAdapter adapter = new TopicsRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        topicDao.get().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Topic> topics = new ArrayList<>();
                for (DataSnapshot topicsNode : snapshot.getChildren()) {
                    ArrayList<Exercise> exercises = new ArrayList<>();
                    for (DataSnapshot exercisesNode : topicsNode.child("Exercises").getChildren()) {
                        ArrayList<Question> questions = new ArrayList<>();
                        for (DataSnapshot questionsNode : exercisesNode.child("Questions").getChildren()) {
                            Question question = new Question(questionsNode.getKey(),
                                    questionsNode.child("content").getValue().toString(),
                                    questionsNode.child("answer").getValue().toString());
                            questions.add(question);
                        }
                        Exercise exercise = new Exercise(exercisesNode.getKey(), questions);
                        exercises.add(exercise);
                    }
                    Topic topic = new Topic(topicsNode.getKey(), topicsNode.child("title").getValue().toString(), exercises);
                    topics.add(topic);
                }
                initRecyclerView(topics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}