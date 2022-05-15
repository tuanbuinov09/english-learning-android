package com.myapp.learnenglish.fragment.home.adapter.listening;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.activity.arrangewords.ArrangeWordsExercisesActivity;
import com.myapp.learnenglish.fragment.home.activity.listening.ListeningExerciseActivity;
import com.myapp.learnenglish.fragment.home.activity.listening.ListeningTopicActivity;
import com.myapp.learnenglish.fragment.home.adapter.arrangewords.TopicsRecyclerViewAdapter;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Exercise;
import com.myapp.learnenglish.fragment.home.model.arrangewords.Topic;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningExercise;
import com.myapp.learnenglish.fragment.home.model.listening.ListeningTopic;

import java.util.ArrayList;

public class RVListeningTopicAdapter extends RecyclerView.Adapter<RVListeningTopicAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ListeningTopic> data;

    public RVListeningTopicAdapter(Context context, ArrayList<ListeningTopic> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_topic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        addButtonsToLLButtonGroup(data.get(position).getExercises(), holder.llButtonGroup);
        holder.textViewTopicTitle.setText(data.get(position).getTitle());
        holder.tvTopic.setText(data.get(position).getKey());
        holder.textViewNumOfStars.setText(String.valueOf(getTotalStars(data.get(position))));
        holder.textViewNumOfAchievedStars.setText(String.valueOf(getNumOfAchievedStars(data.get(position))));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getBindingAdapterPosition();
                Intent intent = new Intent(context, ListeningExerciseActivity.class);
                intent.putExtra("title", data.get(pos).getKey() + ": " + data.get(pos).getTitle());
                intent.putExtra("exercises", data.get(pos).getExercises());
                intent.putExtra("path", data.get(pos).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTopicTitle, textViewNumOfAchievedStars, textViewNumOfStars, tvTopic;
        LinearLayout parentLayout, llButtonGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTopicTitle = itemView.findViewById(R.id.textViewTopicTitle);
            textViewNumOfAchievedStars = itemView.findViewById(R.id.textViewNumOfAchievedStars);
            textViewNumOfStars = itemView.findViewById(R.id.textViewNumOfStars);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            parentLayout = itemView.findViewById(R.id.unitsParentLayout);
            llButtonGroup = itemView.findViewById(R.id.llButtonGroup);
        }
    }

    private int getTotalStars(ListeningTopic listeningTopic) {
        int numOfStars = 0;
        for (int i = 0; i < listeningTopic.getExercises().size(); i++) {
            numOfStars += listeningTopic.getExercises().get(i).getQuestions().size();
        }

        return numOfStars;
    }

    private int getNumOfAchievedStars(ListeningTopic listeningTopic) {
        int res = 0;
        for (ListeningExercise exercise : listeningTopic.getExercises()) {
            res += exercise.getScore();
        }
        return res;
    }

    private void addButtonsToLLButtonGroup(ArrayList<ListeningExercise> exercises, LinearLayout llButtonGroup) {
        for (int i = 0; i < exercises.size(); i++) {
            Button button = new Button(context);
            button.setText(String.valueOf(i + 1));
            setButtonAttributes(button);
            llButtonGroup.addView(button);
        }
    }

    private void setButtonAttributes(Button button) {
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMarginEnd(5);
        button.setLayoutParams(layoutParams);
        button.setBackgroundResource(R.drawable.tk_circle_button);
        button.setTextColor(context.getResources().getColor(R.color.teal_200, null));
//        button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.teal_200, null)));
    }
}
