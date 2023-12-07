package com.example.affogatoaffaircafe.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.affogatoaffaircafe.Menu.CardViewMenuAdapter;
import com.example.affogatoaffaircafe.Menu.ListMenuAdapter;
import com.example.affogatoaffaircafe.Menu.MainActivity;
import com.example.affogatoaffaircafe.R;
import com.example.affogatoaffaircafe.SignUpSignIn.Landing;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout editProfile, changePassword;
    private View signOut;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView rvMenu;
    private ListMenuAdapter listMenuAdapter;
    private CardViewMenuAdapter cardViewMenuAdapter;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.nav_view_profile);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        // Initialize your LinearLayouts
        editProfile = findViewById(R.id.edit_profile);
        changePassword = findViewById(R.id.change_password);
        signOut = findViewById(R.id.sign_out);

        // Set click listeners for each LinearLayout
        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, PersonalActivity.class);
            startActivity(intent);
        });

        changePassword.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, PersonalActivity.class);
            startActivity(intent);
        });

        signOut.setOnClickListener(view -> signOut());

        setupBottomNavigationView();
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
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        intent.putExtra("phone_number", phoneNumber); // Pass the phone number to PersonalActivity
        startActivity(intent);
    }

    private void signOut() {
        // Clear session data
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // This will clear all data in SharedPreferences
        editor.apply();

        // Navigate back to the LandingActivity and clear the back stack
        Intent intent = new Intent(ProfileActivity.this, Landing.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}