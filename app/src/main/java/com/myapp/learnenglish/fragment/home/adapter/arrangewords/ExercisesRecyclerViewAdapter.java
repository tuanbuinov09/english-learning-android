package com.myapp.learnenglish.fragment.home.adapter.arrangewords;

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
import com.myapp.learnenglish.fragment.home.activity.arrangewords.ArrangeWordsActivity;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;

import java.util.ArrayList;

public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Exercise> data;
    private String path;

    public ExercisesRecyclerViewAdapter(Context context, ArrayList<Exercise> data, String path) {
        this.context = context;
        this.data = data;
        this.path = path;
    }

    @NonNull
    @Override
    public ExercisesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewExerciseTitle.setText(data.get(position).getKey());
        holder.textViewNumOfAchievedStars.setText(String.valueOf(data.get(position).getScore()));
        holder.textViewNumOfStars.setText(String.valueOf(data.get(position).getQuestions().size()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getBindingAdapterPosition();

                Intent intent = new Intent(context, ArrangeWordsActivity.class);
                intent.putExtra("questions", data.get(pos).getQuestions());
                intent.putExtra("path", path + "/Exercises/" + data.get(pos).getKey());
                intent.putExtra("exerciseIndex", pos);
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
