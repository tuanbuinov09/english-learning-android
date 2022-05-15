package com.myapp.learnenglish.fragment.home.activity.listening;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.TestResultActivity;
import com.myapp.learnenglish.fragment.home.activity.arrangewords.ArrangeWordsActivity;
import com.myapp.learnenglish.fragment.home.activity.arrangewords.ArrangeWordsExercisesActivity;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningQuestion;

import java.util.ArrayList;
import java.util.Locale;

public class ListeningActivity extends AppCompatActivity {
    private ImageButton imageButtonClose, imageButtonSpeaker;
    private Button btnCheck;
    private TextView tvCurrentObtainedStars;
    private EditText etYourAnswer;
    private ProgressBar progressBar;
    private ArrayList<ListeningQuestion> questions;
    private int currentIndex = 0;
    private int obtainedStars = 0;
    private int percentPerQuestion;
    private String path;
    private int exerciseIndex;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_listening);

        setControl();
        setEvent();

        // get current path
        path = getIntent().getExtras().get("path").toString();
        System.out.println(path);

        exerciseIndex = Integer.parseInt(getIntent().getExtras().get("exerciseIndex").toString());
        questions = (ArrayList<ListeningQuestion>) getIntent().getSerializableExtra("questions");
        percentPerQuestion = 100 / questions.size();
        progressBar.setProgress(0);

        speak();
    }

    private void setEvent() {
        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etYourAnswer.getText().toString().isEmpty()) {
                    String[] words = questions.get(currentIndex).getContent().toLowerCase().split("\\s+");
                    String[] yourWords = etYourAnswer.getText().toString().toLowerCase().split("\\s+");
                    int maxMatchedPair = Math.max(words.length, yourWords.length);
                    int numMatchedPair = 0;
                    for (int i = 0; i < Math.min(words.length, yourWords.length); i++) {
                        int max = Math.max(words[i].length(), yourWords[i].length());
                        int num = 0;
                        for (int j = 0; j < Math.min(words[i].length(), yourWords[i].length()); j++) {
                            if (words[i].charAt(j) == yourWords[i].charAt(j)) {
                                num++;
                            }
                        }
                        int fraction = (int) ((num / (float) max) * 100);
                        System.out.println(fraction);
                        if (fraction >= 70) {
                            numMatchedPair++;
                        }
                    }
                    int fraction = (int) ((numMatchedPair / (float) maxMatchedPair) * 100);
                    System.out.println(fraction);
                    openBottomSheetDialog(fraction >= 70);
                }
            }
        });

        imageButtonSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });
    }

    private void setControl() {
        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageButtonSpeaker = findViewById(R.id.imageButtonSpeaker);
        btnCheck = findViewById(R.id.btnCheck);
        progressBar = findViewById(R.id.progressBar);
        tvCurrentObtainedStars = findViewById(R.id.tvCurrentObtainedStars);
        etYourAnswer = findViewById(R.id.etYourAnswer);
    }

    private void openBottomSheetDialog(boolean correct) {
        View view;
        if (correct) {
            view = getLayoutInflater().inflate(R.layout.tk_bottom_sheet_dialog_arrange_words_1, null);
            obtainedStars++;
            tvCurrentObtainedStars.setText(String.valueOf(obtainedStars));
        } else {
            view = getLayoutInflater().inflate(R.layout.tk_bottom_sheet_dialog_arrange_words_2, null);
            TextView tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswer);
            tvCorrectAnswer.setText(questions.get(currentIndex).getContent());
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);

        Button btn = view.findViewById(correct ? R.id.btnContinue : R.id.btnUnderstood);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(progressBar.getProgress() + percentPerQuestion);
                if (currentIndex < questions.size() - 1) {
                    currentIndex++;
                    etYourAnswer.setText("");
                    speak();
                } else {
                    // after having done all questions, save the result
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Listening" + "/" + path);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    databaseReference.child("Scores").child(mAuth.getUid()).setValue(obtainedStars);
                    ListeningExerciseActivity.exercises.get(exerciseIndex).setScore(obtainedStars);
                    ListeningExerciseActivity.exercisesRecyclerViewAdapter.notifyDataSetChanged();

                    // display result view
                    Intent intent = new Intent(ListeningActivity.this, TestResultActivity.class);
                    intent.putExtra("total", questions.size());
                    intent.putExtra("obtainedStars", obtainedStars);
                    startActivity(intent);
                    finish();
                }

                bottomSheetDialog.dismiss();
            }
        });
    }

    private void speak() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1.0f);
                    textToSpeech.speak(questions.get(currentIndex).getContent(), TextToSpeech.QUEUE_ADD, null, null);
                }
            }
        });
    }
}