package com.myapp.learnenglish.fragment.achievement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.R;
import com.myapp.learnenglish.fragment.achievement.adapter.RVAchievementAdapter;
import com.myapp.learnenglish.fragment.achievement.model.Achievement;
import com.myapp.learnenglish.fragment.achievement.model.UserAchievement;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AchievementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AchievementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AchievementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AchievementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AchievementFragment newInstance(String param1, String param2) {
        AchievementFragment fragment = new AchievementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        loadData(view);

        return view;
    }

    private void loadData(View view) {
        FirebaseDatabase.getInstance().getReference("Achievements").orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Achievement> achievements = new ArrayList<>();
                        for (DataSnapshot achievement : snapshot.getChildren()) {
                            achievements.add(new Achievement(achievement.child("name").getValue().toString(),
                                    achievement.child("description").getValue().toString(),
                                    Integer.parseInt(achievement.child("requiredQuantity").getValue().toString()),
                                    achievement.child("path").getValue().toString()));
                        }

                        FirebaseDatabase.getInstance().getReference("Topics").orderByKey()
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        // structure: each topic has a list of exercises, each exercise has a list of questions
                                        int score = 0;
                                        int learned = 0;
                                        String currentUserId = FirebaseAuth.getInstance().getUid();
                                        for (DataSnapshot topicsNode : snapshot.getChildren()) {
                                            for (DataSnapshot exercisesNode : topicsNode.child("Exercises").getChildren()) {
                                                if (exercisesNode.hasChild("Scores")) {
                                                    if (exercisesNode.child("Scores").hasChild(currentUserId)) {
                                                        learned++;
                                                        score += Integer.parseInt(exercisesNode.child("Scores").child(currentUserId).getValue().toString());
                                                    }
                                                }
                                            }
                                        }
                                        UserAchievement userAchievement = new UserAchievement(learned, score);
                                        System.out.println(score + " " + learned);
                                        initRecyclerView(view, achievements, userAchievement);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void initRecyclerView(View view, ArrayList<Achievement> achievements, UserAchievement userAchievement) {
        RecyclerView recyclerView = view.findViewById(R.id.rvAchievements);
        RVAchievementAdapter adapter = new RVAchievementAdapter(getContext(), achievements, userAchievement);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}