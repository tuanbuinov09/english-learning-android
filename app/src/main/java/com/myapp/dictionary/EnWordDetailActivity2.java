package com.myapp.dictionary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.SetOptions;
import com.myapp.GlobalVariables;
import com.myapp.R;
import com.myapp.adapter.ViewPagerAdapter;
import com.myapp.dictionary.fragment.EnWordDetailFragment;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.model.EnWord;
import com.myapp.utils.FileIO2;

import java.util.HashMap;
import java.util.List;

public class EnWordDetailActivity2 extends AppCompatActivity {
    public int enWordId;
    EnWord savedWord;
    BottomNavigationView topNavigation;
    FragmentManager fragmentManager;
    public ImageButton btnSave_UnsaveWord;
    public ImageButton btnBackToSavedWord;

    TextView textViewTitle;
    boolean unsave;

    private final int REQUEST_WRITE = 888;

    MenuItem menuSave_unSaveWord;
    Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_word_detail2);
        setControl();
        setEvent();

        setTitle(savedWord.getWord().trim());
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
        getSupportActionBar().setElevation(0);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new EnWordDetailFragment(savedWord)).commit();

        askPermission();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, enWordId, savedWord);
        viewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("ENG-VIET");
                    break;
                case 1:
                    tab.setText("HÌNH ẢNH");
                    break;
                case 2:
                    tab.setText("GHI CHÚ");
                    break;

            }
        }).attach();

        //GHI LẠI LỊCH SỬ TRA TỪ
        List<Integer> wordList = FileIO2.readFromFile(this);
        if (wordList.contains(enWordId)) {
            wordList.remove(wordList.indexOf(enWordId));
        }
        wordList.add(0, enWordId);
        FileIO2.writeToFile(wordList, this);
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
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EnWordDetailActivity2.this, "Xoa từ khoi danh sach that bai", Toast.LENGTH_LONG).show();

                                }
                            });
                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(savedWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    databaseAccess.open();
                    databaseAccess.unSaveOneWord(GlobalVariables.userId, savedWord.getId());
                    databaseAccess.close();

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
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EnWordDetailActivity2.this, "Lưu từ khong thành công", Toast.LENGTH_LONG).show();
                        }
                    });
                    GlobalVariables.listSavedWordId.add((savedWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                    databaseAccess.open();
                    databaseAccess.saveOneWord(GlobalVariables.userId, savedWord.getId());
                    databaseAccess.close();

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
//        topNavigation.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//            switch (item.getItemId()) {
//                case R.id.pageEnWordDetail:
//
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("enWordId", enWordId);
//
//                    bundle.putSerializable("enWord", savedWord);
//                    selectedFragment = new EnWordDetailFragment(savedWord);
//                    selectedFragment.setArguments(bundle);
//                    break;
//
//                case R.id.menuImage:
//                    Bundle bundle1 = new Bundle();
//                    bundle1.putInt("enWordId", enWordId);
//
//                    bundle1.putSerializable("enWord", savedWord);
//
//                    selectedFragment = new ImageFragment();
//                    selectedFragment.setArguments(bundle1);
//                    break;
//                case R.id.pageYourNote:
//                    Bundle bundleYourNote = new Bundle();
//                    bundleYourNote.putInt("enWordId", enWordId);
//                    selectedFragment = new YourNoteFragment();
//                    selectedFragment.setArguments(bundleYourNote);
//                    break;
//            }
//
//            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,
//                    selectedFragment, null).addToBackStack(null).commit();
//            return true;
//        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.enword_detail_2_menu, menu);
        optionsMenu = menu;
        menuSave_unSaveWord = (MenuItem) optionsMenu.findItem(R.id.menu_save_unsave);
        if (GlobalVariables.listSavedWordId.contains(savedWord.getId())) {
            unsave = true;
            menuSave_unSaveWord.setIcon(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
        } else {
            unsave = false;
            menuSave_unSaveWord.setIcon(R.drawable.icons8_bookmark_outline_32px);
        }
        if (GlobalVariables.userId.equalsIgnoreCase("") || GlobalVariables.userId == null) {
            menuSave_unSaveWord.setVisible(false);
        } else {
            menuSave_unSaveWord.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:

                break;
            case R.id.menu_note:

                break;
            case R.id.menu_save_unsave:
                if (unsave == true) {
                    GlobalVariables.listSavedWordId.remove(GlobalVariables.listSavedWordId.indexOf(savedWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
                    databaseAccess.open();
                    databaseAccess.unSaveOneWord(GlobalVariables.userId, savedWord.getId());
                    databaseAccess.close();

                    item.setIcon(R.drawable.icons8_bookmark_outline_32px);
                    unsave = !unsave;

                } else {
                    //them ca vao trong nay cho de dung
                    GlobalVariables.listSavedWordId.add((savedWord.getId()));

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
                    databaseAccess.open();
                    databaseAccess.saveOneWord(GlobalVariables.userId, savedWord.getId());
                    databaseAccess.close();

                    item.setIcon(R.drawable.icons8_filled_bookmark_ribbon_32px_1);
                    unsave = !unsave;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(EnWordDetailActivity2.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}