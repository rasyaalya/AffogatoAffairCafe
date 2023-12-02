package com.example.affogatoaffaircafe.CheckoutPayment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.affogatoaffaircafe.R;
import com.example.affogatoaffaircafe.Menu.Menu; // Import your Menu class

import java.util.ArrayList;

public class Checkout extends AppCompatActivity {

    private LinearLayout itemsLayout;
    private TextView totalTextView;
    private ArrayList<Menu> selectedItems; // This should be passed from your menu activity
    private ArrayList<Menu> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        itemsLayout = findViewById(R.id.item_container);
        totalTextView = findViewById(R.id.total_price);

        cartItems = getIntent().getParcelableArrayListExtra("CartItems");

        if (cartItems != null) {
            for (Menu item : cartItems) {
                addItemView(item);
            }
        }

        // This should be your actual method to get selected items
        selectedItems = getSelectedMenuItems();

        for (Menu item : selectedItems) {
            addItemView(item);
        }

        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close this activity and return to the previous one
                finish();
            }
        });

        Button proceedToPaymentButton = findViewById(R.id.button_proceed);
        proceedToPaymentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Checkout.this, Payment.class);
            startActivity(intent);
        });

        updatePrice();
    }

    private ArrayList<Menu> getSelectedMenuItems() {
        // This method should return the actual list of selected menu items
        // For now, returning an empty list for example purposes
        return new ArrayList<>();
    }

    private void addItemView(Menu item) {
        View itemView = getLayoutInflater().inflate(R.layout.menu_item, itemsLayout, false);

        ImageView itemImage = itemView.findViewById(R.id.img_item_picture);
        TextView itemName = itemView.findViewById(R.id.tv_item_name);
        TextView itemQuantity = itemView.findViewById(R.id.tv_quantity);
        TextView itemPrice = itemView.findViewById(R.id.tv_price);
        ImageButton minusButton = itemView.findViewById(R.id.btn_decrement);
        ImageButton plusButton = itemView.findViewById(R.id.btn_increment);
        ImageButton trashButton = itemView.findViewById(R.id.btn_trash);

        // Set the values from the 'item' object to your views
        itemName.setText(item.getNama());

        // Set the price based on quantity
        double basePrice = Double.parseDouble(item.getHarga());
        int quantity = item.getQuantity();
        double totalPriceForItem = basePrice * quantity;
        itemPrice.setText(String.format("%.2f", totalPriceForItem));

        // Set the item image
        Glide.with(this) // Use 'this' because it's an activity context
                .load(item.getPicture()) // Use the URL or drawable from the 'item'
                .into(itemImage);

        // Set the quantity text
        itemQuantity.setText(String.valueOf(quantity));

        minusButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(itemQuantity.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                itemQuantity.setText(String.valueOf(currentQuantity));
                itemPrice.setText(String.format("%.2f", basePrice * currentQuantity));
                updatePrice();
            }
        });

        plusButton.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(itemQuantity.getText().toString());
            currentQuantity++;
            itemQuantity.setText(String.valueOf(currentQuantity));
            itemPrice.setText(String.format("%.2f", basePrice * currentQuantity));
            updatePrice();
        });

        trashButton.setOnClickListener(v -> {
            removeItemFromCart(item); // Remove item from the cart
            itemsLayout.removeView(itemView); // Remove the view from the layout
            updatePrice(); // Update the total price
        });

        itemsLayout.addView(itemView); // Add the item view to the layout
    }

    private void updatePrice() {
        // Calculate the total price based on the items and their quantities
        double totalPrice = 0;
        for (int i = 0; i < itemsLayout.getChildCount(); i++) {
            View itemView = itemsLayout.getChildAt(i);
            TextView priceView = itemView.findViewById(R.id.tv_price);


            double price = Double.parseDouble(priceView.getText().toString());

            totalPrice += price;
        }
        totalTextView.setText(String.format("%.2f", totalPrice));
    }

    private void removeItemFromCart(Menu itemToRemove) {
        cartItems.removeIf(item -> item.equals(itemToRemove));
        if (cartItems.isEmpty()) {

        }
    }
}