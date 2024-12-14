package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Item;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {

    List<Item> itemList;
    Context context;

    public ChiTietAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txt_ten.setText(item.getTensp()+"");
        holder.txt_soluong.setText("Số lượng: "+item.getSoluong()+"");
        Glide.with(context).load(item.getHinhanh()).into(holder.img_chitiet);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_chitiet;
        TextView txt_ten,txt_soluong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_chitiet = itemView.findViewById(R.id.Item_Img_ChiTiet);
            txt_soluong = itemView.findViewById(R.id.Item_SoLuong_ChiTiet);
            txt_ten = itemView.findViewById(R.id.Item_TenSp_ChiTiet);
        }
    }
}
