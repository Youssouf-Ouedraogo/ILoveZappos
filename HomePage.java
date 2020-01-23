package com.manoideveloppers.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static com.manoideveloppers.ilovezappos.NotificationHandler.notificationId;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private NotificationManagerCompat notificationManagerCompat;
    Toolbar toolbar;
    Button button1, button2, button3, button4;
    EditText editText;
    SharedPreferences sharedPreferences ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        toolbar = findViewById(R.id.home);
        toolbar.setTitle("Home Page");

        button1 = findViewById(R.id.history);
        button2 = findViewById(R.id.book);
        button3 = findViewById(R.id.price);
        button4 = findViewById(R.id.save);
        editText =findViewById(R.id.editText);
        sharedPreferences = this.getSharedPreferences("com.manoideveloppers.ilovezappos",MODE_PRIVATE);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.history:
                Intent historyIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(historyIntent);
                break;
            case R.id.book:
                Intent bookIntent = new Intent(getApplicationContext(),BookingPage.class);
                startActivity(bookIntent);
                break;
            case R.id.price:
                button4.setVisibility(View.VISIBLE);
                editText.setVisibility(View.VISIBLE);
                if( sharedPreferences.getString("savePrice",null) !=null){
                    editText.setText(String.valueOf(sharedPreferences.getString("savePrice",null)));
                }
                break;
            case R.id.save:
                String desirePrice = editText.getText().toString();

                if (desirePrice == null || desirePrice.isEmpty())
                    Toast.makeText(getApplicationContext(),"Please input a price",Toast.LENGTH_LONG).show();
                else {
                    sharedPreferences.edit().putString("savePrice", desirePrice).apply();
                    Toast.makeText(getApplicationContext(),"Successfully saved",Toast.LENGTH_LONG).show();
                    editText.setText("");
                }

        }
    }
    public void notification(){
        Notification notification = new NotificationCompat.Builder(this, notificationId)
                .setSmallIcon(R.drawable.price_udate)
                .setContentTitle("Price is currently bellow input")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


            while (true){
                Log.d( "onDestroy: ", "in Loop");
                RequestQueue requestQueue = Volley.newRequestQueue(HomePage.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        "https://www.bitstamp.net/api/v2/ticker_hour/btcusd/",
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String price = response.getString("low");
                            Log.d( "price: ",price);
                            String inputPrice = sharedPreferences.getString("savePrice", null);

                            if (inputPrice !=null){
                                if (Double.parseDouble(price)<= Double.parseDouble(inputPrice)){
                                    notification();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

                );
                requestQueue.add(jsonObjectRequest);

            }


    }
}
