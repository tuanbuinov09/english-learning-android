package com.myapp.learnenglish.fragment.achievement.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.myapp.R;
import com.myapp.learnenglish.fragment.achievement.model.Achievement;
import com.myapp.learnenglish.fragment.achievement.model.UserAchievement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RVAchievementAdapter extends RecyclerView.Adapter<RVAchievementAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Achievement> achievements;
    private UserAchievement userAchievement;

    public RVAchievementAdapter(Context context, ArrayList<Achievement> achievements, UserAchievement userAchievement) {
        this.context = context;
        this.achievements = achievements;
        this.userAchievement = userAchievement;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tk_achievement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(achievements.get(position).getName());
        holder.tvDescription.setText(achievements.get(position).getDescription());
        holder.tvRequiredQuantity.setText(String.valueOf(achievements.get(position).getRequiredQuantity()));
        try {
            File localFile = File.createTempFile("temp", "png");
            localFile.deleteOnExit();
            FirebaseStorage.getInstance().getReference(achievements.get(position).getPath())
                    .getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, context.getResources().getDisplayMetrics());
                    holder.ivBadge.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleAchievement(holder, achievements.get(position).getName(), achievements.get(position).getRequiredQuantity());
    }

    private void handleAchievement(ViewHolder holder, String name, int required) {
        switch (name) {
            case "Bài học đầu tiên":
                if (userAchievement.getLearned() >= required) {
                    holder.progressBar.setProgress(100);
                    holder.tvProgress.setText(String.valueOf(required));
                    holder.mcvAchievement.setCardBackgroundColor(context.getResources().getColor(R.color.tk_cardView, null));
                } else {
                    holder.tvProgress.setText(String.valueOf(userAchievement.getLearned()));
                    holder.progressBar.setProgress((int) ((userAchievement.getLearned() / (float) required) * 100));
                }
                break;
            case "Thợ săn sao":
            case "Cao thủ săn sao":
                if (userAchievement.getScore() >= required) {
                    holder.progressBar.setProgress(100);
                    holder.tvProgress.setText(String.valueOf(required));
                    holder.mcvAchievement.setCardBackgroundColor(Color.parseColor("#8EFC86"));
                } else {
                    holder.tvProgress.setText(String.valueOf(userAchievement.getScore()));
                    holder.progressBar.setProgress((int) ((userAchievement.getScore() / (float) required) * 100));
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvProgress, tvRequiredQuantity;
        ProgressBar progressBar;
        ImageView ivBadge;
        MaterialCardView mcvAchievement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(com.myapp.R.id.tvName);
            tvDescription = itemView.findViewById(com.myapp.R.id.tvDescription);
            tvProgress = itemView.findViewById(com.myapp.R.id.tvProgress);
            tvRequiredQuantity = itemView.findViewById(com.myapp.R.id.tvRequiredQuantity);
            progressBar = itemView.findViewById(com.myapp.R.id.progressBar);
            ivBadge = itemView.findViewById(com.myapp.R.id.ivBadge);
            mcvAchievement = itemView.findViewById(com.myapp.R.id.mcvAchievement);
        }
    }
}
