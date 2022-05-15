package com.myapp.learnenglish.fragment.home.adapter.listening;

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
import com.myapp.learnenglish.fragment.home.activity.listening.ListeningActivity;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningExercise;

import java.util.ArrayList;

public class RVListeningExerciseAdapter extends RecyclerView.Adapter<RVListeningExerciseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ListeningExercise> data;
    private String path;

    public RVListeningExerciseAdapter(Context context, ArrayList<ListeningExercise> data, String path) {
        this.context = context;
        this.data = data;
        this.path = path;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_exercise_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewExerciseTitle.setText(data.get(position).getKey());
        holder.textViewNumOfAchievedStars.setText(String.valueOf(data.get(position).getScore()));
        holder.textViewNumOfStars.setText(String.valueOf(data.get(position).getQuestions().size()));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getBindingAdapterPosition();

                Intent intent = new Intent(context, ListeningActivity.class);
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
