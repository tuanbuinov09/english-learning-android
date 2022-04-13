package com.myapp.learnenglish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myapp.R;
import com.myapp.learnenglish.fragment.AchievementFragment;
import com.myapp.learnenglish.fragment.HomeFragment;
import com.myapp.learnenglish.fragment.ProfileFragment;
import com.myapp.learnenglish.fragment.RankingFragment;

public class LearnEnglishActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_learn_english);
        
        setControl();
        setEvent();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
    }

    private void setEvent() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.pageHome:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.pageAchievement:
                    selectedFragment = new AchievementFragment();
                    break;
                case R.id.pageRanking:
                    selectedFragment = new RankingFragment();
                    break;
                case R.id.pageProfile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    selectedFragment).commit();
            return true;
        });
    }

    private void setControl() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
    }
}