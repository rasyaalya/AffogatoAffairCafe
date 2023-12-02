package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.affogatoaffaircafe.R;

public class SignUpSuccess extends AppCompatActivity {

    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_success);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the SignIn activity
                Intent intent = new Intent(SignUpSuccess.this, SignIn.class);
                startActivity(intent);
            }
        });
    }
}
