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
import com.myapp.learnenglish.fragment.home.EnglishMultichoiceTestActivity;
import com.myapp.learnenglish.fragment.home.EnglishMultichoiceTestActivity1;
import com.myapp.learnenglish.fragment.home.EnglishMultichoiceTestActivity2;
import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.ArrayList;

public class EnglishMLRecyclerViewAdapter extends RecyclerView.Adapter<EnglishMLRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data;

    public EnglishMLRecyclerViewAdapter(Context context, ArrayList<String> data) {
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
        holder.ELtextview.setText(data.get(position));
        holder.Elparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnglishMultichoiceTestActivity1.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout Elparent;
        TextView ELtextview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Elparent = itemView.findViewById(R.id.ELParentLayout);
            ELtextview = itemView.findViewById(R.id.textViewEL);
        }
    }
}
