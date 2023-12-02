package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.example.affogatoaffaircafe.R;

public class splashscreen extends AppCompatActivity {
    private int waktu_loading=5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setelah loading maka akan langsung berpindah
                Intent home=new Intent(splashscreen.this, Landing.class);
                startActivity(home);
                finish();
            }
        },waktu_loading);
    }
}