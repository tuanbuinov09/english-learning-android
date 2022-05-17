package com.myapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myapp.adapter.EnWordRecyclerAdapter;
import com.myapp.dictionary.DictionaryActivity;
import com.myapp.dictionary.YourWordActivity;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.learnenglish.LearnEnglishActivity;
import com.myapp.model.EnWord;
import com.myapp.model.Settings;
import com.myapp.utils.ChangeSearchView;
import com.myapp.utils.FileIO;
import com.myapp.utils.FileIO2;
import com.myapp.utils.FileIO3;
import com.myapp.utils.SoftKeyboard;

import java.io.File;
import java.util.ArrayList;

public class Main extends AppCompatActivity {

    private Button buttonLearnEnglish, btnToAllWord, btnToYourWord, buttonTranslateText, buttonSettings, buttonAccount,
            buttonTranslateCamera, buttonTranslateImage, buttonHistory;
    ImageButton btnMic;

    FloatingActionButton fab;
    LinearLayout floatingLinearLayout;
    androidx.appcompat.widget.SearchView searchInput = null;
    public RecyclerView recyclerView;
    public EnWordRecyclerAdapter enWordRecyclerAdapter;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrollOutItems;
    ProgressBar progressBar;

    //public static TextToSpeech ttobj;
    DatabaseAccess DB;
    private FirebaseAuth mAuth;

    private final int REQUEST_MIC_CODE = 111;
    private final int REQUEST_WRITE = 105;
    private final int REQUEST_CAMERA = 109;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_linearlayout);
        setContentView(R.layout.main_relative);

        setControl();
        setEvent();

//        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if (status == TextToSpeech.SUCCESS) {
//                    ttobj.setLanguage(Locale.ENGLISH);
//                }
//            }
//        });

        //CREATE SETTINGS FILE
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_CONFIG_NAME);

        if (!file.exists()) {
            FileIO.writeToFile(new Settings(), getApplicationContext());
        }

        //CREATE HISTORY FILE
        File path2 = getApplicationContext().getFilesDir();
        File file2 = new File(path2, GlobalVariables.FILE_HISTORY_NAME);

        if (!file2.exists()) {
            FileIO2.writeToFile(new ArrayList<>(), getApplicationContext());
        }

        //CREATE ALARM FILE
        File path3 = getApplicationContext().getFilesDir();
        File file3 = new File(path3, GlobalVariables.FILE_ALARM_SET);

        //CREATE SETTINGS FILE
        if (!file3.exists()) {
            FileIO3.writeToFile(new ArrayList<>(), getApplicationContext());
        }

        checkFAB();
        ChangeSearchView.change(searchInput, this);
        askPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        GlobalVariables.userId = databaseAccess.getCurrentUserId__OFFLINE();
        databaseAccess.close();

        getSavedWordOfUser();
        checkFAB();

//        try{
//            DB = DatabaseAccess.getInstance(getApplicationContext());
//            mAuth = FirebaseAuth.getInstance();
//            GlobalVariables.userId = mAuth.getCurrentUser().getUid();
//        }catch (Exception ex){
//
//        }

        //để khi lưu hay bỏ lưu ở word detail thì cái nàfy đc cậpj nhật
        enWordRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_MIC_CODE) {
            String speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            searchInput.setQuery(speechText, true);
        }
    }

    private void setEvent() {
        buttonLearnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClickLearnEnglish(view);
            }
        });

        btnToAllWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAllWordClick(view);
            }
        });
        btnToYourWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleYourWordClick(view);
            }
        });

        buttonTranslateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonTranslateTextClick(view);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleButtonSettingsClick(view);
            }
        });
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAccount(view);
            }
        });
        buttonTranslateCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, CropActivity.class);
                intent.putExtra("request", CropActivity.OPEN_CAMERA_CODE);
                startActivity(intent);
            }
        });
        buttonTranslateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, CropActivity.class);
                intent.putExtra("request", CropActivity.OPEN_GALLERY_CODE);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyboard.showKeyboard(searchInput, getApplicationContext());
                searchInput.performClick();
                searchInput.requestFocus();
            }
        });

        enWordRecyclerAdapter = new EnWordRecyclerAdapter(getApplicationContext());

        searchInput.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                enWordRecyclerAdapter.filterList(new ArrayList<EnWord>());
                floatingLinearLayout.setVisibility(View.GONE);
                SoftKeyboard.hideSoftKeyboard(searchInput, getApplicationContext());
                return false;
            }
        });
        searchInput.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
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
        btnMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking...");
                startActivityForResult(intent, REQUEST_MIC_CODE);
            }
        });
        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
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

    private void filter(String text) {
        // if query is empty: return all
        if (text.isEmpty()) {
            GlobalVariables.listFilteredWords.removeAll(GlobalVariables.listFilteredWords);
            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
            floatingLinearLayout.setVisibility(View.GONE);
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
            enWordRecyclerAdapter = new EnWordRecyclerAdapter(this, GlobalVariables.listFilteredWords);
            recyclerView.setAdapter(enWordRecyclerAdapter);
            manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);
            floatingLinearLayout.setVisibility(View.VISIBLE);
            enWordRecyclerAdapter.filterList(GlobalVariables.listFilteredWords);
        }
    }

    private void setControl() {
        GlobalVariables.db = FirebaseFirestore.getInstance();
//        // Create a reference to the cities collection
//        CollectionReference savedWordRef = GlobalVariables.db.collection("saved_word");
//
//        // Create a query against the collection.
//        Query query = savedWordRef.whereEqualTo("user_id", GlobalVariables.userId);

        getSavedWordOfUser();
        buttonLearnEnglish = findViewById(R.id.buttonLearnEnglish);
        btnToAllWord = findViewById(R.id.btnToAllWord);
        btnToYourWord = findViewById(R.id.buttonToYourWord);
        buttonTranslateText = findViewById(R.id.buttonTranslateText);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonTranslateCamera = findViewById(R.id.buttonTranslateCamera);
        buttonTranslateImage = findViewById(R.id.buttonTranslateImage);
        buttonAccount = findViewById(R.id.buttonAccount);
        searchInput = findViewById(R.id.searchInput);
        floatingLinearLayout = findViewById(R.id.floatingLinearLayout);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progress_bar);
        btnMic = findViewById(R.id.btnMic);
        buttonHistory = findViewById(R.id.buttonHistory);

        //default, k có từ nào trong adapter


        fab = findViewById(R.id.fab);

        //lấy user id
//        try {
//            DB = DatabaseAccess.getInstance(getApplicationContext());
//            mAuth = FirebaseAuth.getInstance();
//            GlobalVariables.userId = mAuth.getCurrentUser().getUid();
//        } catch (Exception ex) {
//
//        }

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        GlobalVariables.userId = databaseAccess.getCurrentUserId__OFFLINE();
        databaseAccess.close();
    }

    public void search(View view) {
        Toast.makeText(this, "bạn vừa tìm: " + searchInput.getQuery().toString().trim(), Toast.LENGTH_LONG).show();
    }

    public void toAccount(View view) {
//        if (GlobalVariables.username == null) {
//            // go to sign in
////            Intent signInIntent = new Intent(this, SignIn.class);
////            Intent signInIntent = new Intent(this, SignInActivity.class);
////            startActivity(signInIntent);
//        } else {
//            Toast.makeText(this, "đăng nhập thành công", Toast.LENGTH_LONG).show();
//        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 1000);

//        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//        if(user==null){
//            //Chưa login
//            Intent intent = new Intent(this,SignInActivity.class);
//            startActivity(intent);
//        }else{
//            Intent intent = new Intent(this,ThongTinTaikhoanActivity.class);
//            startActivity(intent);
//        }

    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //Chưa login
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ThongTinTaikhoanActivity.class);
            startActivity(intent);
        }
    }

    private void nextActivityLearnEnglish() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            //Chưa login
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, LearnEnglishActivity.class);
            startActivity(intent);
        }
    }

    public void handleYourWordClick(View view) {
        Intent yourWordIntent = new Intent(this, YourWordActivity.class);
        startActivity(yourWordIntent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleAllWordClick(View view) {
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void handleClickLearnEnglish(View view) {
//        Intent intent = new Intent(this, LearnEnglishActivity.class);
//        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivityLearnEnglish();
            }
        }, 1000);
    }

    private void handleButtonTranslateTextClick(View view) {
        Intent intent = new Intent(this, TranslateTextActivity.class);
        startActivity(intent);
    }

    private void handleButtonSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getSavedWordOfUser() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (true) {//if(connected==false)
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
            databaseAccess.open();
            GlobalVariables.listSavedWordId.clear();
            GlobalVariables.listSavedWordId = databaseAccess.getListSavedWordIdFromSQLite(GlobalVariables.userId);
            databaseAccess.close();
        }/*else{
            GlobalVariables.db.collection("saved_word").whereEqualTo("user_id", GlobalVariables.userId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            GlobalVariables.listSavedWordId.clear();
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                long wordId1 = snapshot.getLong("word_id");
                                int wordId = (int) wordId1;
                                GlobalVariables.listSavedWordId.add(wordId);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Main.this, "Oops ... something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }

    private void checkFAB() {
        Settings settings = FileIO.readFromFile(this);
        if (settings.isSwitchFAB()) fab.setVisibility(View.VISIBLE);
        else fab.setVisibility(View.GONE);
    }

    public void askPermission() {
        String[] PERMISSIONS = {
                Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.RECEIVE_BOOT_COMPLETED,

        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        ;
    }

    private boolean hasPermissions(Context context, String... PERMISSIONS) {
        if (context != null && PERMISSIONS != null) {
            for (String p : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(context, p) == PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "INTERNET", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "CAMERA", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[3] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[4] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[5] == PackageManager.PERMISSION_GRANTED) {

            } else {
                //Toast.makeText(this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}