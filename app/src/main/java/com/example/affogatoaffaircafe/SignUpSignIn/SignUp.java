package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.affogatoaffaircafe.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignUp extends AppCompatActivity {

    private EditText edtPhone;
    private CheckBox checkBoxTerms;
    private TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtPhone = findViewById(R.id.editTextPhone);
        checkBoxTerms = findViewById(R.id.checkboxTermsConditions);
        signInText = findViewById(R.id.textViewSignInPrompt);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = edtPhone.getText().toString().trim();
                boolean isEmptyFields = false;

                // Check if the phone number is empty
                if (TextUtils.isEmpty(phoneNumber)) {
                    isEmptyFields = true;
                    edtPhone.setError("Phone number must be filled");
                }

                // Check if the terms and conditions are accepted
                if (!checkBoxTerms.isChecked()) {
                    isEmptyFields = true;
                    Toast.makeText(SignUp.this, "You must agree to the terms and conditions", Toast.LENGTH_LONG).show();
                }

                if (!isEmptyFields) {
                    // Proceed to send data to server and then to verification activity
                    sendDataToServer(phoneNumber);
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the Sign In activity
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

    private void sendDataToServer(final String phoneNumber) {
        // AsyncTask to perform network operation in background
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://10.0.2.2/ProjectMobile/signup.php"); // URL to your PHP script
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Data parameters to send to the PHP script
                    String data = "phone_number=" + URLEncoder.encode(phoneNumber, "UTF-8");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();
                        return response.toString();
                    } else {
                        Log.e("SignUp", "HTTP error code: " + responseCode);
                        return "Server returned non-OK status: " + responseCode;
                    }
                } catch (Exception e) {
                    Log.e("SignUp", "Error in sendDataToServer", e);
                    return "Error sending data to server: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("SignUp", "Response from server: " + s);
                if (s != null){ //&& s.contains("User berhasil ditambahkan")) {
                    //Toast.makeText(SignUp.this, "Sign up successful", Toast.LENGTH_LONG).show();

                    // Jika sign up berhasil, navigasi ke Verification Activity
                    Intent intent = new Intent(SignUp.this, Verification.class);
                    intent.putExtra("phone_number", phoneNumber); // Kirim nomor telepon ke PersonalInfo
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(SignUp.this, s, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
