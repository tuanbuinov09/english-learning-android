package com.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private static final int REQUEST_CODE_EXAMPLE = 2;
    //    private View mView;
    private EditText edtNewPassword;
    private EditText edtOldPassword;
    private Button btnChangePassword;
    private ProgressDialog progressDialog;
    private String m_Text = "";
    private String m_Text1 = "";
    private String strEmail="";
    private String strNewPassword="";
    private String strOldPassword="";
    FirebaseUser user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUi();
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOldPassword  = edtOldPassword.getText().toString().trim();
                strNewPassword  = edtNewPassword.getText().toString().trim();
//                String strEmail="";FirebaseUser
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    // String name = user.getDisplayName();
                    strEmail = user.getEmail();
                }
                reAuthentiate(strEmail,strOldPassword);
            }
        });
    }
    public void onClickChangePassword() {
 //       String strOldPassword  = edtOldPassword.getText().toString().trim();
 //       strNewPassword  = edtNewPassword.getText().toString().trim();

//        progressDialog.show();
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            // String name = user.getDisplayName();
//            strEmail = user.getEmail();
//        }

        String finalStrEmail = strEmail;
        //reAuthentiate(finalStrEmail,strOldPassword);+strEmail+"  "+strOldPassword+"  "+strNewPassword
        user.updatePassword(strNewPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassword.this,"Change password successfully",Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();

                        }else{
                            //reAuthentiate(finalStrEmail,strOldPassword);
                            //reAuthentiate1(finalStrEmail,strOldPassword);
                            Toast.makeText(ChangePassword.this,"Password change failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void reAuthentiate(String email,String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
//                .getCredential("user@example.com", "password1234");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            String text = "121212";
                            Intent intent = new Intent(ChangePassword.this,OtpSendActivity.class);
//                            intent.putExtra("strEmail",strEmail);
//                            intent.putExtra("strPassword",text);
                            startActivityForResult(intent, REQUEST_CODE_EXAMPLE);
//                            startActivityForResult(intent, 2);
                            //onClickChangePassword();
                        }else{
                            Toast.makeText(ChangePassword.this,"Mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
    }
    private void reAuthentiate1(String email,String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password);
//                .getCredential("user@example.com", "password1234");

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
//                            String text = "121212";
                            Toast.makeText(ChangePassword.this,"Re auChange password successfully: "+strEmail+"  "+strOldPassword+"  "+strNewPassword,Toast.LENGTH_LONG).show();
                            onClickChangePassword();
                        }else{
                            Toast.makeText(ChangePassword.this,"Cần xác thực lại:" +email +  "  "+password,Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
    }
    private void signin(String email,String password){
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
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
                                    onClickChangePassword();

                                    // hide the progress bar
                                    // if sign-in is successful
                                    // intent to home activity
//                                    Intent intent
//                                            = new Intent(SignInActivity.this,
//                                            MainActivity.class);
//                                    startActivity(intent);
                                } else {
                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Sai Email hoặc mật khẩu!!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        });
    }

    private void initUi(){
        progressDialog = new ProgressDialog(ChangePassword.this);
        edtNewPassword = findViewById(R.id.edt_new_pasword);
        edtOldPassword = findViewById(R.id.edt_old_pasword);
        btnChangePassword =findViewById(R.id.btn_change_password);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
        if(requestCode == REQUEST_CODE_EXAMPLE) {

            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if(resultCode == ChangePassword.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                final String result = data.getStringExtra(OtpSendActivity.EXTRA_DATA);
                signin(strEmail,strOldPassword);
                //reAuthentiate1(strEmail,strOldPassword);
                //onClickChangePassword();
                // Sử dụng kết quả result bằng cách hiện Toast
                //Toast.makeText(this, "Result:111111111 " + result, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Đổi mật khẩu thành công " + result, Toast.LENGTH_LONG).show();
                finish();
            } else {

                Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }
}