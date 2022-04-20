package com.myapp.learnenglish.fragment.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.ArrangeWordsActivity;
import com.myapp.learnenglish.fragment.home.model.Exercise;

import java.util.ArrayList;

public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Exercise> data;

    public ExercisesRecyclerViewAdapter(Context context, ArrayList<Exercise> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ExercisesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewExerciseTitle.setText(data.get(position).getTitle());
        holder.textViewNumOfStars.setText(String.valueOf(data.get(position).getQuestions().size()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArrangeWordsActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExerciseTitle, textViewNumOfAchievedStars, textViewNumOfStars;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExerciseTitle = itemView.findViewById(R.id.textViewExerciseTitle);
            textViewNumOfAchievedStars = itemView.findViewById(R.id.textViewNumOfAchievedStars);
            textViewNumOfStars = itemView.findViewById(R.id.textViewNumOfStars);
            parentLayout = itemView.findViewById(R.id.exercisesParentLayout);
        }
    }
}
