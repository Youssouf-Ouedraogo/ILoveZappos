package com.manoideveloppers.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button button;
    private LineChart lineChart;
    String apiUrl;
    ArrayList <String> date;
    ArrayList <String> price;
    ArrayList <Entry> coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.grah);
        toolbar.setTitle("Transaction History");
        button = findViewById(R.id.refresh);


        lineChart = findViewById(R.id.chart1);
        date = new ArrayList<>();
        price = new ArrayList<>();
        coordinates = new ArrayList<>();

        apiUrl = "https://www.bitstamp.net/api/v2/transactions/btcusd/";
        parse();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parse();
            }
        });



    }
    private void parse (){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for (int i =0; i <response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                date.add(jsonObject.getString("date"));
                                price.add(jsonObject.getString("price"));

                                int x = Integer.parseInt(jsonObject.getString("date"));
                                int y = (int) Double.parseDouble(jsonObject.getString("price"));
                                coordinates.add(new Entry(x,y));
                            }

                            grapher(coordinates);

                            Log.d( "date: ", date.toString());
                            Log.d( "price: ", price.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d( "onErrorResponse: ",error.toString());
                    }
                }

        );
        requestQueue.add(jsonArrayRequest);
    }

    private void grapher (ArrayList <Entry>coordinates){
        LineDataSet lineDataSet = new LineDataSet(coordinates,"Price over time");
        ArrayList <ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setAvoidFirstLastClipping(true);
        lineChart.getAxisLeft().setInverted(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setDrawLabels(false);
        lineChart.getLegend().setForm(Legend.LegendForm.LINE);
        lineChart.invalidate();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        while (true){
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            parse();
        }
    }
}
