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
import com.myapp.learnenglish.fragment.home.EnglishMultichoiceTestActivity1;
import com.myapp.learnenglish.fragment.home.model.multichoice.TopicML;
import com.myapp.learnenglish.fragment.home.model.multichoice.ExerciseML;

import java.util.ArrayList;

public class EnglishMLRecyclerViewAdapter extends RecyclerView.Adapter<EnglishMLRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<TopicML> data;

    public EnglishMLRecyclerViewAdapter(Context context, ArrayList<TopicML> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EnglishMLRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.english_multichoice_test_item, parent, false);
        return new EnglishMLRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnglishMLRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.ELtextview.setText(data.get(position).getKey());
        holder.textViewNumOfStarsML.setText(String.valueOf(getTotalStars(data.get(position))));
        holder.textViewNumOfAchievedStarsML.setText(String.valueOf(getNumOfAchievedStars(data.get(position))));
        holder.Elparent.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnglishMultichoiceTestActivity1.class);
                int pos = holder.getBindingAdapterPosition();
                intent.putExtra("title", data.get(pos).getKey() + ": " ); //data.get(pos).getTitle());
                intent.putExtra("exercises", data.get(pos).getExercises());
                intent.putExtra("path", data.get(pos).getKey());
                context.startActivity(intent);
            }
        });
    }
    private int getTotalStars(TopicML topic) {
        int numOfStars = 0;
        for (int i = 0; i < topic.getExercises().size(); i++) {
            numOfStars += topic.getExercises().get(i).getQuestions().size();
        }

        return numOfStars;
    }

    private int getNumOfAchievedStars(TopicML topic) {
        int res = 0;
        for (ExerciseML exercise : topic.getExercises()) {
            res += exercise.getScore();
        }
        return res;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Elparent;
        TextView ELtextview, textViewNumOfAchievedStarsML, textViewNumOfStarsML;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Elparent = itemView.findViewById(R.id.ELParentLayout);
            ELtextview = itemView.findViewById(R.id.textViewEL);
            textViewNumOfAchievedStarsML = itemView.findViewById(R.id.textViewNumOfAchievedStarsML);
            textViewNumOfStarsML = itemView.findViewById(R.id.textViewNumOfStarsML);
        }
    }
}
