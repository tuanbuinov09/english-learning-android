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

public class EnglishML1RecyclerViewAdapter extends RecyclerView.Adapter<EnglishML1RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data;

    public EnglishML1RecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public EnglishML1RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.english_multichoice_test_item_1, parent, false);
        return new EnglishML1RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnglishML1RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.EL1textview.setText(data.get(position));
        holder.El1parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnglishMultichoiceTestActivity2.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout El1parent;
        TextView EL1textview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            El1parent = itemView.findViewById(R.id.EL1ParentLayout);
            EL1textview = itemView.findViewById(R.id.textViewEL1);
        }
    }
}