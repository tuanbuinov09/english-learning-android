package com.myapp.learnenglish.fragment.ranking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.myapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdapterRank extends RecyclerView.Adapter<AdapterRank.MyViewHolder> {
    Context context;
    ArrayList<UserRanking> list;

    public AdapterRank(Context context, ArrayList<UserRanking> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_xephang,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        UserRanking userRanking = list.get(position);
        holder.email.setText(userRanking.getEmail());
        holder.hoTen.setText(userRanking.getHoTen());
        holder.point.setText(userRanking.getPoint()+"");


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView email,hoTen,point,sdt;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.tvemailrank);
            hoTen = itemView.findViewById(R.id.tvhotenrank);
            point = itemView.findViewById(R.id.tvpointrank);


        }
    }
}
