package com.myapp.learnenglish.fragment.home;

import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.EnglishMLRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.multichoice.ExerciseML;
import com.myapp.learnenglish.fragment.home.model.multichoice.QuestionML;
import com.myapp.learnenglish.fragment.home.model.multichoice.TopicML;

import java.util.ArrayList;

public class EnglishMultichoiceTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.english_multichoice_test);

        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_topic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView(ArrayList<TopicML> data) {
        RecyclerView recyclerView = findViewById(R.id.EnglishT);
        EnglishMLRecyclerViewAdapter adapter = new EnglishMLRecyclerViewAdapter(this, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadData() {
        FirebaseDatabase.getInstance().getReference("Multichoice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // structure: each topic has a list of exercises, each exercise has a list of questions
                ArrayList<TopicML> topics = new ArrayList<>();
                for (DataSnapshot topicsNode : snapshot.getChildren()) {
                    ArrayList<ExerciseML> exercises = new ArrayList<>();
                    for (DataSnapshot exercisesNode : topicsNode.child("Exercises").getChildren()) {
                        ArrayList<QuestionML> questions = new ArrayList<>();
                        for (DataSnapshot questionsNode : exercisesNode.child("Questions").getChildren()) {
                            QuestionML question = new QuestionML(questionsNode.getKey(),
                                    questionsNode.child("A").getValue().toString(),
                                    questionsNode.child("B").getValue().toString(),
                                    questionsNode.child("C").getValue().toString(),
                                    questionsNode.child("D").getValue().toString(),
                                    questionsNode.child("content").getValue().toString(),
                                    questionsNode.child("answer").getValue().toString());

                            questions.add(question);
                        }

                        // get the score of the current user
                        int score = 0;
                        String currentUserId = FirebaseAuth.getInstance().getUid();
                        if (exercisesNode.hasChild("Scores")) {
                            if (exercisesNode.child("Scores").hasChild(currentUserId)) {
                                score = Integer.parseInt(exercisesNode.child("Scores").child(currentUserId).getValue().toString());
                            }
                        }

                        ExerciseML exercise = new ExerciseML(exercisesNode.getKey(), score, questions);
                        exercises.add(exercise);
                    }
                    TopicML topic = new TopicML(topicsNode.getKey(), exercises);//topicsNode.child("title").getValue().toString()
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