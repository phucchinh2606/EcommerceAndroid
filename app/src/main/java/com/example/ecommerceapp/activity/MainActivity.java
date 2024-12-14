package com.example.ecommerceapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.LoaiSpAdapter;
import com.example.ecommerceapp.adapter.SanPhamMoiAdapter;
import com.example.ecommerceapp.model.LoaiSp;
import com.example.ecommerceapp.model.SanPhamMoi;
import com.example.ecommerceapp.model.SanPhamMoiModel;
import com.example.ecommerceapp.retrofit.ApiBanHang;
import com.example.ecommerceapp.retrofit.RetrofitClient;
import com.example.ecommerceapp.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbarMain;
    ViewFlipper viewFlipperMain;
    RecyclerView recyclerViewMain;
    NavigationView navigationViewMain;
    ListView listViewMain;
    DrawerLayout drawerLayoutMain;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> listLoaiSp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> listSpMoi;
    SanPhamMoiAdapter spMoiAdt;
    NotificationBadge badge;
    FrameLayout frameLayoutTrangchu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);


        //ánh xạ id
        AnhXa();
        //set actionbar trang chủ
        ActionBar();
        //set chuyển động cho quảng cáo viewfliper
        ActionViewFlipper();
        //kiem tra internet
        if(isConnected(this)) {

            ActionViewFlipper();

            getLoaiSanPham();

            getSpMoi();

            getEventClick();
        }else{
            Toast.makeText(getApplicationContext(),"Không có internet, vui lòng kết nối lại",Toast.LENGTH_LONG).show();
        }



    }

    private void getEventClick() {
        listViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangChu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangChu);
                        break;
                    case 1:
                        Intent dienThoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienThoai.putExtra("loai",1);
                        startActivity(dienThoai);
                        break;
                    case 2:
                        Intent lapTop = new Intent(getApplicationContext(),LaptopActivity.class);
                        lapTop.putExtra("loai",2);
                        startActivity(lapTop);
                        break;
                    case 3:
                        Intent donHang = new Intent(getApplicationContext(),XemDonActivity.class);
                        startActivity(donHang);
                        break;
                }
            }
        });




    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                listSpMoi = sanPhamMoiModel.getResult();
                                spMoiAdt = new SanPhamMoiAdapter(getApplicationContext(),listSpMoi);
                                recyclerViewMain.setAdapter(spMoiAdt);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối được với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                        }
                ));

    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if(loaiSpModel.isSuccess()){
                                listLoaiSp = loaiSpModel.getResult();
                                //khởi tạo adapter
                                loaiSpAdapter = new LoaiSpAdapter(listLoaiSp,getApplicationContext());
                                listViewMain.setAdapter(loaiSpAdapter);
                            }
                        }
                ));

    }

    private void ActionViewFlipper() {
        List<String> listQuangCao = new ArrayList<>();
        listQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-Le-hoi-phu-kien-800-300.png");
        listQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
        listQuangCao.add("https://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");

        for(int i=0;i<listQuangCao.size();i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(listQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperMain.addView(imageView);
            viewFlipperMain.setFlipInterval(3000);
            viewFlipperMain.setAutoStart(true);

            Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
            Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
            viewFlipperMain.setInAnimation(slide_in);
            viewFlipperMain.setOutAnimation(slide_out);
        }
    }

    private void ActionBar() {
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMain.setNavigationIcon(R.drawable.list_w24);
        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutMain.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem =0;
        for(int i=0;i<Utils.listGioHang.size();i++){
            totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
        }

        badge.setText(String.valueOf(totalItem));
    }

    private void AnhXa() {
        toolbarMain = findViewById(R.id.ToolBarMain);
        viewFlipperMain = findViewById(R.id.ViewFlipperMain);
        recyclerViewMain = findViewById(R.id.RecyclerViewMain);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.setHasFixedSize(true);


        listViewMain = findViewById(R.id.ListViewMain);
        navigationViewMain = findViewById(R.id.NavigationViewMain);
        drawerLayoutMain = findViewById(R.id.DrawerLayoutMain);

        badge = findViewById(R.id.menu_soluong_TrangChu);
        frameLayoutTrangchu = findViewById(R.id.FrameTrangChu);



        //khởi tạo listloaiSP
        listLoaiSp = new ArrayList<>();
        listSpMoi = new ArrayList<>();

        if(Utils.listGioHang == null){
            Utils.listGioHang = new ArrayList<>();

        }
        else {
            int totalItem =0;
            for(int i=0;i<Utils.listGioHang.size();i++){
                totalItem = totalItem + Utils.listGioHang.get(i).getSoluong();
            }

            badge.setText(String.valueOf(totalItem));
        }

        frameLayoutTrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });

    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifi!=null&&wifi.isConnected())||(mobile!=null&&mobile.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}