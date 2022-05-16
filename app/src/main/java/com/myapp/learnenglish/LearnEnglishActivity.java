package com.myapp.learnenglish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.myapp.ProfileActivity;
import com.myapp.R;
import com.myapp.ThongTinTaikhoanActivity;
import com.myapp.learnenglish.fragment.achievement.AchievementFragment;
import com.myapp.learnenglish.fragment.home.HomeFragment;
import com.myapp.learnenglish.fragment.profile.ProfileFragment;
import com.myapp.learnenglish.fragment.ranking.RankingActivity;
import com.myapp.learnenglish.fragment.ranking.RankingFragment;

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
//                    Intent intentpageRanking = new Intent(this, RankingActivity.class);
//                    startActivity(intentpageRanking);
//                    return true;
                    break;
                case R.id.pageProfile:
                    selectedFragment = new ProfileFragment();
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    return true;

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