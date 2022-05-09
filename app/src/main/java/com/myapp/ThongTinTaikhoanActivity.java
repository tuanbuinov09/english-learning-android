package com.myapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myapp.dtbassethelper.DatabaseAccess;

import java.io.IOException;


public class ThongTinTaikhoanActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 0;
    final  String DATABASE_NAME = "tudien.db";
    DatabaseAccess DB;
    SQLiteDatabase database;
    EditText tvHoten,tvEmail,tvSdt,tvUID;
    TextView tvtaikhoan, tvTen;
    Button btnCapNhat;
    String iduser;
    User user;
    private Main mMainActivity ;
    private  Uri mUri;
    private boolean changeimage=false;
    private ImageView imageView;
    private static Context context;
    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent intent = result.getData();
                        if(intent == null){
                            return;
                        }
                        Uri uri = intent.getData();
                        setUri(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            setBitmapImageView(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_taikhoan);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        AnhXa();

        iduser = DB.iduser;
       // mMainActivity = new MainActivity() ;
        LayUser();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();

            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CapNhatThongTin();
               // onClickUpdateProfile();
            }
        });

    }
    private void onClickRequestPermission() {
//        MainActivity mainActivity = (MainActivity) ChangePassword.this;
        if(ThongTinTaikhoanActivity.this == null){
            return;
        }
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
            openGallery();
            changeimage = true;
            return;
        }
        if(ThongTinTaikhoanActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
            changeimage = true;
        }else{
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ThongTinTaikhoanActivity.this.requestPermissions(permissions,MY_REQUEST_CODE);
        }
    }

    private void AnhXa()
    {
        tvHoten = findViewById(R.id.textIntEdtHoten);
        tvEmail = findViewById(R.id.textIntEdtEmail);
        tvSdt = findViewById(R.id.textIntEdtSdt);
        tvUID = findViewById(R.id.textIntEdtUID);
        tvtaikhoan = findViewById(R.id.tVusername);
        tvTen = findViewById(R.id.textViewTen);
//        tvPoint = findViewById(R.id.textviewPoint);
        btnCapNhat = findViewById(R.id.buttonCapNhat);

        tvUID.setEnabled(false);
        tvEmail.setEnabled(false);
        imageView = findViewById(R.id.img_avatar);
    }
    private void CapNhatThongTin()
    {
        String hoten = tvHoten.getText().toString();
        String sdt = tvSdt.getText().toString();
        if(hoten =="" || sdt=="")
        {
            Toast.makeText(this, "Không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean checkupdate = DB.capnhatthongtin(DB.iduser,hoten,sdt);
            if(checkupdate == true)
            {
                Toast.makeText(this, "Câp nhật thành công", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
            }
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        String strfullname= hoten;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strfullname)
                .build();
        if(changeimage == true){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(mUri)
                    .build();
        }

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ThongTinTaikhoanActivity.this,"Update Success",Toast.LENGTH_SHORT).show();
                            //mMainActivity.showUserInformation();


                        }
                    }
                });
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        imageView.setImageBitmap(bitmapImageView);
    }
    private void TruyenThongTin(){
        //Truyền thông tin
        tvHoten.setText(user.getHoTen());
        tvTen.setText(user.getHoTen());
        tvtaikhoan.setText(user.getEmail());
//        tvPoint.setText(String.valueOf(user.getPoint()));
        tvEmail.setText(user.getEmail());
        tvSdt.setText(user.getSDT());
        tvUID.setText(user.getIduser());
        imageView = findViewById(R.id.img_avatar);
    }

    public void LayUser()
    {
        database = Database.initDatabase(ThongTinTaikhoanActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
        cursor.moveToNext();
        String Iduser = cursor.getString(0);
        String HoTen = cursor.getString(1);
        int Point = cursor.getInt(2);
        String Email = cursor.getString(3);
        String SDT = cursor.getString(4);
        user = new User(Iduser,HoTen,Point,Email,SDT);
        setUserInformation();
//        ThongTinTaikhoanActivity.context = getApplicationContext();

        //Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
        TruyenThongTin();

    }
    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
//        if(user.getDisplayName().equals("")){
//            edtFullName.setText("No name");
//        }
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);

    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"));
    }



}