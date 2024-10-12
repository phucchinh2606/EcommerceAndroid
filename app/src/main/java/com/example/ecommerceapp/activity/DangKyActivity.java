package com.example.ecommerceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.UserModel;
import com.example.ecommerceapp.retrofit.ApiBanHang;
import com.example.ecommerceapp.retrofit.RetrofitClient;
import com.example.ecommerceapp.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {

    EditText dangki_email,dangki_pass,dangki_cfpass,dangki_phone,dangki_username;
    AppCompatButton btn_dangki;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
    }

    private void initControl() {
        btn_dangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKi();
            }
        });
    }

    private void dangKi() {
        String str_email = dangki_email.getText().toString().trim();
        String str_pass = dangki_pass.getText().toString().trim();
        String str_cfpass = dangki_cfpass.getText().toString().trim();
        String str_mobile = dangki_phone.getText().toString().trim();
        String str_username = dangki_username.getText().toString().trim();
        if(TextUtils.isEmpty(str_email)){
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập pass",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_cfpass)) {
            Toast.makeText(getApplicationContext(),"Bạn chưa xác nhận pass",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập số điện thoại",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(),"Bạn chưa nhập Username",Toast.LENGTH_LONG).show();
        }
        else {
            if(str_pass.equals(str_cfpass)){
                //post data
                compositeDisposable.add(apiBanHang.dangKi(str_email,str_pass,str_username,str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if(userModel.isSuccess()){
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setEmail(str_pass);
                                        Toast.makeText(getApplicationContext(),"Đăng kí thành công",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                }
                        ));
            }
            else {
                Toast.makeText(getApplicationContext(),"Pass chưa khớp, vui lòng nhập lại",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);


        dangki_email = findViewById(R.id.DangKi_Email);
        dangki_username = findViewById(R.id.DangKi_Username);
        dangki_pass = findViewById(R.id.DangKi_Pass);
        dangki_cfpass = findViewById(R.id.DangKi_ConfrimPass);
        btn_dangki = findViewById(R.id.Btn_DangKi);
        dangki_phone = findViewById(R.id.DangKi_Mobile);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}