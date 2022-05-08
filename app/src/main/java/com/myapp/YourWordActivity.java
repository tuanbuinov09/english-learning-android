package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myapp.adapter.EnWordRecyclerAdapter;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;

import java.util.ArrayList;

public class YourWordActivity extends AppCompatActivity {
    EditText searchInput = null;
    ListView listViewYourWord;
    //    TextToSpeech ttobj;
    ArrayList<EnWord> listEnWord = new ArrayList<>();

    private RecyclerView recyclerView;
    private EnWordRecyclerAdapter enWordRecyclerAdapter;
    private ArrayList<EnWord> list;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(GlobalVariables.username.equalsIgnoreCase("") || GlobalVariables.username == null){
//            Toast.makeText(this,"Hãy đăng nhập để sử dụng tính năng này",Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this, SignIn.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_word);
//        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    ttobj.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });

        setControl();
        setEvent();

    }


    public void setYourWordAdapter() {



        //---------------
//        YourWordAdapter adapter = new
//                YourWordAdapter(this, this.listEnWord, R.layout.en_word_item);
//        listViewYourWord.setAdapter(adapter);
//        listViewYourWord.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Toast.makeText(YourWordActivity.this, "Bạn chọn " +
//                        listEnWord.get(position).getId(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
//                intent.putExtra("enWordId", listEnWord.get(position).getId());
//                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//        });
//
//        for(EnWord en : listEnWord){
//            System.out.println("----"+en.toString());
//        }
    }

    private void setEvent() {
        setYourWordAdapter();
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

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                GlobalVariables.offset = GlobalVariables.offset + GlobalVariables.limit;

                ArrayList<EnWord> justFetched = new ArrayList<>();
                justFetched = databaseAccess.getFakeSavedWord_NoPopulateWithOffsetLimit(GlobalVariables.offset, GlobalVariables.limit);

                databaseAccess.close();

                GlobalVariables.listAllSavedWords.addAll(justFetched);
                enWordRecyclerAdapter.notifyDataSetChanged();

                progressBar.setVisibility(View.GONE);

            }
        }, 3000);
    }
//        try {
//            enWordList = (ArrayList<EnWord>) new UserDAO().getSavedWordlist("username");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

//        YourWordAdapter adapter = new
//                YourWordAdapter(this, this.listEnWord, R.layout.yourword_listview_item, ttobj);
//        listViewYourWord.setAdapter(adapter);
//        listViewYourWord.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Toast.makeText(YourWordActivity.this, "Bạn chọn " +
//                        listEnWord.get(position).getId(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(view.getContext(), EnWordDetailActivity.class);
//                intent.putExtra("enWord", listEnWord.get(position));
//                view.getContext().startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//            }
//        });

//    }

    private void setControl() {
        searchInput = findViewById(R.id.searchInput);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
//        list =  databaseAccess.getAllEnWord_NoPopulate();
        GlobalVariables.listAllSavedWords = databaseAccess.getAllEnWord_NoPopulateWithOffsetLimit(GlobalVariables.offset, GlobalVariables.limit);
        databaseAccess.close();
        System.out.println("-------------" + GlobalVariables.listAllSavedWords.size());

        enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listAllSavedWords);
        recyclerView.setAdapter(enWordRecyclerAdapter);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    public void search(View view) {
        Toast.makeText(this, "bạn vừa tìm: " + searchInput.getText().toString().trim() + "trong từ của bạn", Toast.LENGTH_LONG).show();
    }

    public void backToMain(View view) {
        Intent mainIntent = new Intent(this, Main.class);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }


// lấy từ api
    //    private void getSavedWord(int userId, final VolleyCallBack callBack) {
//
//        String url = "http://10.0.2.2:8000/savedword/"+userId;
//        System.out.println("---------------------------------------------------"+url);
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                getData(response);
//            }
//        }, new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(YourWordActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(YourWordActivity.this);
//        requestQueue.add(request);
//    }

//    private void getData(JSONArray array) {
//        try{
////            JSONArray array = response;
//            for(int i=0; i<array.length(); i=i+1){
//                JSONObject object = array.getJSONObject(i);
//
//                EnWord enWord = new EnWord();
//                enWord.setWord(object.getString("word"));
//                enWord.setId(object.getInt("id"));
//                enWord.setViews(object.getInt("views"));
//                enWord.setPronunciation(object.getString("pronunciation"));
//
//                JSONArray meaningArray = object.getJSONArray("meanings");
//                ArrayList<Meaning> listMeaning = new ArrayList<>();
//
//                for(int j=0; j<meaningArray.length(); j=j+1) {
//                    JSONObject objectMeaning = meaningArray.getJSONObject(j);
//                    JSONObject objectPartOfSpeech = objectMeaning.getJSONObject("partOfSpeech");
//                    Meaning meaning = new Meaning();
//
//                    meaning.setMeaning(objectMeaning.getString("meaning"));
//                    meaning.setId(objectMeaning.getInt("id"));
//                    meaning.setPartOfSpeechName(objectPartOfSpeech.getString("name"));
//
//                    // bắst trường hợp k có example
//                    JSONArray exampleArray = new JSONArray();
//                    ArrayList<ExampleDetail> listExample = new ArrayList<>();
//                    try{
//                        exampleArray = objectMeaning.getJSONArray("examples");
//                    }catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//                    for(int k=0; k<exampleArray.length(); k=k+1) {
//                        JSONObject exampleObject = exampleArray.getJSONObject(k);
//                        ExampleDetail exampleDetail = new ExampleDetail();
//                        exampleDetail.setId(exampleObject.getInt("id"));
//                        exampleDetail.setMeaningId(exampleObject.getInt("meaningId"));
//                        exampleDetail.setExample(exampleObject.getString("example"));
//                        exampleDetail.setExampleMeaning(exampleObject.getString("exampleMeaning"));
//
//                        listExample.add(exampleDetail);
//                    }
//
//                    meaning.setListExampleDetails(listExample);
//
//                    listMeaning.add(meaning);
//                }
//
//                enWord.setListMeaning(listMeaning);
//                listEnWord.add(enWord);
//            }
//            for(EnWord en : listEnWord){
//                System.out.println("----"+en.toString());
//            }
//            setYourWordAdapter();
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//
//    }
}




