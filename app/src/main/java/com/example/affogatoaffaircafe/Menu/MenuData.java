package com.example.affogatoaffaircafe.Menu;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuData {

    public static void fetchMenuData(Context context, final VolleyResponseListener listener) {
        String url = "http://10.0.2.2/ProjectMobile/get_menu.php"; // Sesuaikan dengan URL server Anda
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("menu");
                            ArrayList<Menu> menuList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject menuObject = jsonArray.getJSONObject(i);
                                Menu menu = new Menu();
                                menu.setNama(menuObject.getString("nama"));
                                menu.setHarga(menuObject.getString("harga"));
                                menu.setDetail(menuObject.getString("detail"));
                                menu.setPicture(menuObject.getString("picture"));
                                menuList.add(menu);
                            }

                            listener.onResponse(menuList);

                        } catch (Exception e) {
                            listener.onError("Error parsing JSON data: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError("Error fetching data: " + error.getMessage());
            }
        });

        queue.add(jsonObjectRequest);
    }

    public interface VolleyResponseListener {
        void onResponse(ArrayList<Menu> menuList);
        void onError(String message);
    }
}
