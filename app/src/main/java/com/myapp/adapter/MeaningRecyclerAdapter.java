package com.myapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.R;
import com.myapp.model.Meaning;

import java.util.ArrayList;

public class MeaningRecyclerAdapter extends
        RecyclerView.Adapter<MeaningRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Meaning> meaningArrayList;
    public MeaningRecyclerAdapter(Context mContext, ArrayList<Meaning> meaningArrayList){
            this.mContext = mContext;
            this.meaningArrayList = meaningArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.meaning_item_for_recyclerview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Meaning meaning = meaningArrayList.get(viewHolder.getAbsoluteAdapterPosition());
        viewHolder.textViewPartOfSpeech.setText(meaning.getPartOfSpeechName().trim());
        Meaning prevMeaning=null;
        // nếu meaning này có partofspeech trùng với meaning trước thì khỏi show partofspeech
        if(position>=1){
            prevMeaning = meaningArrayList.get(viewHolder.getAbsoluteAdapterPosition()-1);
            if(prevMeaning.getPartOfSpeechName().equalsIgnoreCase(meaning.getPartOfSpeechName().trim())){
                viewHolder.textViewPartOfSpeech.setVisibility(View.GONE);
            }
        }

        viewHolder.textViewMeaning.setText(meaning.getMeaning().trim());

        ExampleDetailRecyclerAdapter exampleDetailRecyclerAdapter = new ExampleDetailRecyclerAdapter(mContext, meaning.getListExampleDetails());
        viewHolder.exampleRecyclerView.setAdapter(exampleDetailRecyclerAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        viewHolder.exampleRecyclerView.setLayoutManager(manager);
//        viewHolder.buttonWordMenu.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return meaningArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewPartOfSpeech;
        private TextView textViewMeaning;
    private RecyclerView exampleRecyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPartOfSpeech = (TextView) itemView.findViewById(R.id.textViewPartOfSpeech);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            textViewMeaning = (TextView) itemView.findViewById(R.id.textViewMeaning);
            exampleRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
        }
    }
}
