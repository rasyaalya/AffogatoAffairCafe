package com.example.affogatoaffaircafe.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.affogatoaffaircafe.Profile.PersonalActivity;
import com.example.affogatoaffaircafe.Profile.ProfileActivity;
import com.example.affogatoaffaircafe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvMenu;
    private ArrayList<Menu> list = new ArrayList<>();
    private ListMenuAdapter listMenuAdapter;
    private CardViewMenuAdapter cardViewMenuAdapter;
    private BottomNavigationView bottomNavigationView;
    private String phoneNumber; // Add this line to hold the phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMenu = findViewById(R.id.rv_menu);
        bottomNavigationView = findViewById(R.id.nav_view);

        // Retrieve the phone number from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        phoneNumber = prefs.getString("phone_number", null);

        setupRecyclerView();
        setupBottomNavigationView();

        MenuData.fetchMenuData(this, new MenuData.VolleyResponseListener() {
            @Override
            public void onResponse(ArrayList<Menu> menuList) {
                list.clear();
                list.addAll(menuList);
                rvMenu.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        rvMenu.setHasFixedSize(true);
        listMenuAdapter = new ListMenuAdapter(list);
        cardViewMenuAdapter = new CardViewMenuAdapter(list);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(listMenuAdapter); // Default to list view
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    showRecyclerList();
                    return true;
                } else if (itemId == R.id.navigation_order) {// TODO: Implement your logic or start new Activity for Order
                    showRecyclerCardView();
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    navigateToProfile();
                    return true;
                }
                return false;
            }
        });
    }

    private void showRecyclerList() {
        rvMenu.setAdapter(listMenuAdapter);
    }

    private void showRecyclerCardView() {
        rvMenu.setAdapter(cardViewMenuAdapter);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra("phone_number", phoneNumber); // Pass the phone number to PersonalActivity
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_list) {
            showRecyclerList();
            return true;
        } else if (itemId == R.id.action_cardview) {
            showRecyclerCardView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
