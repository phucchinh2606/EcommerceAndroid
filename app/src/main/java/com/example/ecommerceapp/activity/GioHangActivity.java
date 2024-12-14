package com.example.ecommerceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.GioHangAdapter;
import com.example.ecommerceapp.model.EventBus.TinhTongEvent;
import com.example.ecommerceapp.model.GioHang;
import com.example.ecommerceapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {

    TextView giohangtrong,tongtien;
    Toolbar toolbarGioHang;
    RecyclerView recyclerViewGioHang;
    Button btnMuaHang;
    GioHangAdapter gioHangAdt;
    List<GioHang> gioHangList;
    long tongTienSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gio_hang);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        initControl();
        TinhTongTien();




    }

    private void TinhTongTien() {
        tongTienSp = 0;
        for(int i=0;i<Utils.listGioHang.size();i++){
            tongTienSp = tongTienSp+Utils.listGioHang.get(i).getGiasp()*Utils.listGioHang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongTienSp));



    }

    private void initControl() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerViewGioHang.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewGioHang.setLayoutManager(layoutManager);
        if(Utils.listGioHang.isEmpty()){
            giohangtrong.setVisibility(View.VISIBLE);
        }
        else {
            gioHangAdt = new GioHangAdapter(getApplicationContext(),Utils.listGioHang);
            recyclerViewGioHang.setAdapter(gioHangAdt);
        }

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongtien",tongTienSp);
                startActivity(intent);
            }
        });



    }

    private void initView() {
        giohangtrong = findViewById(R.id.TxtGioHangTrong);
        toolbarGioHang = findViewById(R.id.ToolBarGioHang);
        recyclerViewGioHang = findViewById(R.id.RecyclerViewGioHang);
        tongtien = findViewById(R.id.TxtTongTien);
        btnMuaHang = findViewById(R.id.BtnMuaHang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if(event!=null){
            TinhTongTien();
        }
    }

}