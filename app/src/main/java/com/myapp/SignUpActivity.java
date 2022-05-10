package com.myapp;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.dtbassethelper.DatabaseAccess;

public class SignUpActivity extends AppCompatActivity {

    TextView tvDangNhap;
    EditText edtHoTen,edtEmail,edtSdt,edtMatKhau,edtXacNhan;
    Button btnSignUp;
    FirebaseAuth mAuth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseAccess DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AnhXa();
        mAuth = FirebaseAuth.getInstance();

        DB =  DatabaseAccess.getInstance(getApplicationContext());
        tvDangNhap = (TextView) findViewById(R.id.textView_login);
        tvDangNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = edtHoTen.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String sdt = edtSdt.getText().toString().trim();
                String matkhau = edtMatKhau.getText().toString().trim();
                String xacnhanmatkhau = edtXacNhan.getText().toString().trim();

                if(hoten.equals("")||email.equals("")||sdt.equals("")||matkhau.equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "Điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(matkhau.equals(xacnhanmatkhau)){

                        Boolean kiemtrataikhoan = DB.checktaikhoan(email);
                        if(kiemtrataikhoan == false)
                        {


                            mAuth
                                    .createUserWithEmailAndPassword(email, matkhau)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task)
                                        {
                                            if (task.isSuccessful()) {
                                                DB.open();
                                                Boolean insert = DB.insertData(mAuth.getCurrentUser().getUid(),hoten,email,sdt,0);
                                                DB.close();
                                                btnSignUp.setText(insert.toString());
                                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                                // if the user created intent to login activity
                                                rootNode= FirebaseDatabase.getInstance();
                                                reference= rootNode.getReference("User");
                                                User newuser = new User(mAuth.getCurrentUser().getUid(), hoten,0,email,sdt);
                                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newuser);

                                                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                                                startActivity(intent);
                                            }
                                            else {

                                                Toast.makeText(SignUpActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


//                            if(insert == true)
//                            {
//                                btnSignUp.setText(insert.toString());
//                                Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
//                                startActivity(intent);
//                            }
//                            else{
//                                Toast.makeText(Signup.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        }


                    }
                    else{
                        Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        edtMatKhau.setText("");
                        edtXacNhan.setText("");
                    }
                }
            }
        });

    }
    private void AnhXa()
    {
        tvDangNhap = (TextView) findViewById(R.id.textView_login);
        edtHoTen = (EditText) findViewById(R.id.editTextEmailNav);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtSdt = (EditText) findViewById(R.id.editTextSdt);
        edtMatKhau= (EditText) findViewById(R.id.editTextMatKhau);
        edtXacNhan = (EditText) findViewById(R.id.editTextXacNhan);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp);
    }
}
//public class SignUpActivity extends AppCompatActivity {
//
//    private EditText edtEmail,edtPassword;
//    private Button btnSignUp;
//    private ProgressDialog progressDialog;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);
//        initUI();
//        initListener();
//
//    }
//
//    private void initListener() {
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickSignUp();
//
//            }
//        });
//    }
//
//    private void onClickSignUp() {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        String email = edtEmail.getText().toString().trim();
//        String password = edtPassword.getText().toString().trim();
//
//        progressDialog.show();
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        progressDialog.dismiss();
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
//                            startActivity(intent);
//                            finishAffinity();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//
//                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }
//
//    private void initUI(){
//        edtEmail = findViewById(R.id.edt_email);
//        edtPassword = findViewById(R.id.edt_password);
//        btnSignUp = findViewById(R.id.btn_sign_up);
//
//        progressDialog = new ProgressDialog(this);
//    }
//}