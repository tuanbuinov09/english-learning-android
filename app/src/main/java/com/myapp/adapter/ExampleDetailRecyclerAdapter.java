package com.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;

import java.util.ArrayList;

public class ExampleDetailRecyclerAdapter extends
        RecyclerView.Adapter<ExampleDetailRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ExampleDetail> exampleDetailArrayList;
    public ExampleDetailRecyclerAdapter(Context mContext, ArrayList<ExampleDetail> exampleDetailArrayList){
            this.mContext = mContext;
            this.exampleDetailArrayList = exampleDetailArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.example_detail_item_for_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ExampleDetail exampleDetail = exampleDetailArrayList.get(viewHolder.getAbsoluteAdapterPosition());
        viewHolder.textViewExample.setText(exampleDetail.getExample());
        viewHolder.textViewExampleMeaning.setText(exampleDetail.getExampleMeaning());

    }

    @Override
    public int getItemCount() {
        return exampleDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewExample;
        private TextView textViewExampleMeaning;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewExample = (TextView) itemView.findViewById(R.id.textViewExample);
            textViewExampleMeaning = (TextView) itemView.findViewById(R.id.textViewExampleMeaning);
        }
    }
}
