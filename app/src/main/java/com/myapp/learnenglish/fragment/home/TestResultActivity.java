package com.myapp.learnenglish.fragment.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.Database;
import com.myapp.R;
import com.myapp.User;
import com.myapp.dtbassethelper.DatabaseAccess;

public class TestResultActivity extends AppCompatActivity {
    private TextView tvObtainedStars, tvInTotal;
    private Button btnContinue;
    DatabaseAccess DB;
    User user;
    int score=0;
    final  String DATABASE_NAME = "tudien.db";
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.myapp.R.layout.activity_test_result);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        setControl();
        setEvent();
        LayUser();
        tvInTotal.setText(getIntent().getExtras().get("total").toString());
        tvObtainedStars.setText(getIntent().getExtras().get("obtainedStars").toString());
        DB.capnhatdiem(DB.iduser,user.getPoint(), (Integer) getIntent().getExtras().get("total"));
    }
    public void LayUser()
    {
        database = Database.initDatabase(TestResultActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
        cursor.moveToNext();
        String Iduser = cursor.getString(0);
        String HoTen = cursor.getString(1);
        int Point = cursor.getInt(2);
        String Email = cursor.getString(3);
        String SDT = cursor.getString(4);
        user = new User(Iduser,HoTen,Point,Email,SDT);
    }
    private void setEvent() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setControl() {
        tvObtainedStars = findViewById(R.id.tvObtainedStars);
        tvInTotal = findViewById(R.id.tvInTotal);
        btnContinue = findViewById(R.id.btnContinue);
    }
}