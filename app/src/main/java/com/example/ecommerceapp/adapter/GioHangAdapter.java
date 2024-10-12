package com.example.ecommerceapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.inTerFaces.ImgClickListener;
import com.example.ecommerceapp.model.EventBus.TinhTongEvent;
import com.example.ecommerceapp.model.GioHang;
import com.example.ecommerceapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {

    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GioHang gioHang = gioHangList.get(position);
        holder.itemGioHang_TenSp.setText(gioHang.getTensp());
        holder.itemGioHang_SoLuong.setText(gioHang.getSoluong()+" ");
        Glide.with(context).load(gioHang.getHinhsp()).into(holder.itemGioHang_Img);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.itemGioHang_GiaSp.setText(decimalFormat.format(gioHang.getGiasp()));
        long gia = gioHang.getSoluong() * gioHang.getGiasp();
        holder.itemGioHang_Tong.setText(decimalFormat.format(gia));

        holder.setImgClickListener(new ImgClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if(giatri ==1){
                    if(gioHangList.get(pos).getSoluong()>1){
                        int slmoi = gioHangList.get(pos).getSoluong()-1;
                        gioHangList.get(pos).setSoluong(slmoi);

                        holder.itemGioHang_SoLuong.setText(gioHangList.get(pos).getSoluong()+" ");
                        long gia = gioHangList.get(pos).getSoluong() * gioHang.getGiasp();
                        holder.itemGioHang_Tong.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (gioHangList.get(pos).getSoluong()==1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có chắc chắc muốn xóa sản phẩm này khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.listGioHang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if(giatri == 2){
                    if(gioHangList.get(pos).getSoluong()<11){
                        int slmoi = gioHangList.get(pos).getSoluong()+1;
                        gioHangList.get(pos).setSoluong(slmoi);
                    }
                    holder.itemGioHang_SoLuong.setText(gioHangList.get(pos).getSoluong()+" ");
                    long gia = gioHangList.get(pos).getSoluong() * gioHang.getGiasp();
                    holder.itemGioHang_Tong.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView itemGioHang_Img, img_tru, img_cong;
        TextView itemGioHang_TenSp,itemGioHang_GiaSp,itemGioHang_SoLuong,itemGioHang_Tong;
        ImgClickListener imgClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemGioHang_Img = itemView.findViewById(R.id.ItemGioHang_Img);
            itemGioHang_TenSp = itemView.findViewById(R.id.ItemGioHang_TenSp);
            itemGioHang_GiaSp = itemView.findViewById(R.id.ItemGioHang_GiaSp);
            itemGioHang_SoLuong = itemView.findViewById(R.id.ItemGioHang_SoLuong);
            itemGioHang_Tong = itemView.findViewById(R.id.ItemGioHang_TongThietHai);
            img_tru = itemView.findViewById(R.id.ItemGioHang_Tru);
            img_cong = itemView.findViewById(R.id.ItemGioHang_Cong);

            //event click
            img_cong.setOnClickListener(this);
            img_tru.setOnClickListener(this);


        }

        public void setImgClickListener(ImgClickListener imgClickListener) {
            this.imgClickListener = imgClickListener;
        }

        @Override
        public void onClick(View view) {
            if(view == img_tru){

                imgClickListener.onImageClick(view,getAdapterPosition(),1);

            } else if (view == img_cong) {
                imgClickListener.onImageClick(view,getAdapterPosition(),2);
            }
        }
    }
}
