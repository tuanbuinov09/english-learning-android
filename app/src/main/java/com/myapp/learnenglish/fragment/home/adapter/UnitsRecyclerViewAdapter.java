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

import java.util.ArrayList;

public class UnitsRecyclerViewAdapter extends RecyclerView.Adapter<UnitsRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data;

    // data is passed into the constructor
    public UnitsRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public UnitsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_arrange_words_units_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(@NonNull UnitsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewTitle.setText(data.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArrangeWordsExercisesActivity.class);
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
        TextView textViewUnit, textViewTitle;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUnit = itemView.findViewById(R.id.textViewUnit);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            parentLayout = itemView.findViewById(R.id.unitsParentLayout);
        }
    }
}
