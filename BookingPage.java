package com.manoideveloppers.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BookingPage extends AppCompatActivity {
    Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String apiUrl;
    ArrayList <String> bids;
    ArrayList <String> asks;
    ArrayList <String> bidAmount;
    ArrayList <String> askAmount;

    ArrayList <RecyleELements > recyleELements;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        toolbar = findViewById(R.id.table);
        toolbar.setTitle("Order Book");

        recyclerView = findViewById(R.id.recycler);
        apiUrl = "https://www.bitstamp.net/api/v2/order_book/btcusd/";
        recyleELements = new ArrayList<>();
        bids = new ArrayList<>();
        asks = new ArrayList<>();
        bidAmount = new ArrayList<>();
        askAmount = new ArrayList<>();
        button = findViewById(R.id.refreshOrders);

        parser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parser();
            }
        });



    } // end of On create Method



    public void parser (){
        RequestQueue requestQueue = Volley.newRequestQueue(BookingPage.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray ask = response.getJSONArray("asks");
                            JSONArray bid = response.getJSONArray("bids");

                            for (int i = 0; i < ask.length(); i++) {
                                JSONArray  secondJsonArray= ask.getJSONArray(i);
                                String currentAsk = secondJsonArray.getString(0);
                                asks.add(currentAsk);
                                String currentAskAmount= secondJsonArray.getString(0);
                                askAmount.add(currentAskAmount);

                            }

                            for (int index =0; index < bid.length(); index++){
                                JSONArray indexJsonArray = bid.getJSONArray(index);

                                    String currentbid = indexJsonArray.getString(0);
                                    bids.add(currentbid);
                                    String currentBidAmount = indexJsonArray.getString(1);
                                    bidAmount.add(currentBidAmount);

                            }

                            Log.d( "onResponse: ","After the loop");

                            int index =0;
                            while (index < asks.size() && index <bids.size()){
                                recyleELements.add(new RecyleELements(bidAmount.get(index),bids.get(index),asks.get(index),askAmount.get(index)));
                                index++;
                            }

                            setRemainingIndex(index);
                            layoutManager = new LinearLayoutManager(BookingPage.this);

                            adapter = new RecycleAdapter(recyleELements);

                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d( "onErrorResponse: is ", error.toString());

                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }



    //starting setRemainingIndex
    public  ArrayList<RecyleELements> setRemainingIndex(int index){
        if (index == asks.size() && index !=bids.size()){

            while (index <bids.size()){ // Loop through the remaining bids
                recyleELements.add(new RecyleELements(bidAmount.get(index),bids.get(index),"-","-"));
                index++;
            }

        }
        else if (index != asks.size() && index == bids.size()){

            while (index <bids.size()){ //Loop through the remaining asks
                recyleELements.add(new RecyleELements("-","-",asks.get(index),askAmount.get(index)));
                index++;
            }

        }
        return  recyleELements;
    }
    // End of setRemainingIndex


    @Override
    protected void onDestroy() {
        super.onDestroy();

        while (true){
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parser();
        }
    }
}
