package com.myapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.dialog.CustomDialog;
import com.myapp.dtbassethelper.DatabaseAccess;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity{

    private static final int MY_REQUEST_CODE = 0;
    final  String DATABASE_NAME = "tudien.db";
    DatabaseAccess DB;
    SQLiteDatabase database;
    EditText tvHoten, tvEmail, tvSdt, tvUID;
    TextView tvtaikhoan, tvTen, tvPoint;
    Button btnCapNhat, btnLogout;
    LinearLayout btnSynchFromFirebase, btnSynchToFirebase;
    String iduser;
    User user;
    ProgressDialog progressDialog;
    StorageReference storageReference;
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
        setContentView(R.layout.activity_profile);
        DB = DatabaseAccess.getInstance(getApplicationContext());
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        AnhXa();

//        iduser = DB.iduser;
        // mMainActivity = new MainActivity() ;
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
        LayUser();
        LayImage();
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
        if(ProfileActivity.this == null){
            return;
        }
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
            openGallery();
            changeimage = true;
            return;
        }
        if(ProfileActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
            changeimage = true;
        }else{
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ProfileActivity.this.requestPermissions(permissions,MY_REQUEST_CODE);
        }
    }

    private void AnhXa()
    {
        btnSynchFromFirebase = findViewById(R.id.btnSynchFromFirebase);
        btnSynchToFirebase = findViewById(R.id.btnSynchToFirebase);
        tvHoten = findViewById(R.id.textIntEdtHoten);
        tvEmail = findViewById(R.id.textIntEdtEmail);
        tvSdt = findViewById(R.id.textIntEdtSdt);
        tvUID = findViewById(R.id.textIntEdtUID);
        tvtaikhoan = findViewById(R.id.tVusername);
        tvTen = findViewById(R.id.textViewTen);
        tvPoint = findViewById(R.id.textviewPoint);
        btnCapNhat = findViewById(R.id.buttonCapNhat);
        btnLogout = findViewById(R.id.buttonLogout);
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
        uploadImage();
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
                            Toast.makeText(ProfileActivity.this,"Update Success",Toast.LENGTH_SHORT).show();
                            //mMainActivity.showUserInformation();


                        }
                    }
                });

    }
    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
        progressDialog.show();


//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
//        Date now = new Date();
//        String fileName = formatter.format(now);
        String fileName = user.getIduser();
        storageReference = FirebaseStorage.getInstance().getReference("userimage/"+fileName);


        storageReference.putFile(mUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //binding.firebaseimage.setImageURI(null);
                        Toast.makeText(ProfileActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


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
        //tvtaikhoan.setText(user.getEmail());
        tvPoint.setText(String.valueOf(user.getPoint()));
        tvEmail.setText(user.getEmail());
        tvSdt.setText(user.getSDT());
        tvUID.setText(user.getIduser());
        imageView = findViewById(R.id.img_avatar);
    }

    public void LayUser()
    {
        database = Database.initDatabase(ProfileActivity.this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
//        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf("UaceqeYAkxY2sGqZfWsUGeSxcRA2")});

        if( cursor != null && cursor.moveToNext()  ){
//            cursor.moveToNext();
            String Iduser = cursor.getString(0);
            String HoTen = cursor.getString(1);
            int Point = cursor.getInt(2);
            String Email = cursor.getString(3);
            String SDT = cursor.getString(4);
            user = new User(Iduser,HoTen,Point,Email,SDT);
//            Toast.makeText(this, Iduser, Toast.LENGTH_LONG).show();
            //setUserInformation();
//        ThongTinTaikhoanActivity.context = getApplicationContext();

            //Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
            TruyenThongTin();
        }else{
            Toast.makeText(this, "FAILLLL ", Toast.LENGTH_LONG).show();
        }


    }
    public void LayImage(){
        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();
        storageRef.child("userimage/"+user.getIduser()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(ProfileActivity.this).load(uri).error(R.drawable.ic_avatar_default).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(ProfileActivity.this, "Load image fail", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //    private void setUserInformation() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
//        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
//
//    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"));
    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent intent = new Intent(ProfileActivity.this, Main.class);
        startActivity(intent);
    }

}


//package com.myapp;
//
//import android.Manifest;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.UserProfileChangeRequest;
//import com.google.firebase.database.FirebaseDatabase;
//import com.myapp.dtbassethelper.DatabaseAccess;
//import com.myapp.learnenglish.LearnEnglishActivity;
//
//import java.io.IOException;
//
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private static final int MY_REQUEST_CODE = 0;
//    final  String DATABASE_NAME = "tudien.db";
//    DatabaseAccess DB;
//    SQLiteDatabase database;
//    EditText tvHoten,tvEmail,tvSdt,tvUID;
//    TextView tvtaikhoan, tvTen,tvPoint;
//    Button btnCapNhat,btnLogout;
//    String iduser;
//    User user;
//    private Main mMainActivity ;
//    private  Uri mUri;
//    private boolean changeimage=false;
//    private ImageView imageView;
//    private static Context context;
//    public void setUri(Uri mUri) {
//        this.mUri = mUri;
//    }
//    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode()==RESULT_OK){
//                        Intent intent = result.getData();
//                        if(intent == null){
//                            return;
//                        }
//                        Uri uri = intent.getData();
//                        setUri(uri);
//                        try {
//                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                            setBitmapImageView(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//            });
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        DB = DatabaseAccess.getInstance(getApplicationContext());
//        AnhXa();
//
////        iduser = DB.iduser;
//        // mMainActivity = new MainActivity() ;
////        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
////        if(user == null){
////            return;
////        }
//        LayUser();
//        LayDiemUser();
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickRequestPermission();
//
//            }
//        });
//        btnCapNhat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CapNhatThongTin();
//                // onClickUpdateProfile();
//            }
//        });
//
//
//    }
//    private void onClickRequestPermission() {
////        MainActivity mainActivity = (MainActivity) ChangePassword.this;
//        if(ProfileActivity.this == null){
//            return;
//        }
//        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
//            openGallery();
//            changeimage = true;
//            return;
//        }
//        if(ProfileActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//            openGallery();
//            changeimage = true;
//        }else{
//            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//            ProfileActivity.this.requestPermissions(permissions,MY_REQUEST_CODE);
//        }
//    }
//
//    private void AnhXa()
//    {
//        tvHoten = findViewById(R.id.textIntEdtHoten);
//        tvEmail = findViewById(R.id.textIntEdtEmail);
//        tvSdt = findViewById(R.id.textIntEdtSdt);
//        tvUID = findViewById(R.id.textIntEdtUID);
//        tvtaikhoan = findViewById(R.id.tVusername);
//        tvTen = findViewById(R.id.textViewTen);
//        tvPoint = findViewById(R.id.textviewPoint);
//        btnCapNhat = findViewById(R.id.buttonCapNhat);
//        btnLogout = findViewById(R.id.buttonLogout);
//        tvUID.setEnabled(false);
//        tvEmail.setEnabled(false);
//        imageView = findViewById(R.id.img_avatar);
//    }
//    private void CapNhatThongTin()
//    {
//        String hoten = tvHoten.getText().toString();
//        String sdt = tvSdt.getText().toString();
//        if(hoten =="" || sdt=="")
//        {
//            Toast.makeText(this, "Không hợp lệ", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Boolean checkupdate = DB.capnhatthongtin(DB.iduser,hoten,sdt);
//            if(checkupdate == true)
//            {
//                Toast.makeText(this, "Câp nhật thành công", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                Toast.makeText(this, "Thất bại", Toast.LENGTH_SHORT).show();
//            }
//        }
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
//        String strfullname= hoten;
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(strfullname)
//                .build();
//        if(changeimage == true){
//            profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setPhotoUri(mUri)
//                    .build();
//        }
//
//        user.updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ProfileActivity.this,"Update Success",Toast.LENGTH_SHORT).show();
//                            //mMainActivity.showUserInformation();
//
//
//                        }
//                    }
//                });
//    }
//
//    public void setBitmapImageView(Bitmap bitmapImageView){
//        imageView.setImageBitmap(bitmapImageView);
//    }
//    private void TruyenThongTin(){
//        //Truyền thông tin
//        tvHoten.setText(user.getHoTen());
//        tvTen.setText(user.getHoTen());
//        tvtaikhoan.setText(user.getEmail());
//        tvPoint.setText(String.valueOf(user.getPoint()));
//        tvEmail.setText(user.getEmail());
//        tvSdt.setText(user.getSDT());
//        tvUID.setText(user.getIduser());
//        imageView = findViewById(R.id.img_avatar);
//    }
//    public void LayDiemUser()
//    {
//
//        DB.capnhatdiem0(user.getIduser(),user.getPoint(),0);
////        database = Database.initDatabase(ProfileActivity.this, DATABASE_NAME);
////        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
//////        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf("UaceqeYAkxY2sGqZfWsUGeSxcRA2")});
////
////        if( cursor != null && cursor.moveToNext()  ){
//////            cursor.moveToNext();
////            String Iduser = cursor.getString(0);
////            String HoTen = cursor.getString(1);
////            int Point = cursor.getInt(2);
////            String Email = cursor.getString(3);
////            String SDT = cursor.getString(4);
////            user = new User(Iduser,HoTen,Point,Email,SDT);
////            Toast.makeText(this, Iduser, Toast.LENGTH_LONG).show();
////            setUserInformation();
//////        ThongTinTaikhoanActivity.context = getApplicationContext();
////
////            //Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
////            TruyenThongTin();
////        }else{
////            Toast.makeText(this, "FAILLLL ", Toast.LENGTH_LONG).show();
////        }
//
//    }
//
//    public void LayUser()
//    {
//        database = Database.initDatabase(ProfileActivity.this, DATABASE_NAME);
//        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf(DB.iduser)});
////        Cursor cursor = database.rawQuery("SELECT * FROM User WHERE ID_User = ?",new String[]{String.valueOf("UaceqeYAkxY2sGqZfWsUGeSxcRA2")});
//
//        if( cursor != null && cursor.moveToNext()  ){
////            cursor.moveToNext();
//            String Iduser = cursor.getString(0);
//            String HoTen = cursor.getString(1);
//            int Point = cursor.getInt(2);
//            String Email = cursor.getString(3);
//            String SDT = cursor.getString(4);
//            user = new User(Iduser,HoTen,Point,Email,SDT);
//            Toast.makeText(this, Iduser, Toast.LENGTH_LONG).show();
//            setUserInformation();
////        ThongTinTaikhoanActivity.context = getApplicationContext();
//
//            //Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
//            TruyenThongTin();
//        }else{
//            Toast.makeText(this, "FAILLLL ", Toast.LENGTH_LONG).show();
//        }
//
//    }
//    private void setUserInformation() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
//        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
//
//    }
//    public void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"));
//    }
//    @Override
//    public void onBackPressed() {
//        // your code.
//        Intent intent = new Intent(ProfileActivity.this, LearnEnglishActivity.class);
//        startActivity(intent);
//    }
//
//
//}