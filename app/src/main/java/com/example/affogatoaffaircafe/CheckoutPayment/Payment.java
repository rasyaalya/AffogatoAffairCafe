package com.example.affogatoaffaircafe.CheckoutPayment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.affogatoaffaircafe.R;
import com.example.affogatoaffaircafe.Menu.Menu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    RadioButton radioMastercard, radioGopay, radioDana, radioOvo, radioShopeepay, radioBlu;
    Button buttonConfirm;
    private String phoneNumber;
    private ArrayList<Menu> cartItems; // Use ArrayList<Menu> if Menu implements Parcelable
    private double totalPrice;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1); // -1 as a default value if not found

        // Retrieve the phone number, cart items, and total price from the intent
        Intent intent = getIntent();
        cartItems = intent.getParcelableArrayListExtra("CartItems");
        phoneNumber = intent.getStringExtra("phone_number");
        totalPrice = intent.getDoubleExtra("total_price", 0);

        initializeUI();
        setupListeners();
    }

    private void initializeUI() {
        radioMastercard = findViewById(R.id.radio_mastercard);
        radioGopay = findViewById(R.id.radio_gopay);
        radioDana = findViewById(R.id.radio_dana);
        radioOvo = findViewById(R.id.radio_ovo);
        radioShopeepay = findViewById(R.id.radio_shopeepay);
        radioBlu = findViewById(R.id.radio_blu);
        buttonConfirm = findViewById(R.id.button_confirm);
    }

    private void setupListeners() {
        View.OnClickListener radioButtonListener = v -> {
            RadioButton rb = (RadioButton) v;
            clearRadioButtons();
            rb.setChecked(true);
        };

        radioMastercard.setOnClickListener(radioButtonListener);
        radioGopay.setOnClickListener(radioButtonListener);
        radioDana.setOnClickListener(radioButtonListener);
        radioOvo.setOnClickListener(radioButtonListener);
        radioShopeepay.setOnClickListener(radioButtonListener);
        radioBlu.setOnClickListener(radioButtonListener);

        buttonConfirm.setOnClickListener(v -> {
            if (!isAnyRadioButtonChecked()) {
                Toast.makeText(Payment.this, "Please choose a payment method", Toast.LENGTH_SHORT).show();
            } else {
                String selectedPaymentMethod = getSelectedPaymentMethod();
                sendOrderToServer(userId, phoneNumber, cartItems, totalPrice, selectedPaymentMethod);
                proceedToPaymentSuccess();
            }
        });
    }

    private boolean isAnyRadioButtonChecked() {
        return radioMastercard.isChecked() || radioGopay.isChecked() ||
                radioDana.isChecked() || radioOvo.isChecked() ||
                radioShopeepay.isChecked() || radioBlu.isChecked();
    }

    private void clearRadioButtons() {
        radioMastercard.setChecked(false);
        radioGopay.setChecked(false);
        radioDana.setChecked(false);
        radioOvo.setChecked(false);
        radioShopeepay.setChecked(false);
        radioBlu.setChecked(false);
    }

    private String getSelectedPaymentMethod() {
        if (radioMastercard.isChecked()) return "Mastercard";
        if (radioGopay.isChecked()) return "Gopay";
        if (radioDana.isChecked()) return "Dana";
        if (radioOvo.isChecked()) return "Ovo";
        if (radioShopeepay.isChecked()) return "Shopeepay";
        if (radioBlu.isChecked()) return "Blu";
        return "Unknown"; // Default or error case
    }

    private void proceedToPaymentSuccess() {
        Intent intent = new Intent(Payment.this, PaymentSuccessful.class);
        intent.putExtra("phone_number", phoneNumber);
        startActivity(intent);
    }

    private void sendOrderToServer(int userId, String phoneNumber, ArrayList<Menu> cartItems, double totalPrice, String paymentType) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://10.0.2.2/ProjectMobile/insert_order.php"); // Sesuaikan dengan URL server Anda
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("user_id", String.valueOf(userId))
                            .appendQueryParameter("phone_number", phoneNumber)
                            .appendQueryParameter("order_details", convertCartItemsToJson(cartItems))
                            .appendQueryParameter("total_price", String.valueOf(totalPrice))
                            .appendQueryParameter("payment_type", paymentType);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
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
                Toast.makeText(Payment.this, result, Toast.LENGTH_LONG).show();
                if (result.equals("Data successfully saved")) { // Periksa pesan sukses dari server
                    proceedToPaymentSuccess();
                }
            }
        }.execute();
    }

    private String convertCartItemsToJson(ArrayList<Menu> cartItems) {
        // Implementasi konversi cartItems ke JSON string
        // Contoh sederhana, silakan sesuaikan dengan struktur data Menu Anda
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for (int i = 0; i < cartItems.size(); i++) {
            Menu item = cartItems.get(i);
            if (i > 0) jsonBuilder.append(",");
            jsonBuilder.append("{");
            jsonBuilder.append("\"name\":\"").append(item.getNama()).append("\",");
            jsonBuilder.append("\"quantity\":").append(item.getQuantity()).append(",");
            jsonBuilder.append("\"price\":").append(item.getHarga());
            jsonBuilder.append("}");
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}