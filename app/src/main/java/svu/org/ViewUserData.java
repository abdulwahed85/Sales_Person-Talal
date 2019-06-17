package svu.org;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ViewUserData extends AppCompatActivity {
    Spinner spinner,spinner2;
    String userID, strRoles, userName;
    String[] roles;

    TextView TVmonthlyCommission, TSouth, TCoastl, TNorth, TEast, TLebanon;

    String[] months_array = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    String[] years_array =  {"2015", "2016", "2017", "2018", "2019", "2020"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_data);

        spinner = (Spinner) findViewById(R.id.spinnerM);
        spinner2 = (Spinner) findViewById(R.id.spinnerY);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Months_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Years_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        if (this.getIntent().hasExtra("userID")) {
            userID = this.getIntent().getStringExtra("userID");
        }

        if (this.getIntent().hasExtra("userName")) {
            userName = this.getIntent().getStringExtra("userName");
        }

        if (this.getIntent().hasExtra("roles")) {
            strRoles = this.getIntent().getStringExtra("roles");

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(strRoles);

                roles = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    roles[i] = jsonArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        TVmonthlyCommission = (TextView) findViewById(R.id.TTotal);
        TSouth = (TextView) findViewById(R.id.TSouth);
        TCoastl = (TextView) findViewById(R.id.TCoastl);
        TNorth = (TextView) findViewById(R.id.TNorth);
        TEast = (TextView) findViewById(R.id.TEast);
        TLebanon = (TextView) findViewById(R.id.TLebanon);

        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userID);

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/SalesTransaction/GetLatestReport");
        httpCall.setParams(map);


        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);

                JSONObject json = new JSONObject(response);

                if (json.has("Message")) {
                    TVmonthlyCommission.setText(json.get("Message").toString());
                } else {
                    TSouth.setText("SouthSalesCommission:    " + json.get("SouthSalesCommission").toString());
                    TCoastl.setText("CoastalSalesCommission:    " + json.get("CoastalSalesCommission").toString());
                    TNorth.setText("NorthSalesCommission:    " + json.get("NorthSalesCommission").toString());
                    TEast.setText("EastSalesCommission:    " + json.get("EastSalesCommission").toString());
                    TLebanon.setText("LebanonSalesCommission:    " + json.get("LebanonSalesCommission").toString());

                    TVmonthlyCommission.setText("Monthly Commission:    " + json.get("TotalMonthlyCommission").toString());

                    spinner.setSelection(Arrays.asList(months_array).indexOf(json.get("Month").toString()));
                    spinner2.setSelection(Arrays.asList(years_array).indexOf(json.get("Year").toString()));
                }
            }
        }.execute(httpCall);


    }

    public void search(View view){
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userID);
        map.put("month", Integer.toString(Arrays.asList(months_array).indexOf(spinner.getSelectedItem())));
        map.put("year", spinner2.getSelectedItem().toString());

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/SalesTransaction/GetReport");
        httpCall.setParams(map);


        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                TVmonthlyCommission = (TextView) findViewById(R.id.TTotal);
                TSouth = (TextView) findViewById(R.id.TSouth);
                TCoastl = (TextView) findViewById(R.id.TCoastl);
                TNorth = (TextView) findViewById(R.id.TNorth);
                TEast = (TextView) findViewById(R.id.TEast);
                TLebanon = (TextView) findViewById(R.id.TLebanon);
                //TVrsgistrationDate = (TextView) findViewById(R.id.TVrsgistrationDate);
                //TVsalesPersonNumber = (TextView) findViewById(R.id.TVsalesPersonNumber);

                JSONObject json = new JSONObject(response);
                if (json.has("Message")) {
                    //TVmonthlyCommission.setText(json.get("Message").toString());
                } else {
                    TSouth.setText("SouthSalesCommission:    " + json.get("SouthSalesCommission").toString());
                    TCoastl.setText("CoastalSalesCommission:    " + json.get("CoastalSalesCommission").toString());
                    TNorth.setText("NorthSalesCommission:    " + json.get("NorthSalesCommission").toString());
                    TEast.setText("EastSalesCommission:    " + json.get("EastSalesCommission").toString());
                    TLebanon.setText("LebanonSalesCommission:    " + json.get("LebanonSalesCommission").toString());

                    TVmonthlyCommission.setText("Monthly Commission:    " + json.get("TotalMonthlyCommission").toString());
                    //TVrsgistrationDate.setText("Rsgistration Date:    " + json.get("RegistrationDate").toString());
                    //TVsalesPersonNumber.setText("SalesPerson Number:    " + json.get("SalePersonNumber").toString());
                }

                //TVfullName;
            }
        }.execute(httpCall);
    }
}
