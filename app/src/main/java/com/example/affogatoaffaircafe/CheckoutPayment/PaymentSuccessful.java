package com.example.affogatoaffaircafe.CheckoutPayment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.affogatoaffaircafe.Menu.MainActivity;
import com.example.affogatoaffaircafe.R;

public class PaymentSuccessful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        Button backToHomeButton = findViewById(R.id.button_to_home);
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessful.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
