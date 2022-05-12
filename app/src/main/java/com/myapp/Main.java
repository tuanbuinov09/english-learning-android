package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.myapp.dictionary.DictionaryActivity;
import com.myapp.dictionary.YourWordActivity;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.learnenglish.LearnEnglishActivity;
import com.myapp.model.Settings;
import com.myapp.utils.FileIO;

import java.io.File;
import java.util.Locale;

public class Main extends AppCompatActivity {

    private Button buttonLearnEnglish, btnToAllWord, btnToYourWord, buttonTranslateText, buttonSettings, buttonAccount,
            buttonTranslateCamera, buttonTranslateImage;
    FloatingActionButton fab;

    EditText searchInput = null;
    public static TextToSpeech ttobj;
    DatabaseAccess DB;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_linearlayout);

        setControl();
        setEvent();

        ttobj = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    ttobj.setLanguage(Locale.ENGLISH);
                }
            }
        });

        File path = getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_CONFIG_NAME);

        //CREATE SETTINGS FILE
        if (!file.exists()) {
            FileIO.writeToFile(new Settings(), this);
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
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        fab = findViewById(R.id.fab);
        try{
            DB = DatabaseAccess.getInstance(getApplicationContext());
            mAuth = FirebaseAuth.getInstance();
            GlobalVariables.userId = mAuth.getCurrentUser().getUid();
        }catch (Exception ex){

        }

    }

    public void search(View view) {
        Toast.makeText(this, "bạn vừa tìm: " + searchInput.getText().toString().trim(), Toast.LENGTH_LONG).show();
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
        finish();
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
        Intent intent = new Intent(this, LearnEnglishActivity.class);
        startActivity(intent);
    }

    private void handleButtonTranslateTextClick(View view) {
        Intent intent = new Intent(this, TranslateTextActivity.class);
        startActivity(intent);
    }

    private void handleButtonSettingsClick(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void getSavedWordOfUser(){

        GlobalVariables.db.collection("saved_word").whereEqualTo("user_id",GlobalVariables.userId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        GlobalVariables.listSavedWordId.clear();
                        for (DocumentSnapshot snapshot : task.getResult()){
//                            String wordIdstr = snapshot.getString("word_id");
                            long wordId1= snapshot.getLong("word_id");
//                            System.out.println("/////////////"+wordId1);
                            int wordId = (int) wordId1;
//                            Model model = new Model(snapshot.getString("id")); /*, snapshot.getString("title") , snapshot.getString("desc")*/
                            GlobalVariables.listSavedWordId.add(wordId);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Main.this, "Oops ... something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}