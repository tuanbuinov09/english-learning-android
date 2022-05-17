package com.myapp.learnenglish.fragment.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.myapp.Main;
import com.myapp.R;

import java.util.ArrayList;
import java.util.Collections;

public class RankingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterRank adapterRank;
    TextView tvRank, tvTest;
    TextView tvPointrank1,tvPointrank2,tvPointrank3;
    ImageView imgback;
    Query database;
    ArrayList<UserRanking> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        AnhXa();
        database = FirebaseDatabase.getInstance().getReference("User").orderByChild("point");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list= new ArrayList<>();
        adapterRank = new AdapterRank(this,list);
        recyclerView.setAdapter(adapterRank);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserRanking userRanking = dataSnapshot.getValue(UserRanking.class);
                    list.add(userRanking);
                }
                //dao nguoc list
                Collections.reverse(list);
                Top3point();
                adapterRank.notifyDataSetChanged();
            }

                      @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //Back
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(RankingActivity.this,
                        Main.class);
                startActivity(intent);
            }
        });




    }
    private void AnhXa()
    {
        recyclerView = findViewById(R.id.userranklist);
        tvPointrank1 = (TextView) findViewById(R.id.tvpointrank1);
        tvPointrank2 = (TextView) findViewById(R.id.tvpointrank2);
        tvPointrank3 = (TextView) findViewById(R.id.tvpointrank3);
        tvRank =(TextView) findViewById(R.id.tvRank);
        imgback = (ImageView) findViewById(R.id.imgVBackRank);

    }
    private void Top3point()
    {
        tvPointrank1.setText(String.valueOf(list.get(0).getPoint()));
        tvPointrank2.setText(String.valueOf(list.get(1).getPoint()));
        tvPointrank3.setText(String.valueOf(list.get(2).getPoint()));


    }

}