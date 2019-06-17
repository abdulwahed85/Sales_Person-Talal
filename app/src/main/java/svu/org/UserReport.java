package svu.org;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class UserReport extends AppCompatActivity {

    String userID, strRoles ;
    String[] roles;

    Spinner spinner,spinner2;
    ArrayAdapter<CharSequence> adapter, adapter2;

    ListView listView;

    //TextView  TVfullName = (TextView) findViewById(R.id.TVfullName);
    TextView TVmonthlyCommission, TVrsgistrationDate, TVsalesPersonNumber ;

    String[] months_array = {"0", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] years_array =  {"2015", "2016", "2017", "2018", "2019", "2020"};

    String[] mobileArray; //= {"South region=70000","Costal region=5000","Northern rgion=5000","Eastern region=60000", "Lebanon=13500"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        if (this.getIntent().hasExtra("message")) {
            String message = this.getIntent().getStringExtra("message");
            if(message.length() > 0 ) {
                Toast.makeText(UserReport.this, message, Toast.LENGTH_LONG).show();
                message = "";
            }
        }

        if (this.getIntent().hasExtra("userID")) {
            userID = this.getIntent().getStringExtra("userID");
            //userID = "e080e402-9040-4078-99e1-81fd86ff3afe";
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



        spinner = (Spinner) findViewById(R.id.spinnerM);
        spinner2 = (Spinner) findViewById(R.id.spinnerY);
        listView= (ListView) findViewById(R.id.LVNews);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this, R.array.Months_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.Years_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

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
                TVmonthlyCommission = (TextView) findViewById(R.id.TVmonthlyCommission);
                TVrsgistrationDate = (TextView) findViewById(R.id.TVrsgistrationDate);
                TVsalesPersonNumber = (TextView) findViewById(R.id.TVsalesPersonNumber);

                JSONObject json = new JSONObject(response);
                if (json.has("Message")) {
                    TVrsgistrationDate.setText(json.get("Message").toString());
                } else {
                    mobileArray = new String[5];
                    mobileArray[0] = "SouthSalesCommission:    " + json.get("SouthSalesCommission").toString();
                    mobileArray[1] = "CoastalSalesCommission:    " + json.get("CoastalSalesCommission").toString();
                    mobileArray[2] = "NorthSalesCommission:    " + json.get("NorthSalesCommission").toString();
                    mobileArray[3] = "EastSalesCommission:    " + json.get("EastSalesCommission").toString();
                    mobileArray[4] = "LebanonSalesCommission:    " + json.get("LebanonSalesCommission").toString();

                    ArrayAdapter<String> adapter3= new ArrayAdapter<String>(UserReport.this, android.R.layout.simple_list_item_1, mobileArray);

                    listView.setAdapter(adapter3);

                    TVmonthlyCommission.setText("Monthly Commission:    " + json.get("TotalMonthlyCommission").toString());
                    TVrsgistrationDate.setText("Rsgistration Date:    " + json.get("RegistrationDate").toString());
                    TVsalesPersonNumber.setText("SalesPerson Number:    " + json.get("SalePersonNumber").toString());


                    spinner.setSelection(Arrays.asList(months_array).indexOf(json.get("Month").toString()));
                    spinner2.setSelection(Arrays.asList(years_array).indexOf(json.get("Year").toString()));
                }
            }
        }.execute(httpCall);
    }

    public void search(View view) {
        //spinner = (Spinner) findViewById(R.id.spinner);
        //spinner2 = (Spinner) findViewById(R.id.spinner2);



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
                TVmonthlyCommission = (TextView) findViewById(R.id.TVmonthlyCommission);
                TVrsgistrationDate = (TextView) findViewById(R.id.TVrsgistrationDate);
                TVsalesPersonNumber = (TextView) findViewById(R.id.TVsalesPersonNumber);

                JSONObject json = new JSONObject(response);
                if (json.has("Message")) {
                    TVrsgistrationDate.setText(json.get("Message").toString());
                } else {
                    mobileArray = new String[5];
                    mobileArray[0] = "SouthSalesCommission:    " + json.get("SouthSalesCommission").toString();
                    mobileArray[1] = "CoastalSalesCommission:    " + json.get("CoastalSalesCommission").toString();
                    mobileArray[2] = "NorthSalesCommission:    " + json.get("NorthSalesCommission").toString();
                    mobileArray[3] = "EastSalesCommission:    " + json.get("EastSalesCommission").toString();
                    mobileArray[4] = "LebanonSalesCommission:    " + json.get("LebanonSalesCommission").toString();

                    ArrayAdapter<String> adapter3= new ArrayAdapter<String>(UserReport.this, android.R.layout.simple_list_item_1, mobileArray);

                    listView.setAdapter(adapter3);

                    TVmonthlyCommission.setText("Monthly Commission:    " + json.get("TotalMonthlyCommission").toString());
                    TVrsgistrationDate.setText("Rsgistration Date:    " + json.get("RegistrationDate").toString());
                    TVsalesPersonNumber.setText("SalesPerson Number:    " + json.get("SalePersonNumber").toString());
                }

                //TVfullName;
            }
        }.execute(httpCall);
    }


}
