package com.example.affogatoaffaircafe.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.affogatoaffaircafe.R;
import com.example.affogatoaffaircafe.SignUpSignIn.Landing;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout editProfile, changePassword;
    private View signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize your LinearLayouts
        editProfile = findViewById(R.id.edit_profile);
        changePassword = findViewById(R.id.change_password);
        signOut = findViewById(R.id.sign_out);

        // Set click listeners for each LinearLayout
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PersonalActivity.class);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
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
