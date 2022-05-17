package com.myapp.learnenglish.fragment.ranking;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.myapp.R;
import com.myapp.ThongTinTaikhoanActivity;

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
        //holder.email.setText(userRanking.getEmail());
        holder.rank.setText(Integer.toString(position+1));
        holder.email.setText(userRanking.getEmail());
        holder.hoTen.setText(userRanking.getHoTen());
        holder.point.setText(userRanking.getPoint()+"");
//        holder.iduser.setText(userRanking.getIduser()+"");

        LayImage(userRanking.getIduser(),holder.imView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView email,hoTen,point,sdt,rank,iduser;
        ImageView imView;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            rank = itemView.findViewById(R.id.tvRank);
            email = itemView.findViewById(R.id.tvemailrank);
            hoTen = itemView.findViewById(R.id.tvhotenrank);
            point = itemView.findViewById(R.id.tvpointrank);
            imView = itemView.findViewById(R.id.imagerank);

        }
    }

    public void LayImage(String idUser, ImageView imgview){
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("userimage/"+idUser).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context).load(uri).error(R.drawable.ic_avatar_default).into(imgview);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                //Toast.makeText(context, "Load image fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
