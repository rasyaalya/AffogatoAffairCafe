package com.example.affogatoaffaircafe.CheckoutPayment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.affogatoaffaircafe.Menu.MainActivity;
import com.example.affogatoaffaircafe.R;

public class PaymentSuccessful extends AppCompatActivity {

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);

        // Retrieve the phone number from the intent
        phoneNumber = getIntent().getStringExtra("phone_number");

        Button backToHomeButton = findViewById(R.id.button_to_home);
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentSuccessful.this, MainActivity.class);
            intent.putExtra("phone_number", phoneNumber); // Pass the phone number back to MainActivity
            startActivity(intent);
        });
    }
}
