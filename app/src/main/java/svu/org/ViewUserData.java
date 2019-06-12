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

import java.util.Arrays;
import java.util.HashMap;

public class ViewUserData extends AppCompatActivity {
    Spinner spinner,spinner2;
    String userID, strRoles, userName;
    String[] roles;

    TextView TVmonthlyCommission, TVrsgistrationDate, TSouth, TCoastl, TNorth, TEast, TLebanon;
    TextView  TVsalesPersonNumber;

    String[] months_array = {"0", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

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
                    TVmonthlyCommission.setText(json.get("Message").toString());
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
