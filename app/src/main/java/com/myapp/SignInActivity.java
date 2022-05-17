package com.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.myapp.dtbassethelper.DatabaseAccess;

public class SignInActivity extends AppCompatActivity {

    Button btnDangnhap;
    TextView tvDangky, tvforgotPassword;
    EditText edttaikhoan, edtmatkhau;
    DatabaseAccess DB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);
        AnhXa();
        DB = DatabaseAccess.getInstance(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();

        //Đăng nhập thành công chuyển sang MainActivity
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edttaikhoan.getText().toString().trim();
                String matkhau = edtmatkhau.getText().toString().trim();
//                String email = "1@gmail.com";
//                String matkhau = "123456";
//                String email = "1236@gmail.com";
//                String matkhau = "123456";

                // validations for input email and password // check th trong
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(),
                            "Hãy nhập Email của bạn!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(matkhau)) {
                    Toast.makeText(getApplicationContext(),
                            "Hãy nhập mật khẩu của bạn!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // signin existing user

                mAuth.signInWithEmailAndPassword(email, matkhau)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Đăng nhập thành công!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();

                                            DB.iduser = mAuth.getCurrentUser().getUid();
                                            DB.CapNhatUser(mAuth.getCurrentUser().getUid());

                                            DB.setCurrentUserId__OFFLINE(mAuth.getCurrentUser().getUid());
                                            // hide the progress bar
                                            // if sign-in is successful
                                            // intent to home activity
                                            Intent intent
                                                    = new Intent(SignInActivity.this,
                                                    Main.class);
                                            startActivity(intent);
                                        } else {
                                            // sign-in failed
                                            Toast.makeText(getApplicationContext(),
                                                    "Sai Email hoặc mật khẩu!!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                });


//                if(taikhoan.equals("")||matkhau.equals(""))
//                {
//                    Toast.makeText(LoginActivity.this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    DB.open();
//                    //Kiểm tra username password
//                    Boolean kiemtra = DB.checktaikhoanmatkhau(taikhoan,matkhau);
//                    DB.close();
//
//                    if(kiemtra == true)
//                    {
////                        Intent tentaikhoan = new Intent(LoginActivity.this, ThongTinTaikhoanActivity.class);
////                        tentaikhoan.putExtra("username", taikhoan);
//                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
//                    }
//                }


            }
        });
        tvDangky.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                //Intent intent = new Intent(SignInActivity.this, Main.class);
                startActivity(intent);
            }
        });
//
        tvforgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AnhXa() {
        btnDangnhap = (Button) findViewById(R.id.buttonDangnhap);
        tvDangky = (TextView) findViewById(R.id.textView_register);
        tvforgotPassword = (TextView) findViewById(R.id.textView_forgotPassword);
        edttaikhoan = (EditText) findViewById(R.id.editTextUser);
        edtmatkhau = (EditText) findViewById(R.id.editTextPass);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}