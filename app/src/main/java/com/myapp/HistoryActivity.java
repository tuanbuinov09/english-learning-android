package com.myapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.adapter.EnWordHistoryAdapter;
import com.myapp.adapter.TranslationHistoryAdapter;
import com.myapp.sqlite.DatabaseHelper;
import com.myapp.sqlite.dao.TranslationHistoryDao;
import com.myapp.utils.FileIO2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvEmpty;
    List<Integer> wordsList = null;

    DatabaseHelper databaseHelper;
    TranslationHistoryDao translationHistoryDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setControl();

        setTitle("Từ đã tra");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);

        databaseHelper = DatabaseHelper.getInstance(this);
        translationHistoryDao = new TranslationHistoryDao(databaseHelper);

        File path = getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_HISTORY_NAME);

        //CREATE SETTINGS FILE
        if (!file.exists()) {
            FileIO2.writeToFile(new ArrayList<Integer>(), getApplicationContext());
        }

        wordsList = FileIO2.readFromFile(this);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new EnWordHistoryAdapter(this, wordsList, tvEmpty);
        recyclerView.setAdapter(recyclerViewAdapter);

        //getSupportActionBar().show();
    }

    private void setControl() {
        recyclerView = findViewById(R.id.recyclerView);
        tvEmpty = findViewById(R.id.tvEmpty);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
