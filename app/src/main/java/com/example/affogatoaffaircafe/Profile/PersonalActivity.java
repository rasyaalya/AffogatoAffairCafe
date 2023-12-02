package com.example.affogatoaffaircafe.Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.affogatoaffaircafe.Menu.MainActivity;
import com.example.affogatoaffaircafe.R;
import com.example.affogatoaffaircafe.SignUpSignIn.Landing;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class PersonalActivity extends Activity {

    private EditText etName, etEmail, etBirthdate, etPassword, etPhone;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnSave, btnCancel;
    private String phoneNumber; // This will be the phone number used to fetch and update data
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        // Initialize views
        initViews();
        setupBottomNavigationView();

        // Retrieve the phone number from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        phoneNumber = prefs.getString("phone_number", null);

        if (phoneNumber == null) {
            Toast.makeText(this, "Phone number is missing", Toast.LENGTH_LONG).show();
            finish(); // Exit if no phone number
        } else {
            // Fetch user data based on phone number
            new FetchUserDataTask().execute(phoneNumber);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input data
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String birthdate = etBirthdate.getText().toString().trim();
                String password = etPassword.getText().toString();
                String gender = ((RadioButton) findViewById(rgGender.getCheckedRadioButtonId())).getText().toString();
                // Execute AsyncTask to update user data
                new UpdateUserDataTask().execute(phoneNumber, name, email, birthdate, gender, password);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Exit activity
            }
        });

        View btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etBirthdate = findViewById(R.id.etBirthdate);
        etPassword = findViewById(R.id.etPassword);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        bottomNavigationView = findViewById(R.id.nav_view);
    }

    private void setupBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
                    intent.putExtra("phone_number", phoneNumber); // Pass the phone number to PersonalActivity
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.navigation_order) {// TODO: Implement your logic or start new Activity for Order
                    return true;
                } else if (itemId == R.id.navigation_profile) {
                    Intent intent = new Intent(PersonalActivity.this, PersonalActivity.class);
                    intent.putExtra("phone_number", phoneNumber); // Pass the phone number to PersonalActivity
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private class FetchUserDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("http://10.0.2.2/ProjectMobile/getUserData.php?phoneNumber=" + params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);

                conn.connect();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } else {
                    return "Server responded with status code: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject userData = new JSONObject(result);
                etPhone.setText(phoneNumber);
                etName.setText(userData.optString("name", ""));
                etEmail.setText(userData.optString("email", ""));
                etBirthdate.setText(userData.optString("birthdate", ""));
                String gender = userData.optString("gender", "");
                if ("male".equalsIgnoreCase(gender)) {
                    rbMale.setChecked(true);
                } else if ("female".equalsIgnoreCase(gender)) {
                    rbFemale.setChecked(true);
                }
            } catch (JSONException e) {
                Toast.makeText(PersonalActivity.this, "Error parsing user data", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private class UpdateUserDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedWriter writer = null;

            try {
                URL url = new URL("http://10.0.2.2/ProjectMobile/updateUserData.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                Map<String, String> data = new HashMap<>();
                data.put("phone_number", params[0]);
                data.put("name", params[1]);
                data.put("email", params[2]);
                data.put("birthdate", params[3]);
                data.put("gender", params[4]);
                data.put("password", params[5]); // Ensure you hash the password in production

                StringBuilder query = new StringBuilder();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    if (query.length() != 0) query.append('&');
                    query.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    query.append('=');
                    query.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }

                writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                writer.write(query.toString());
                writer.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return result.toString();
                } else {
                    return "Server responded with status code: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(PersonalActivity.this, result, Toast.LENGTH_LONG).show();
            // Add logic based on the response from the server
        }
    }

    private void signOut() {
        // Clear session data
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("phone_number");
        editor.apply();

        // Navigate to the LandingActivity
        Intent intent = new Intent(PersonalActivity.this, Landing.class);
        startActivity(intent);
        finish();
    }


    private void clearSession() {
        // Example of clearing session data using SharedPreferences
        SharedPreferences preferences = getSharedPreferences("YourPreferenceName", MODE_PRIVATE);
        SharedPreferences.Editor editor = ((SharedPreferences) preferences).edit();
        editor.remove("keyForSavedData"); // Use the actual key you use to save session data
        editor.apply();
    }
}
