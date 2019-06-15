package svu.org;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
public class AddCommission extends AppCompatActivity {


    String userID, empNumber, strRoles;
    String userMainRegion = "";
    Spinner spinner,spinner2;
    String[] roles, months_array = {"0", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commission);
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
        TextView TVsalesPersonNumber = (TextView) findViewById(R.id.TVsalesPersonNumber);
        if (this.getIntent().hasExtra("empNumber")) {
            empNumber = this.getIntent().getStringExtra("empNumber");
            TVsalesPersonNumber.setText("SalesPerson Number:    " + empNumber);
        } else {
            TVsalesPersonNumber.setVisibility(View.INVISIBLE);
        }

        if (this.getIntent().hasExtra("userID")) {
            userID = this.getIntent().getStringExtra("userID");

            HashMap<String, String> map = new HashMap<>();
            map.put("UserId", userID);

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.GET);
            httpCall.setUrl("https://esalesperson.azurewebsites.net/api/UserManagement/GetUserById");
            httpCall.setParams(map);



            new HttpRequest() {
                @Override
                protected void onResponse(String response) throws JSONException {
                    JSONObject json;
                    json = new JSONObject(response);

                    if (json.has("FullName")) {
                        TextView TVfullName = (TextView) findViewById(R.id.TVfullName);
                        TVfullName.setText(json.get("FullName").toString());
                        if (json.has("MainRegionId")) {
                            userMainRegion = json.get("MainRegionId").toString();
                            if(userMainRegion.length() > 0) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("id", userMainRegion);

                                HttpCall httpCall = new HttpCall();
                                httpCall.setMethodtype(HttpCall.GET);

                                httpCall.setUrl("https://esalesperson.azurewebsites.net/api/Regions/GetRegion");
                                httpCall.setParams(map);

                                new HttpRequest() {
                                    @Override
                                    protected void onResponse(String response) throws JSONException {
                                        JSONObject json;
                                        json = new JSONObject(response);

                                        if (json.has("RegionName")) {
                                            TextView TVmainRegion = (TextView) findViewById(R.id.TVmainRegion);
                                            TVmainRegion.setText(json.get("RegionName").toString());
                                        } else if (json.has("Message")) {
                                            Toast.makeText(AddCommission.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(AddCommission.this, "Fatal Error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }.execute(httpCall);//
                            }
                        }
                    } else if (json.has("Message")) {
                        Toast.makeText(AddCommission.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddCommission.this, "Fatal Error", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute(httpCall);
        }

        if (this.getIntent().hasExtra("roles")) {
            String strRoles = this.getIntent().getStringExtra("roles");

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

    public void submit(View view) {
        TextView ESouth = (TextView) findViewById(R.id.ESouth);
        TextView ECoast = (TextView) findViewById(R.id.ECoast);
        TextView ENorth = (TextView) findViewById(R.id.ENorth);
        TextView EEast = (TextView) findViewById(R.id.EEast);
        TextView Elebanon = (TextView) findViewById(R.id.Elebanon);

        HashMap<String, String> map = new HashMap<>();
        map.put("UserId", userID);
        map.put("Month", Integer.toString(Arrays.asList(months_array).indexOf(spinner.getSelectedItem())));
        map.put("Year", spinner2.getSelectedItem().toString());

        map.put("SouthSalesAmount", ESouth.getText().toString());
        map.put("CoastalSalesAmount", ECoast.getText().toString());
        map.put("NorthSalesAmount", ENorth.getText().toString());
        map.put("EastSalesAmount", EEast.getText().toString());
        map.put("LebanonSalesAmount", Elebanon.getText().toString());

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/SalesTransaction/Create");
        httpCall.setParams(map);

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                JSONObject json;
                super.onResponse(response);
                if( response.equals("200")) {
                    json = new JSONObject("{'result':'User Report has been added successfully'}");
                } else {
                    json = new JSONObject(response);
                }
                if (json.has("result")) {
                    Toast.makeText(AddCommission.this, json.get("result").toString(), Toast.LENGTH_LONG).show();
                    TextView ESouth = (TextView) findViewById(R.id.ESouth);
                    TextView ECoast = (TextView) findViewById(R.id.ECoast);
                    TextView ENorth = (TextView) findViewById(R.id.ENorth);
                    TextView EEast = (TextView) findViewById(R.id.EEast);
                    TextView Elebanon = (TextView) findViewById(R.id.Elebanon);

                    ESouth.setText(null);
                    ECoast.setText(null);
                    ENorth.setText(null);
                    EEast.setText(null);
                    Elebanon.setText(null);

                } else if (json.has("Message")) {
                    Toast.makeText(AddCommission.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddCommission.this, "Fatal Error", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(httpCall);
    }


}