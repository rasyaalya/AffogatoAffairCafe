package com.example.affogatoaffaircafe.Menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.affogatoaffaircafe.R;

import java.util.ArrayList;

public class ListMenuAdapter extends RecyclerView.Adapter<ListMenuAdapter.MenuViewHolder> {
    private ArrayList<Menu> listMenu;
    private OnItemClickCallback onItemClickCallback;

    public ListMenuAdapter(ArrayList<Menu> list) {
        this.listMenu = list;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuViewHolder holder, int position) {
        Menu menu = listMenu.get(position);

        // Menggunakan Glide untuk memuat gambar dari URL atau path
        Glide.with(holder.itemView.getContext())
                .load(menu.getPicture()) // Menggunakan getPicture() yang sesuai dengan kelas Menu yang diperbarui
                .into(holder.imgPhoto);

        holder.tvName.setText(menu.getNama()); // Menggunakan getNama() yang sesuai dengan kelas Menu yang diperbarui
        holder.tvDetail.setText(menu.getDetail());
        holder.tvPrice.setText(menu.getHarga()); // Menggunakan getHarga() yang sesuai dengan kelas Menu yang diperbarui

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickCallback != null) {
                    onItemClickCallback.onItemClicked(listMenu.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDetail, tvPrice;

        MenuViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_picture);
            tvName = itemView.findViewById(R.id.tv_item_nama);
            tvDetail = itemView.findViewById(R.id.tv_item_detail);
            tvPrice = itemView.findViewById(R.id.tv_item_harga);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Menu data);
    }
}
