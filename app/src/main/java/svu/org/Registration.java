package svu.org;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Registration extends AppCompatActivity {
    String mainRegion = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        final HttpCall[] httpCall = {new HttpCall()};
        httpCall[0].setMethodtype(HttpCall.GET);
        httpCall[0].setUrl("https://esalesperson.azurewebsites.net/api/Regions/GetRegions");


        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                JSONArray json = new JSONArray(response);
                ListView listView;
                ArrayList<Region> list = new ArrayList<Region>();

                listView = findViewById(R.id.LVmainRegions);

                for(int i = 0; i < json.length(); ++ i) {
                    list.add(new Region(((json.getJSONObject(i)).get("RegionId")).toString(), ((json.getJSONObject(i)).get("RegionName")).toString()));

                }

                listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);

                final ArrayAdapter[] adapter = {new ArrayAdapter(Registration.this, android.R.layout.simple_list_item_single_choice, list)};
                listView.setAdapter(adapter[0]);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    String mainRegion1 = "0";
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mainRegion = ((Region) adapter[0].getItem(i)).getId();
                        //Toast.makeText(Registration.this, "Selected -> " + ((Region) adapter.getItem(i)).toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.execute(httpCall[0]);

    }

    public void registerNewUser(View view) {

        TextView EDTFullName = (TextView) findViewById(R.id.EDTFullName);
        TextView EDTemail = (TextView) findViewById(R.id.EDTemail);
        TextView EDTEmpNumber = (TextView) findViewById(R.id.EDTEmpNumber);
        TextView EDTPhoneNumber = (TextView) findViewById(R.id.EDTPhoneNumber);
        TextView EDTpassword = (TextView) findViewById(R.id.EDTpassword);
        TextView EDTpasswordR = (TextView) findViewById(R.id.EDTpasswordR);

        if((EDTpassword.getText().toString()).equals(EDTpasswordR.getText().toString())) {

            if(mainRegion != "0") {


                HashMap<String, String> map = new HashMap<>();

                map.put("email", EDTemail.getText().toString());
                map.put("FullName", EDTFullName.getText().toString());
                map.put("SalePersonNumber", EDTEmpNumber.getText().toString());
                map.put("MainRegionId", mainRegion);
                map.put("PhoneNumber", EDTPhoneNumber.getText().toString());
                map.put("password", EDTpassword.getText().toString());
                map.put("confirmPassword", EDTpasswordR.getText().toString());

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.POST);
                httpCall.setUrl("https://esalesperson.azurewebsites.net/api/account/register");
                httpCall.setParams(map);

                new HttpRequest() {
                    @Override
                    protected void onResponse(String response) throws JSONException {
                        JSONObject json;
                        super.onResponse(response);
                        if( response.equals("200")) {
                            json = new JSONObject("{'result':'User has been created successfully'}");
                        } else {
                            json = new JSONObject(response);
                        }
                        if (json.has("result")) {
                            Intent intent = new Intent(Registration.this, MainActivity.class);
                            intent.putExtra("message", json.get("result").toString());
                            startActivity(intent);
                        } else if (json.has("Message")) {
                            Toast.makeText(Registration.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Registration.this, "Fatal Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute(httpCall);

            } else {
                Toast.makeText(this, "Kindly, select Employee's Main Region", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Password & Confirm password are not matched", Toast.LENGTH_LONG).show();
        }
    }
}


