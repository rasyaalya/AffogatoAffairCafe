package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.affogatoaffaircafe.R;

public class Verification extends AppCompatActivity {

    private EditText editTextCode1, editTextCode2, editTextCode3, editTextCode4;
    private Button buttonVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        editTextCode1 = findViewById(R.id.editTextCode1);
        editTextCode2 = findViewById(R.id.editTextCode2);
        editTextCode3 = findViewById(R.id.editTextCode3);
        editTextCode4 = findViewById(R.id.editTextCode4);
        buttonVerify = findViewById(R.id.buttonVerify);

        String phoneNumber = getIntent().getStringExtra("phone_number");

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (areAllFieldsFilled()) {
                    // All fields are filled, proceed with whatever verification logic you have
                    // For now, we'll assume the verification is successful and move to the PersonalInfo activity
                    Intent intent = new Intent(Verification.this, PersonalInfo.class);
                    intent.putExtra("phone_number", phoneNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(Verification.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean areAllFieldsFilled() {
        if (TextUtils.isEmpty(editTextCode1.getText().toString().trim())) {
            editTextCode1.setError("Must be filled");
            return false;
        }
        if (TextUtils.isEmpty(editTextCode2.getText().toString().trim())) {
            editTextCode2.setError("Must be filled");
            return false;
        }
        if (TextUtils.isEmpty(editTextCode3.getText().toString().trim())) {
            editTextCode3.setError("Must be filled");
            return false;
        }
        if (TextUtils.isEmpty(editTextCode4.getText().toString().trim())) {
            editTextCode4.setError("Must be filled");
            return false;
        }
        return true;
    }
}
