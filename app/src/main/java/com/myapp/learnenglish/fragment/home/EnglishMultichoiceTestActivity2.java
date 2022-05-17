package com.myapp.learnenglish.fragment.home;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.model.multichoice.QuestionML;

import java.util.ArrayList;

public class EnglishMultichoiceTestActivity2 extends AppCompatActivity {
    private Button choice1, choice2, choice3, choice4, nextML;
    private ImageButton imageButtonCloseML;
    private TextView tvCurrentObtainedStarsML, questionML;
    private ProgressBar progressBarML;
    private int currentIndex = 0;
    private int obtainedStars = 0;
    private ArrayList<QuestionML> questions;
    private int percentPerQuestion;
    private String path;
    private int exerciseIndex;
    private String yourAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.english_multichoice_test_2);

        setControl();
        setEvent();


        // get current path
        path = getIntent().getExtras().get("path").toString();
        System.out.println(path);

        exerciseIndex = Integer.parseInt(getIntent().getExtras().get("exerciseIndex").toString());
        questions = (ArrayList<QuestionML>) getIntent().getSerializableExtra("questions");
        percentPerQuestion = 100 / questions.size();
        progressBarML.setProgress(0);

        loadNextQuestion(questions.get(currentIndex));
    }

    private void setEvent() {
        imageButtonCloseML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourAnswer = "A";
                choice2.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice4.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice1.setBackgroundResource(R.drawable.bg_steelblue_radius_15);
            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourAnswer = "B";
                choice1.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice4.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice2.setBackgroundResource(R.drawable.bg_steelblue_radius_15);
            }
        });
        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourAnswer = "C";
                choice2.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice1.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice4.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_steelblue_radius_15);
            }
        });
        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourAnswer = "D";
                choice2.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice1.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_steelblue_radius_15);
            }
        });

        nextML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reset
                choice2.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice3.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice1.setBackgroundResource(R.drawable.bg_green_radius_15);
                choice4.setBackgroundResource(R.drawable.bg_green_radius_15);

                System.out.println(questions.get(currentIndex).getAnswer());
                openBottomSheetDialog(yourAnswer.equals(questions.get(currentIndex).getAnswer()));
            }
        });

    }

    private void openBottomSheetDialog(boolean correct) {
        View view;
        if (correct) {
            view = getLayoutInflater().inflate(R.layout.tk_bottom_sheet_dialog_arrange_words_1, null);
            obtainedStars++;
            tvCurrentObtainedStarsML.setText(String.valueOf(obtainedStars));
        } else {
            view = getLayoutInflater().inflate(R.layout.tk_bottom_sheet_dialog_arrange_words_2, null);
            TextView tvCorrectAnswer = view.findViewById(R.id.tvCorrectAnswer);
            tvCorrectAnswer.setText(questions.get(currentIndex).getAnswer());
        }

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(false);

        Button btn = view.findViewById(correct ? R.id.btnContinue : R.id.btnUnderstood);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarML.setProgress(progressBarML.getProgress() + percentPerQuestion);
                if (currentIndex < questions.size() - 1) {
                    loadNextQuestion(questions.get(++currentIndex));
                } else {
                    // after having done all questions, save the result
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Multichoice" + "/" + path);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    databaseReference.child("Scores").child(mAuth.getUid()).setValue(obtainedStars);
                    EnglishMultichoiceTestActivity1.exercises.get(exerciseIndex).setScore(obtainedStars);
                    EnglishMultichoiceTestActivity1.englishML1RecyclerViewAdapter.notifyDataSetChanged();

                    // display result view
                    Intent intent = new Intent(EnglishMultichoiceTestActivity2.this, TestResultActivity.class);
                    intent.putExtra("total", questions.size());
                    intent.putExtra("obtainedStars", obtainedStars);
                    startActivity(intent);
                    finish();
                }

                yourAnswer = "";
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void setControl() {
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        choice4 = findViewById(R.id.choice4);
        imageButtonCloseML =findViewById(R.id.imageButtonCloseML);
        questionML = findViewById(R.id.questionML);
        nextML = findViewById(R.id.nextML);
        progressBarML = findViewById(R.id.progressBarML);
        tvCurrentObtainedStarsML = findViewById(R.id.tvCurrentObtainedStarsML);
    }

    private void loadNextQuestion(QuestionML question) {
        questionML.setText(question.getContent());
        choice1.setText(question.getA());
        choice2.setText(question.getB());
        choice3.setText(question.getC());
        choice4.setText(question.getD());

    }



//    private QuestionLibrary mQuestionLibrary = new QuestionLibrary();
//
//    private TextView mScoreView;
//    private TextView mQuestionView;
//    private Button mButtonChoice1;
//    private Button mButtonChoice2;
//    private Button mButtonChoice3;
//
//    private String mAnswer;
//    private int mScore = 0;
//    private int mQuestionNumber = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.english_multichoice_test_2);
//
//        mScoreView = (TextView)findViewById(com.myapp.R.id.score);
//        mQuestionView = (TextView)findViewById(com.myapp.R.id.question);
//        mButtonChoice1 = (Button)findViewById(com.myapp.R.id.choice1);
//        mButtonChoice2 = (Button)findViewById(com.myapp.R.id.choice2);
//        mButtonChoice3 = (Button)findViewById(com.myapp.R.id.choice3);
//
//        updateQuestion();
//
//        //Start of Button Listener for Button1
//        mButtonChoice1.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //My logic for Button goes in here
//
//                if (mButtonChoice1.getText() == mAnswer){
//                    mScore = mScore + 1;
//                    updateScore(mScore);
//                    updateQuestion();
//                    //This line of code is optiona
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "correct", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "wrong", Toast.LENGTH_SHORT).show();
//                    updateQuestion();
//                }
//            }
//        });
//
//        //End of Button Listener for Button1
//
//        //Start of Button Listener for Button2
//        mButtonChoice2.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //My logic for Button goes in here
//
//                if (mButtonChoice2.getText() == mAnswer){
//                    mScore = mScore + 1;
//                    updateScore(mScore);
//                    updateQuestion();
//                    //This line of code is optiona
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "correct", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "wrong", Toast.LENGTH_SHORT).show();
//                    updateQuestion();
//                }
//            }
//        });
//
//        //End of Button Listener for Button2
//
//
//        //Start of Button Listener for Button3
//        mButtonChoice3.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                //My logic for Button goes in here
//
//                if (mButtonChoice3.getText() == mAnswer){
//                    mScore = mScore + 1;
//                    updateScore(mScore);
//                    updateQuestion();
//                    //This line of code is optiona
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "correct", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(EnglishMultichoiceTestActivity2.this, "wrong", Toast.LENGTH_SHORT).show();
//                    updateQuestion();
//                }
//            }
//        });
//
//        //End of Button Listener for Button3
//
//
//
//
//
//    }
//
//    private void updateQuestion(){
//        mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
//        mButtonChoice1.setText(mQuestionLibrary.getChoice1(mQuestionNumber));
//        mButtonChoice2.setText(mQuestionLibrary.getChoice2(mQuestionNumber));
//        mButtonChoice3.setText(mQuestionLibrary.getChoice3(mQuestionNumber));
//
//        mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
//        mQuestionNumber++;
//    }
//
//
//    private void updateScore(int point) {
//        mScoreView.setText("" + mScore);
//    }


}

