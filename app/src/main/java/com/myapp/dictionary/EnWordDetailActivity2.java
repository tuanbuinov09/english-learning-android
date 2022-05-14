package com.myapp.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.SetOptions;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.dictionary.fragment.EnWordDetailFragment;
import com.myapp.dictionary.fragment.YourNoteFragment;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.learnenglish.fragment.home.HomeFragment;
import com.myapp.model.EnWord;

import java.util.HashMap;

public class EnWordDetailActivity2 extends AppCompatActivity {
    public int enWordId;
    EnWord savedWord;
    BottomNavigationView topNavigation;
    FragmentManager fragmentManager;
    public ImageButton btnSave_UnsaveWord;
    public ImageButton btnBackToSavedWord;

    TextView textViewTitle;
    boolean unsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_word_detail2);
        setControl();
        setEvent();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new EnWordDetailFragment(savedWord)).commit();

    }


    private void setEvent() {
        textViewTitle.setText(savedWord.getWord().trim());
        btnBackToSavedWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), YourWordActivity.class);
                startActivity(intent);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        // neu trong danh sach da luu thi to mau vang
        if (GlobalVariables.listSavedWordId.contains(savedWord.getId())) {
            unsave = true;
            btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
        } else {
            unsave = false;
            btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
        }

        // luu tu xoa tu
        btnSave_UnsaveWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (unsave == true) {
                    //---run unsave code
                    GlobalVariables.db.collection("saved_word").document(GlobalVariables.userId + savedWord.getId() + "")
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EnWordDetailActivity2.this, "Xoa từ khoi danh sach thanh cong", Toast.LENGTH_LONG).show();
                                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(savedWord.getId()));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EnWordDetailActivity2.this, "Xoa từ khoi danh sach that bai", Toast.LENGTH_LONG).show();

                                }
                            });

                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_bookmark_outline_32px);
                    unsave = !unsave;
                } else {
                    //---run save code
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("user_id", GlobalVariables.userId);
                    map.put("word_id", savedWord.getId());
                    GlobalVariables.db.collection("saved_word")
                            .document(GlobalVariables.userId + savedWord.getId() + "").set(map, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(EnWordDetailActivity2.this, "Lưu từ thành công", Toast.LENGTH_LONG).show();
                                    //them ca vao trong nay cho de dung
                                    GlobalVariables.listSavedWordId.add((savedWord.getId()));
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EnWordDetailActivity2.this, "Lưu từ khong thành công", Toast.LENGTH_LONG).show();
                        }
                    });

                    btnSave_UnsaveWord.setBackgroundResource(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    unsave = !unsave;
                }
            }
        });
//        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//              @Override
//              public void onBackStackChanged() {
//                  Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
//                  if (currentFragment instanceof MainFragment) {
//                      //place your filtering logic here using currentFragment
//                  }
//              }
//          });
        topNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.pageEnWordDetail:

                    Bundle bundle = new Bundle();
                    bundle.putInt("enWordId", enWordId);

                    bundle.putSerializable("enWord", savedWord);
                    selectedFragment = new EnWordDetailFragment(savedWord);
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.pageYourNote:
                    Bundle bundleYourNote = new Bundle();
                    bundleYourNote.putInt("enWordId", enWordId);
                    selectedFragment = new YourNoteFragment();
                    selectedFragment.setArguments(bundleYourNote);
                    break;
            }

            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                    selectedFragment, null).addToBackStack(null).commit();
            return true;
        });
    }

    private void setControl() {
        topNavigation = findViewById(R.id.topNavigation);
        fragmentManager = getSupportFragmentManager();
        enWordId = getIntent().getIntExtra("enWordId", -1);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        databaseAccess.open();
        savedWord = databaseAccess.getOneEnWord(enWordId);
        databaseAccess.close();

        textViewTitle = findViewById(R.id.textViewTitle);
        btnSave_UnsaveWord = findViewById(R.id.btnSave_UnsaveWord);
        btnBackToSavedWord = findViewById(R.id.imgBtnBackToSavedWord);
        unsave = true;
    }
}