package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<CoronaItem> coronaItemArrayList;
    private RequestQueue requestQueue;
    private TextView stateName,dailyDeaths,dailyConfirm,dailyReco,dateHeaders,totalDeath,totalConfirm,totalRecovered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateName=findViewById(R.id.stateName);
        dailyConfirm=findViewById(R.id.dailyConfirm);
        dailyDeaths=findViewById(R.id.dailyDeath);
        dailyReco=findViewById(R.id.dailyRecovered);
        dateHeaders=findViewById(R.id.dateHeader);
        totalConfirm=findViewById(R.id.totalConfirm);
        totalRecovered=findViewById(R.id.dailyRecovered);


        recyclerView=findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        coronaItemArrayList=new ArrayList<>();
        requestQueue=Volley.newRequestQueue(this);

        jsonParse();





    }

    private void jsonParse() {
      String url="https://api.covid19india.org/raw_data.json";
      
      final JsonObjectRequest request=new
              JsonObjectRequest((Request.Method.GET url, null , new Response.Listener<JSONObject>()) {
          @Override
          public void onResponse(JSONObject response) {


              try {
                  JSONArray todayAndTotalDataArray = response.getJSONArray("statewise");
                  JSONObject todayAndTotalDataObject = todayAndTotalDataArray.getJSONObject(0);
                  String dailyConfirmed = todayAndTotalDataObject.getString("deltaconfirmed");
                  String dailyDeath = todayAndTotalDataObject.getString("deltadeaths");
                  String dailyRec = todayAndTotalDataObject.getString("deltarecovered");
                  String dataHeader = todayAndTotalDataObject.getString("lastuptadedtime").substring(0, 5);
                  dataHeader = getFormattedDate(dataHeader);


                  dailyConfirm.setText(dailyConfirmed);
                  dailyReco.setText(dailyRec);
                  dailyDeaths.setText(dailyDeath);
                  dateHeaders.setText(dataHeader);


                  String totalDeathsFetched = todayAndTotalDataObject.getString("deaths");
                  String totalRecoveredFetched = todayAndTotalDataObject.getString("recovered");
                  String totalConfirmedFetched = todayAndTotalDataObject.getString("confirmed");


                  totalConfirm.setText(totalConfirmedFetched);
                  totalDeath.setText(totalDeathsFetched);
                  totalRecovered.setText(totalRecoveredFetched);


                  for (int i = 1; i < todayAndTotalDataArray.length(); i++) {

                      JSONObject stateWiseArrayJSONObject = todayAndTotalDataArray.getJSONObject(i);
                      String active = stateWiseArrayJSONObject.getString("active");
                      String death = stateWiseArrayJSONObject.getString("deaths");
                      String recovered = stateWiseArrayJSONObject.getString("recovered");
                      String state = stateWiseArrayJSONObject.getString("state");
                      String confirmed = stateWiseArrayJSONObject.getString("confirmed");
                      String lastUpdated = stateWiseArrayJSONObject.getString("lastupdatedtime");


                      String todayActive = stateWiseArrayJSONObject.getString("deltaconfirmed");
                      String todayDeath = stateWiseArrayJSONObject.getString("deltadeaths");
                      String todayRecovered = stateWiseArrayJSONObject.getString("deltarecovered");


                      CoronaItem coronaItem = new CoronaItem(state, death, active, recovered, confirmed, lastUpdated, todayDeath, todayRecovered, todayActive);

                      coronaItemArrayList.add(coronaItem);


                  }
                  RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, coronaItemArrayList);


              }
              catch (JSONException e){
                  e.printStackTrace();
              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              
          }
      });
        
        
        
        
        
    }
    private String getFormattedDate(String dateHeader){
        switch (dateHeader.substring(3,5)){
            case "01":
                return dateHeader.substring(0,2)+"Jan";
            case "02":
                return dateHeader.substring(0,2)+"Feb";
            case "03":
                return dateHeader.substring(0,2)+"Mart";
            case "04":
                return dateHeader.substring(0,2)+"Apr";
            case "05":
                return dateHeader.substring(0,2)+"May";
            case "06":
                return dateHeader.substring(0,2)+"Jun";
            case "07":
                return dateHeader.substring(0,2)+"Jul";
            case "08":
                return dateHeader.substring(0,2)+"Aug";
            case "09":
                return dateHeader.substring(0,2)+"Sep";
            case "10":
                return dateHeader.substring(0,2)+"Oct";
            case "11":
                return dateHeader.substring(0,2)+"Nov";
            case "12":
                return dateHeader.substring(0,2)+"Dec";
            default:
                return null;




        }
    }
    
    
}