package com.myapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.adapter.TranslationHistoryAdapter;
import com.myapp.model.TranslationHistory;

import java.util.ArrayList;
import java.util.List;

public class TranslationHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation_history);

        recyclerView = findViewById(R.id.recyclerView);

        List<TranslationHistory> historyList = new ArrayList<>();
        historyList.add(new TranslationHistory("1", "1"));
        historyList.add(new TranslationHistory("2", "2"));
        historyList.add(new TranslationHistory("3", "3"));
        historyList.add(new TranslationHistory("4", "4"));

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new TranslationHistoryAdapter(historyList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        //getSupportActionBar().show();
    }
}
