package com.myapp.learnenglish.fragment.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import com.myapp.R;
import com.myapp.learnenglish.fragment.home.adapter.UnitsRecyclerViewAdapter;

import java.util.ArrayList;

public class ArrangeWordsUnitsActivity extends AppCompatActivity {
    private ArrayList<String> tempData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_arrange_words_units);

        tempData = new ArrayList<>();
        tempData.add("asdf");
        tempData.add("234sdaf");
        tempData.add("asd324f");
        tempData.add("sadf");
        tempData.add("aszxcvdf");

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tk_unit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUnits);
        UnitsRecyclerViewAdapter adapter = new UnitsRecyclerViewAdapter(this, tempData);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}