package com.myapp.learnenglish.fragment.home.activity.arrangewords;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.R;
import com.myapp.learnenglish.fragment.home.TestResultActivity;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ArrangeWordsActivity extends AppCompatActivity {
    private FlexboxLayout flexboxLayoutYourAnswerSection, flexboxLayoutGivenWordsSection;
    private ImageButton imageButtonClose;
    private Button btnCheck;
    private TextView tvContent, tvCurrentObtainedStars;
    private ProgressBar progressBar;
    private ArrayList<Question> questions;
    private int currentIndex = 0;
    private int obtainedStars = 0;
    private ObservableArrayList<Button> yourAnswer;
    private int percentPerQuestion;
    private String path;
    private int exerciseIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words);
        
        setControl();
        setEvent();

        yourAnswer = new ObservableArrayList<>();
        // add a listener to yourAnswer to check if there is any element in it
        // disable the check button if there isn't any, otherwise, enable it
        yourAnswer.addOnListChangedCallback(new ObservableList.OnListChangedCallback() {
            @Override
            public void onChanged(ObservableList sender) {
            }

            @Override
            public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            }

            @Override
            public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                btnCheck.setEnabled(yourAnswer.size() != 0);
            }

            @Override
            public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            }

            @Override
            public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                btnCheck.setEnabled(yourAnswer.size() != 0);
            }
        });

        // get current path
        path = getIntent().getExtras().get("path").toString();
        System.out.println(path);

        exerciseIndex = Integer.parseInt(getIntent().getExtras().get("exerciseIndex").toString());
        questions = (ArrayList<Question>) getIntent().getSerializableExtra("questions");
        percentPerQuestion = 100 / questions.size();
        progressBar.setProgress(0);

        loadNextQuestion(questions.get(currentIndex));
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
                ArrayList<String> temp = new ArrayList<>();
                for (Button button : yourAnswer) {
                    temp.add(button.getText().toString());
                }
                String answer = String.join(" ", temp);
                openBottomSheetDialog(answer.equals(questions.get(currentIndex).getAnswer()));
            }
        });
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
                progressBar.setProgress(progressBar.getProgress() + percentPerQuestion);
                if (currentIndex < questions.size() - 1) {
                    loadNextQuestion(questions.get(++currentIndex));
                } else {
                    // after having done all questions, save the result
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Topics" + "/" + path);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    databaseReference.child("Scores").child(mAuth.getUid()).setValue(obtainedStars);
                    ArrangeWordsExercisesActivity.exercises.get(exerciseIndex).setScore(obtainedStars);
                    ArrangeWordsExercisesActivity.exercisesRecyclerViewAdapter.notifyDataSetChanged();

                    // display result view
                    Intent intent = new Intent(ArrangeWordsActivity.this, TestResultActivity.class);
                    intent.putExtra("total", questions.size());
                    intent.putExtra("obtainedStars", obtainedStars);
                    startActivity(intent);
                    finish();
                }

                bottomSheetDialog.dismiss();
            }
        });
    }

    private void setControl() {
        flexboxLayoutYourAnswerSection = findViewById(com.myapp.R.id.flexboxLayoutYourAnswerSection);
        flexboxLayoutGivenWordsSection = findViewById(com.myapp.R.id.flexboxLayoutGivenWordsSection);
        imageButtonClose = findViewById(R.id.imageButtonClose);
        btnCheck = findViewById(R.id.btnCheck);
        tvContent = findViewById(R.id.tvContent);
        progressBar = findViewById(R.id.progressBar);
        tvCurrentObtainedStars = findViewById(R.id.tvCurrentObtainedStars);
    }

    private void loadNextQuestion(Question question) {
        // reset
        yourAnswer.clear();
        flexboxLayoutYourAnswerSection.removeAllViews();
        flexboxLayoutGivenWordsSection.removeAllViews();

        tvContent.setText(question.getContent());
        ArrayList<String> words = new ArrayList<>(Arrays.asList(question.getAnswer().split(" ")));
        Collections.shuffle(words);
        addButtonsToChooseSection(words);
    }

    private void addButtonsToChooseSection(ArrayList<String> words) {
        for (int i = 0; i < words.size(); i++) {
            Button button = new Button(this);
            setButtonAttributes(button);
            button.setText(words.get(i));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setEnabled(false);

                    // add a duplicate button to the answer section when a button is clicked
                    Button duplicate = new Button(ArrangeWordsActivity.this);
                    yourAnswer.add(duplicate);
                    setButtonAttributes(duplicate);
                    duplicate.setText(button.getText());
                    duplicate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            button.setEnabled(true);
                            yourAnswer.remove(duplicate);

                            flexboxLayoutYourAnswerSection.removeView(duplicate);
                        }
                    });
                    flexboxLayoutYourAnswerSection.addView(duplicate);
                }
            });

            flexboxLayoutGivenWordsSection.addView(button);
        }
    }

    private void setButtonAttributes(Button button) {
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 15, 15);
        button.setLayoutParams(layoutParams);
        button.setMinWidth(0);
        button.setMinimumWidth(0);
        button.setAllCaps(false);
        button.setTextSize(20);
        button.setTypeface(null, Typeface.NORMAL);
        button.setBackgroundResource(com.myapp.R.drawable.tk_button);
    }
}