package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.affogatoaffaircafe.R;

public class Landing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        Button buttonSignIn = findViewById(R.id.buttonSignIn);

        // Handle sign up button click
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to SignUpActivity
                Intent intent = new Intent(Landing.this, SignUp.class);
                startActivity(intent);
            }
        });

        // Handle sign in button click
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate to SignInActivity
                Intent intent = new Intent(Landing.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}
