package com.example.affogatoaffaircafe.Menu;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.affogatoaffaircafe.CheckoutPayment.Checkout;
import com.example.affogatoaffaircafe.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CardViewMenuAdapter extends RecyclerView.Adapter<CardViewMenuAdapter.CardViewViewHolder> {
    private ArrayList<Menu> listMenu;
    private ArrayList<Menu> cartItems = new ArrayList<>();
    private Snackbar snackbar;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public CardViewMenuAdapter(ArrayList<Menu> list) {
        this.listMenu = list;
    }

    private void updateCartItem(Menu menu, int quantity) {
        for (Menu item : cartItems) {
            if (item.equals(menu)) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    private int getTotalItems() {
        int totalItems = 0;
        for (Menu item : cartItems) {
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    private void updateSnackbar(View view) {
        if (snackbar != null) {
            snackbar.dismiss(); // Dismiss the current snackbar
        }
        snackbar = Snackbar.make(view, "Total order: " + getTotalItems() + " items", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("CHECKOUT", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Checkout.class);
                intent.putParcelableArrayListExtra("CartItems", cartItems);
                view.getContext().startActivity(intent);
            }
        });
        snackbar.show();
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_menu, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {
        Menu menu = listMenu.get(position);

        // Menggunakan Glide untuk memuat gambar dari URL atau path
        Glide.with(holder.itemView.getContext())
                .load(menu.getPicture()) // Menggunakan getPicture() yang sesuai dengan kelas Menu yang diperbarui
                .apply(new RequestOptions().override(350, 550))
                .into(holder.imgPhoto);

        holder.tvName.setText(menu.getNama()); // Menggunakan getNama() yang sesuai dengan kelas Menu yang diperbarui
        holder.tvPrice.setText(menu.getHarga()); // Menggunakan getHarga() yang sesuai dengan kelas Menu yang diperbarui
        holder.tvDetail.setText(menu.getDetail());

        // Initially hide the increment and decrement buttons
        holder.tvQuantity.setVisibility(View.GONE);
        holder.btnIncrement.setVisibility(View.GONE);
        holder.btnDecrement.setVisibility(View.GONE);
        holder.btnTrash.setVisibility(View.GONE);

        holder.btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menampilkan Toast ketika tombol "Wishlist" ditekan
                Toast.makeText(holder.itemView.getContext(), "Wishlist: " + menu.getNama(), Toast.LENGTH_SHORT).show(); // Menggunakan getNama()
            }
        });

        holder.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                currentQuantity++;
                holder.tvQuantity.setText(String.valueOf(currentQuantity));
                updateCartItem(menu, currentQuantity); // Update the cart item with new quantity
                updateSnackbar(view); // Update the snackbar with new quantity
            }
        });

        holder.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    holder.tvQuantity.setText(String.valueOf(currentQuantity));
                    updateCartItem(menu, currentQuantity); // Update the cart item with new quantity
                    updateSnackbar(view); // Update the snackbar with new quantity
                }
            }
        });

        if (menu.isOrdered()) {
            holder.tvQuantity.setVisibility(View.VISIBLE);
            holder.btnIncrement.setVisibility(View.VISIBLE);
            holder.btnDecrement.setVisibility(View.VISIBLE);
            holder.btnTrash.setVisibility(View.VISIBLE);
            holder.tvQuantity.setText(String.valueOf(menu.getQuantity()));
        } else {
            holder.tvQuantity.setVisibility(View.GONE);
            holder.btnIncrement.setVisibility(View.GONE);
            holder.btnDecrement.setVisibility(View.GONE);
            holder.btnTrash.setVisibility(View.GONE);
        }

        holder.btnOrder.setOnClickListener(v -> {
            menu.setOrdered(true);
            notifyItemChanged(position);
        });

        holder.btnTrash.setOnClickListener(v -> {
            menu.setOrdered(false);
            menu.setQuantity(0); // Reset quantity
            removeItemFromCart(menu);
            notifyItemChanged(position);
            updateSnackbar(v); // Perbarui snackbar
        });

        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan item ke 'cartItems' ketika tombol order diklik
                int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
                menu.setQuantity(currentQuantity);
                cartItems.add(menu);

                // On 'Order' button click, show the increment and decrement buttons
                holder.tvQuantity.setVisibility(View.VISIBLE);
                holder.btnIncrement.setVisibility(View.VISIBLE);
                holder.btnDecrement.setVisibility(View.VISIBLE);
                holder.btnTrash.setVisibility(View.VISIBLE);

                Toast.makeText(holder.itemView.getContext(), "Order: " + menu.getNama(), Toast.LENGTH_SHORT).show();

                // Tampilkan Snackbar dengan opsi untuk Checkout
                Snackbar.make(v, "Total order: " + cartItems.size() + " menus", Snackbar.LENGTH_INDEFINITE)
                        .setAction("CHECKOUT", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(view.getContext(), Checkout.class);
                                intent.putParcelableArrayListExtra("CartItems", cartItems);
                                view.getContext().startActivity(intent);
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    static class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDetail, tvPrice, tvQuantity;
        Button btnOrder, btnWishlist;
        ImageButton btnIncrement, btnDecrement, btnTrash;

        CardViewViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_picture);
            tvName = itemView.findViewById(R.id.tv_item_nama);
            tvDetail = itemView.findViewById(R.id.tv_item_detail);
            tvPrice = itemView.findViewById(R.id.tv_item_harga);
            tvQuantity = itemView.findViewById(R.id.tv_quantity); // TextView for quantity
            btnOrder = itemView.findViewById(R.id.btn_set_order);
            btnWishlist = itemView.findViewById(R.id.btn_set_wishlist);
            btnIncrement = itemView.findViewById(R.id.btn_increment); // Button for increment
            btnDecrement = itemView.findViewById(R.id.btn_decrement); // Button for decrement
            btnTrash = itemView.findViewById(R.id.btn_trash);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Menu data);
    }

    private void removeItemFromCart(Menu itemToRemove) {
        cartItems.removeIf(item -> item.equals(itemToRemove));
    }
}