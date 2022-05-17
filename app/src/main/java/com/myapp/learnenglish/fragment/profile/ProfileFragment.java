package com.myapp.learnenglish.fragment.profile;

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
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.myapp.Database;
import com.myapp.Main;
import com.myapp.ProfileActivity;
import com.myapp.R;
import com.myapp.User;
import com.myapp.dtbassethelper.DatabaseAccess;
import com.myapp.learnenglish.LearnEnglishActivity;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int MY_REQUEST_CODE = 0;
    final  String DATABASE_NAME = "tudien.db";
    DatabaseAccess DB;
    SQLiteDatabase database;
    EditText tvHoten,tvEmail,tvSdt,tvUID;
    TextView tvtaikhoan, tvTen,tvPoint;
    Button btnCapNhat,btnLogout;
    String iduser;
    User user;
    private Main mMainActivity ;
    private Uri mUri;
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
                    if(result.getResultCode()==getActivity().RESULT_OK){
                        Intent intent = result.getData();
                        if(intent == null){
                            return;
                        }
                        Uri uri = intent.getData();
                        setUri(uri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                            setBitmapImageView(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //getActivity().setTheme(R.style.Theme_MyApp);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(),R.style.AppTheme);
        //LayoutInflater localInflate = inflater.cloneInContext(contextThemeWrapper);
        //mTheme = ;
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppTheme);

// clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        Context ctx = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        LayoutInflater li = inflater.cloneInContext(ctx);
        li.getContext().setTheme(R.style.AppTheme);
        View view= li.inflate(R.layout.fragment_profile, container, false);

        //this.getActivity().setTheme(R.style.AppTheme);
        DB = DatabaseAccess.getInstance(getActivity().getApplicationContext());
        AnhXa(view);
        //view = inflater.getContext().setTheme(R.style.Theme_MyApp);
//        iduser = DB.iduser;
        // mMainActivity = new MainActivity() ;
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user == null){
//            return;
//        }
        LayUser();
        LayDiemUser();
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

        return view;
    }

    private void onClickRequestPermission() {
//        MainActivity mainActivity = (MainActivity) ChangePassword.this;
        if(ProfileFragment.this == null){
            return;
        }
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
            openGallery();
            changeimage = true;
            return;
        }
        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
            changeimage = true;
        }else{
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions,MY_REQUEST_CODE);
        }
    }

    private void AnhXa(View view)
    {
        tvHoten = view.findViewById(R.id.textIntEdtHoten);
        tvEmail = view.findViewById(R.id.textIntEdtEmail);
        tvSdt = view.findViewById(R.id.textIntEdtSdt);
        tvUID = view.findViewById(R.id.textIntEdtUID);
        tvtaikhoan = view.findViewById(R.id.tVusername);
        tvTen = view.findViewById(R.id.textViewTen);
        tvPoint = view.findViewById(R.id.textviewPoint);
        btnCapNhat = view.findViewById(R.id.buttonCapNhat);
        btnLogout = view.findViewById(R.id.buttonLogout);
        tvUID.setEnabled(false);
        tvEmail.setEnabled(false);
        imageView = view.findViewById(R.id.img_avatar);
//        imageView = findViewById(R.id.img_avatar);
    }
    private void CapNhatThongTin()
    {
        String hoten = tvHoten.getText().toString();
        String sdt = tvSdt.getText().toString();
        if(hoten =="" || sdt=="")
        {
            Toast.makeText(getActivity(), "Không hợp lệ", Toast.LENGTH_SHORT).show();
        }
        else{
            Boolean checkupdate = DB.capnhatthongtin(DB.iduser,hoten,sdt);
            if(checkupdate == true)
            {
                Toast.makeText(getActivity(), "Câp nhật thành công", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getActivity(),"Update Success",Toast.LENGTH_SHORT).show();
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
        tvPoint.setText(String.valueOf(user.getPoint()));
        tvEmail.setText(user.getEmail());
        tvSdt.setText(user.getSDT());
        tvUID.setText(user.getIduser());

    }
    public void LayDiemUser()
    {

        DB.capnhatdiem0(user.getIduser(),user.getPoint(),0);
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

    }

    public void LayUser()
    {
        database = Database.initDatabase(getActivity(), DATABASE_NAME);
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
            Toast.makeText(getActivity(), Iduser, Toast.LENGTH_LONG).show();
            setUserInformation();
//        ThongTinTaikhoanActivity.context = getApplicationContext();

            //Glide.with(context).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);
            TruyenThongTin();
        }else{
            Toast.makeText(getActivity(), "FAILLLL ", Toast.LENGTH_LONG).show();
        }

    }
    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imageView);

    }
    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent,"select picture"));
    }


}