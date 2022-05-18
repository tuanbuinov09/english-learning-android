package com.myapp.dictionary;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.EnWordRecyclerAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;

import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView searchInput = null;
    public RecyclerView recyclerView;
    public EnWordRecyclerAdapter enWordRecyclerAdapter;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar;

    ArrayList<EnWord> filteredlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_dictionary);

        setControl();
        setEvent();

        setTitle("Từ điển Anh-Việt");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);

        changeSearchView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.en_word_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                filter(s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                filter(s);
//                return false;
//            }
//        });
//        return true;
//    }

    //duwngf scroll truoc khi nhap tu khoa de tranh loi
    private void filter(String text) {

        // if query is empty: return all
        if (text.isEmpty()) {
            enWordRecyclerAdapter.filterList(GlobalVariables.listAllWords);
            return;
        }
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        GlobalVariables.listFilteredWords.removeAll(GlobalVariables.listFilteredWords);
        GlobalVariables.listFilteredWords = databaseAccess.searchEnWord_NoPopulateWithOffsetLimit(text, GlobalVariables.offset, GlobalVariables.limit);
        databaseAccess.close();

        if (GlobalVariables.listFilteredWords.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
            enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listFilteredWords);
            recyclerView.setAdapter(enWordRecyclerAdapter);
            manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
        }
    }

    private void setControl() {
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();

//        list =  databaseAccess.getAllEnWord_NoPopulate();
        GlobalVariables.listAllWords = databaseAccess.getAllEnWord_NoPopulateWithOffsetLimit(GlobalVariables.offset, GlobalVariables.limit);
        databaseAccess.close();
        System.out.println("-------------" + GlobalVariables.listAllWords.size());

        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
        recyclerView.setAdapter(enWordRecyclerAdapter);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
//        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllWords);
//        recyclerView.setAdapter(enWordRecyclerAdapter);
//        manager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(manager);
    }

    private void setEvent() {
        searchInput.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    // lấy thêm data
                    fetchData();
                }
            }
        });
    }

    public void setEnWordAdapter() {


    }

    private void changeSearchView() {

        TextView textView = (TextView) searchInput.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(getColor(R.color.space_cadet));
        textView.setHintTextColor(getColor(R.color.azureish_white));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //để khi lưu hay bỏ lưu ở word detail thì cái nàfy đc cậpj nhật
        enWordRecyclerAdapter.notifyDataSetChanged();
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!searchInput.getQuery().toString().trim().equalsIgnoreCase("")) {
                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    databaseAccess.open();
                    GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;

                    ArrayList<EnWord> justFetched = databaseAccess.searchEnWord_NoPopulateWithOffsetLimit(searchInput.getQuery().toString().trim(), GlobalVariables.offset, GlobalVariables.limit);

                    databaseAccess.close();

                    GlobalVariables.listFilteredWords.addAll(justFetched);
                    enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);

                    progressBar.setVisibility(View.GONE);
                    return;
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;

                ArrayList<EnWord> justFetched = databaseAccess.getAllEnWord_NoPopulateWithOffsetLimit(GlobalVariables.offset, GlobalVariables.limit);

                databaseAccess.close();

                GlobalVariables.listAllWords.addAll(justFetched);
                enWordRecyclerAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }
        }, 3000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}