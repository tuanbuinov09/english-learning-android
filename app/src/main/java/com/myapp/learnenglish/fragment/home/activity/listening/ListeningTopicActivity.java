package com.myapp.learnenglish.fragment.home.activity.listening;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.arrangewords.TopicsRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.adapter.listening.RVListeningTopicAdapter;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Question;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Topic;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningExercise;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningQuestion;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningTopic;

import java.util.ArrayList;

public class ListeningTopicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_listening_topic);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_topic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<ListeningTopic> data) {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewTopics);
        RVListeningTopicAdapter adapter = new RVListeningTopicAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference("Listening").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserId = FirebaseAuth.getInstance().getUid();
                ArrayList<ListeningTopic> topics = new ArrayList<>();
                for (DataSnapshot topicsNode : snapshot.getChildren()) {
                    ArrayList<ListeningExercise> exercises = new ArrayList<>();
                    for (DataSnapshot exercisesNode : topicsNode.child("Exercises").getChildren()) {
                        ArrayList<ListeningQuestion> questions = new ArrayList<>();
                        for (DataSnapshot questionsNode : exercisesNode.child("Questions").getChildren()) {
                            ListeningQuestion question = new ListeningQuestion(questionsNode.child("content").getValue().toString());
                            questions.add(question);
                        }

                        // get the score of the current user
                        int score = 0;
                        if (exercisesNode.hasChild("Scores")) {
                            if (exercisesNode.child("Scores").hasChild(currentUserId)) {
                                score = Integer.parseInt(exercisesNode.child("Scores").child(currentUserId).getValue().toString());
                            }
                        }

                        ListeningExercise exercise = new ListeningExercise(exercisesNode.getKey(), score, questions);
                        exercises.add(exercise);
                    }
                    ListeningTopic topic = new ListeningTopic(topicsNode.getKey(), topicsNode.child("title").getValue().toString(), exercises);
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