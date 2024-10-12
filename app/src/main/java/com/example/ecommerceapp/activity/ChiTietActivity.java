package com.example.ecommerceapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.GioHang;
import com.example.ecommerceapp.model.SanPhamMoi;
import com.example.ecommerceapp.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

import okhttp3.internal.Util;

public class ChiTietActivity extends AppCompatActivity {

    TextView tensp,giasp,mota;
    Button btn_them;
    ImageView img;
    Spinner spinner;
    Toolbar toolbarChiTiet;
    SanPhamMoi sanPhamMoi;
    NotificationBadge badge;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initView();
        ActionToolBar();
        initData();
        initControl();
        
        
    }

    private void initControl() {

        btn_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });

    }

    private void themGioHang() {
        if(Utils.listGioHang.size()>0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            for(int i=0;i<Utils.listGioHang.size();i++){
                if(Utils.listGioHang.get(i).getIdsp()==sanPhamMoi.getId()){
                    Utils.listGioHang.get(i).setSoluong(soluong + Utils.listGioHang.get(i).getSoluong());
                    long gia = Long.parseLong(sanPhamMoi.getGiasp()) * Utils.listGioHang.get(i).getSoluong();
                    Utils.listGioHang.get(i).setGiasp(gia);
                    flag = true;
                }
            }

            if(flag==false){
                long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                Utils.listGioHang.add(gioHang);
            }


        }else {
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString());
            long gia = Long.parseLong(sanPhamMoi.getGiasp())*soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            Utils.listGioHang.add(gioHang);
        }

        int totalItem =0;
        for(int i=0;i<Utils.listGioHang.size();i++){
            totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
        }

        badge.setText(String.valueOf(totalItem));
    }



    private void initData() {

        sanPhamMoi = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(Double.parseDouble(sanPhamMoi.getGiasp()))+"Đ");
        Integer so[] = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterSpin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterSpin);

    }

    private void initView() {
        tensp = findViewById(R.id.TxtTenSp);
        giasp = findViewById(R.id.TxtGiaSp);
        mota = findViewById(R.id.TxtMoTaChiTiet);
        btn_them = findViewById(R.id.Btn_Them);
        spinner = findViewById(R.id.SpinerSp);
        img = findViewById(R.id.ImgChiTiet);
        toolbarChiTiet = findViewById(R.id.ToolBarChiTiet);
        badge = findViewById(R.id.menu_soluong);
        FrameLayout frameLayoutGioHang = findViewById(R.id.FrameGioHang);
        frameLayoutGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGioHang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intentGioHang);

            }
        });
        
        if(Utils.listGioHang!=null){

            int totalItem =0;
            for(int i=0;i<Utils.listGioHang.size();i++){
                totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
            }

            badge.setText(String.valueOf(totalItem));
        }



    }

    private void ActionToolBar() {

        setSupportActionBar(toolbarChiTiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(Utils.listGioHang!=null){

            int totalItem =0;
            for(int i=0;i<Utils.listGioHang.size();i++){
                totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
            }

            badge.setText(String.valueOf(totalItem));
        }

    }
}