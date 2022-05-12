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
import com.myapp.learnenglish.fragment.home.ArrangeWordsExercisesActivity;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.ArrayList;

public class TopicsRecyclerViewAdapter extends RecyclerView.Adapter<TopicsRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Topic> data;

    // data is passed into the constructor
    public  TopicsRecyclerViewAdapter(Context context, ArrayList<Topic> data) {
        this.context = context;
        this.data = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public TopicsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_topic_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull TopicsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewTopicTitle.setText(data.get(position).getTitle());
        holder.tvTopic.setText(data.get(position).getKey());
        holder.textViewNumOfStars.setText(String.valueOf(getTotalStars(data.get(position))));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getBindingAdapterPosition();
                Intent intent = new Intent(context, ArrangeWordsExercisesActivity.class);
                intent.putExtra("title", data.get(pos).getKey() + ": " + data.get(pos).getTitle());
                intent.putExtra("exercises", data.get(pos).getExercises());
                context.startActivity(intent);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return data.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTopicTitle, textViewNumOfAchievedStars, textViewNumOfStars, tvTopic;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTopicTitle = itemView.findViewById(R.id.textViewTopicTitle);
            textViewNumOfAchievedStars = itemView.findViewById(R.id.textViewNumOfAchievedStars);
            textViewNumOfStars = itemView.findViewById(R.id.textViewNumOfStars);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            parentLayout = itemView.findViewById(R.id.unitsParentLayout);
        }
    }

    private int getTotalStars(Topic topic) {
        int numOfStars = 0;
        for (int i = 0; i < topic.getExercises().size(); i++) {
            numOfStars += topic.getExercises().get(i).getQuestions().size();
        }

        return numOfStars;
    }
}
