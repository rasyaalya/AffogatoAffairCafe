package com.example.affogatoaffaircafe.SignUpSignIn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;

public class PersonalInfo extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextBirthdate, editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroupGender;
    private Button buttonSubmit;
    private RadioButton radioButtonMale, radioButtonFemale;
    private String phoneNumber; // Menambahkan variabel untuk menyimpan nomor telepon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        // Inisialisasi komponen
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextBirthdate = findViewById(R.id.editTextBirthdate);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Terima phone_number dari intent
        phoneNumber = getIntent().getStringExtra("phone_number");
        if(phoneNumber == null || phoneNumber.trim().isEmpty()) {
            Toast.makeText(this, "Phone number is missing", Toast.LENGTH_LONG).show();
            finish(); // Keluar dari activity jika tidak ada phone number
        }

        // DatePicker dialog untuk birthdate
        editTextBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Listener untuk tombol Submit
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllFieldsFilled() && doPasswordsMatch()) {
                    // Proceed with processing the data
                    Toast.makeText(PersonalInfo.this, "All data is valid!", Toast.LENGTH_SHORT).show();
                    showConfirmationDialog();
                    sendDataToServer();
                }
            }
        });
    }

    private boolean isNameValid(String name) {
        return name.matches("[a-zA-Z ]+"); // Regex to ensure name contains only letters and spaces
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Utilizes Android's Patterns
    }

    private boolean areAllFieldsFilled() {
        String name = editTextName.getText().toString().trim();
        if (TextUtils.isEmpty(name) || !isNameValid(name)) {
            editTextName.setError("Please enter a valid name");
            return false;
        }

        String email = editTextEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !isEmailValid(email)) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        }

        if (TextUtils.isEmpty(editTextName.getText().toString().trim())) {
            editTextName.setError("This field is required");
            return false;
        }

        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())) {
            editTextEmail.setError("This field is required");
            return false;
        }

        if (radioGroupGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(editTextBirthdate.getText().toString().trim())) {
            editTextBirthdate.setError("This field is required");
            return false;
        }

        if (TextUtils.isEmpty(editTextPassword.getText().toString().trim())) {
            editTextPassword.setError("This field is required");
            return false;
        }

        if (TextUtils.isEmpty(editTextConfirmPassword.getText().toString().trim())) {
            editTextConfirmPassword.setError("This field is required");
            return false;
        }

        return true;
    }

    private boolean doPasswordsMatch() {
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("The confirmation password does not match");
            return false;
        }

        return true;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                PersonalInfo.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Note: Month is 0 based
                        editTextBirthdate.setText(String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void sendDataToServer() {
        // Ambil data dari form
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String birthdate = editTextBirthdate.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim(); // Harus di-hash sebelum dikirim ke server!
        final String gender = radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale ? "male" : "female";
        final String phone = this.phoneNumber; // Gunakan nomor telepon yang diterima dari SignUp

        // AsyncTask untuk operasi jaringan
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://10.0.2.2/ProjectMobile/personalinfo.php"); // Ganti dengan URL script PHP Anda
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    // Data yang akan dikirim ke server
                    String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") +
                            "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") +
                            "&" + URLEncoder.encode("birthdate", "UTF-8") + "=" + URLEncoder.encode(birthdate, "UTF-8") +
                            "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") +
                            "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8") +
                            "&" + URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) { //&& s.contains("Informasi pengguna berhasil diperbarui")) {
                    // Navigasi ke SignUpSuccess activity
                    Intent intent = new Intent(PersonalInfo.this, SignUpSuccess.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PersonalInfo.this, "Failed to connect to server", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to register?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Kirim data ke server setelah konfirmasi
                        sendDataToServer();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "No" button, dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
