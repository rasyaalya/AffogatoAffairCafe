package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.affogatoaffaircafe.Menu.MainActivity;
import com.example.affogatoaffaircafe.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SignIn extends AppCompatActivity {

    private EditText editTextPhone, editTextPassword;
    private Button buttonSignIn;
    private TextView textViewSignUpPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        textViewSignUpPrompt = findViewById(R.id.textViewSignUpPrompt);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = editTextPhone.getText().toString().trim();
                final String password = editTextPassword.getText().toString().trim();
                if (!phoneNumber.isEmpty() && !password.isEmpty()) {
                    performSignIn(phoneNumber, password);
                } else {
                    Toast.makeText(SignIn.this, "Phone number and password are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewSignUpPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void performSignIn(final String phoneNumber, final String password) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://10.0.2.2/ProjectMobile/signin.php"); // Replace with your actual PHP script URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String postData = URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8") +
                            "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData);
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
                        return "Server returned non-OK status: " + responseCode;
                    }
                } catch (Exception e) {
                    return "Error sending data to server: " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result.equals("Sign in successful")) {
                    // Save phone number to SharedPreferences
                    SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("phone_number", phoneNumber);
                    editor.apply();

                    // Navigate to MainActivity
                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(SignIn.this, result, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}
