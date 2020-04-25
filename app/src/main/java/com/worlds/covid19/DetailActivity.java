package com.worlds.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private int positionCountry;
    TextView tvCountry, tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths;
    JSONObject jsonObject2;
    Gson gson;
    ValueLineChart valueLineChart;
    ValueLineSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        valueLineChart = findViewById(R.id.barchart);
        series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        Intent intent = getIntent();
        positionCountry = intent.getIntExtra("position", 0);

        getSupportActionBar().setTitle("Details of " + AffectedCountriesActivity.contryModelList.get(positionCountry).getCountry());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        gson = new Gson();
        fetchData();


        tvCountry = findViewById(R.id.tvCountry);
        tvCases = findViewById(R.id.tvCases);
        tvRecovered = findViewById(R.id.tvRecovered);
        tvCritical = findViewById(R.id.tvCritical);
        tvActive = findViewById(R.id.tvActive);
        tvTodayCases = findViewById(R.id.tvTodayCases);
        tvTotalDeaths = findViewById(R.id.tvDeaths);
        tvTodayDeaths = findViewById(R.id.tvTodayDeaths);

        tvCountry.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getCountry());
        tvCases.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getCases());
        tvRecovered.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getRecovered());
        tvCritical.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getCritical());
        tvActive.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getActive());
        tvTodayCases.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getTodayCases());
        tvTotalDeaths.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getDeaths());
        tvTodayDeaths.setText(AffectedCountriesActivity.contryModelList.get(positionCountry).getTodayDeaths());


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/historical/india";


        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("timeline");
                            jsonObject2 = jsonObject1.getJSONObject("cases");
                           /* HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("map", jsonObject2);*/

                            JSONObject songs = jsonObject1.getJSONObject("cases");
                            Iterator x = songs.keys();
                            JSONArray jsonArray = new JSONArray();
                            List<String> arrayList = null;
                            List<Float> arrayList1 = null;
                            arrayList = new ArrayList<>();
                            arrayList1 = new ArrayList<>();

                            while (x.hasNext()) {
                                String key = (String) x.next();
                                jsonArray.put(songs.get(key));

                                arrayList.add(key);


                                //series.addPoint(new ValueLinePoint(arrayList.toString(), 6.2f));

                            }
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("aaa", jsonArray.get(i).toString());
                                Float val= Float.valueOf(jsonArray.get(i).toString()+"f");

                                arrayList1.add(val);


                                series.addPoint(new ValueLinePoint(arrayList.get(i),val));
                            }
                            valueLineChart.addSeries(series);
                            valueLineChart.startAnimation();
                            valueLineChart.setUseCubic(true);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
