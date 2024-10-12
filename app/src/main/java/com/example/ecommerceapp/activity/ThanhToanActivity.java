package com.example.ecommerceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.retrofit.ApiBanHang;
import com.example.ecommerceapp.retrofit.RetrofitClient;
import com.example.ecommerceapp.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {

    Toolbar toolbarThanhToan;
    TextView txt_tongThanhToan, txt_SdtThanhToan,txt_emailThanhToan;
    EditText edt_DiaChi;
    AppCompatButton btn_DatHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_thanh_toan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem =0;
        for(int i=0;i<Utils.listGioHang.size();i++){
            totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
        }
    }

    private void initControl() {
        setSupportActionBar(toolbarThanhToan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThanhToan.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien = getIntent().getLongExtra("tongtien",0);
        txt_tongThanhToan.setText(decimalFormat.format(tongtien));
        txt_emailThanhToan.setText(Utils.user_current.getEmail());
        txt_SdtThanhToan.setText(Utils.user_current.getMobile());


        btn_DatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String diachi = edt_DiaChi.getText().toString().trim();
                if(TextUtils.isEmpty(diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_LONG).show();
                }else {
                    //post data
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();

                    Log.d("test",new Gson().toJson(Utils.listGioHang));
                    compositeDisposable.add(apiBanHang.createOder(str_email,str_sdt,String.valueOf(tongtien),id,diachi,totalItem,new Gson().toJson(Utils.listGioHang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        Toast.makeText(getApplicationContext(),"Đặt hàng thành công",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                            ));
                }
            }
        });
    }

    private void initView(){
        toolbarThanhToan = findViewById(R.id.ToolBarThanhToan);
        txt_emailThanhToan = findViewById(R.id.Txt_EmailThanhToan);
        txt_SdtThanhToan = findViewById(R.id.Txt_PhoneThanhToan);
        txt_tongThanhToan = findViewById(R.id.Txt_TongThanhToan);
        edt_DiaChi = findViewById(R.id.Edt_DiaChiThanhToan);
        btn_DatHang = findViewById(R.id.Btn_DatHang);

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}