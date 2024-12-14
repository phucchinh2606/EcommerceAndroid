package com.example.ecommerceapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.DonHangAdapter;
import com.example.ecommerceapp.model.DonHang;
import com.example.ecommerceapp.retrofit.ApiBanHang;
import com.example.ecommerceapp.retrofit.RetrofitClient;
import com.example.ecommerceapp.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    RecyclerView recyclerViewXemDon;
    Toolbar toolbarXemDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_xem_don);


        initView();
        initToolbar();
        getOrder();




    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {

                            DonHangAdapter adapter = new DonHangAdapter(getApplicationContext(),donHangModel.getResult());
                            recyclerViewXemDon.setAdapter(adapter);

                        },
                        throwable -> {

                        }
                ));
    }


    private void initToolbar() {
        setSupportActionBar(toolbarXemDon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarXemDon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        recyclerViewXemDon = findViewById(R.id.RecyclerViewXemDon);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewXemDon.setLayoutManager(linearLayoutManager);
        toolbarXemDon = findViewById(R.id.ToolBarXemDon);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}