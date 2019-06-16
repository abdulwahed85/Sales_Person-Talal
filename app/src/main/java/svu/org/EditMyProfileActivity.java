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

public class EditMyProfileActivity extends AppCompatActivity {

    JSONObject user;
    String mainRegion = "0";
    int mainRegionIndex = -1;


    String UserId, userIDx, strRoles;
    TextView FullName, Email, SalePersonNumber, PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        if (this.getIntent().hasExtra("user")) {
            if(this.getIntent().hasExtra("userIDx")){
                userIDx = this.getIntent().getStringExtra("userIDx");
            }

            if(this.getIntent().hasExtra("roles")){
                strRoles = this.getIntent().getStringExtra("roles");
            }
            String str = this.getIntent().getStringExtra("user");
            try {
                user = new JSONObject(str);
                //{RegistrationDate":"2019-06-13T08:11:38.747","MainRegionId":3,"PersonalPhoto":null,,"UserName":"1234"}
                UserId = user.getString("UserId");
                FullName = findViewById(R.id.EDTFullName);
                Email = findViewById(R.id.EDTemail);
                SalePersonNumber = findViewById(R.id.EDTEmpNumber);
                PhoneNumber = findViewById(R.id.EDTPhoneNumber);

                FullName.setText(user.getString("FullName"));
                Email.setText(user.getString("Email"));
                SalePersonNumber.setText(user.getString("SalePersonNumber"));
                PhoneNumber.setText(user.getString("PhoneNumber"));

                /*
                * "UserId":"cc5ff3a1-5fdb-4a02-914c-976fd2b2b279",
                 "email":"y.bsata@hotmail.com",
                 "FullName":"yasser ahmad",
                 "SalePersonNumber":22,
                 "UserName":12345,
                 "MainRegionId":1,
                 "PhoneNumber":5222222,
                 "PersonalPhoto":null*/

                HttpCall httpCall = new HttpCall();
                httpCall.setMethodtype(HttpCall.GET);
                httpCall.setUrl("https://esalesperson.azurewebsites.net/api/Regions/GetRegions");


                new HttpRequest() {
                    @Override
                    protected void onResponse(String response) throws JSONException {
                        super.onResponse(response);
                        JSONArray json = new JSONArray(response);
                        ArrayList<Region> list = new ArrayList<Region>();



                        ListView listView = findViewById(R.id.LVmainRegions);

                        for (int i = 0; i < json.length(); ++i) {
                            list.add(new Region(((json.getJSONObject(i)).get("RegionId")).toString(), ((json.getJSONObject(i)).get("RegionName")).toString()));
                            if((user.getString("MainRegionId")).equals(((json.getJSONObject(i)).get("RegionId")).toString())) {
                                mainRegionIndex =  i;
                                mainRegion = user.getString("MainRegionId");
                            }
                        }


                        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);


                        final ArrayAdapter[] adapter = {new ArrayAdapter(EditMyProfileActivity.this, android.R.layout.simple_list_item_single_choice, list)};
                        listView.setAdapter(adapter[0]);
                        if(mainRegionIndex > -1){
                            listView.setItemChecked(mainRegionIndex,true);
                        }

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            String mainRegion1 = "0";

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                mainRegion = ((Region) adapter[0].getItem(i)).getId();
                                //Toast.makeText(Registration.this, "Selected -> " + ((Region) adapter.getItem(i)).toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.execute(httpCall);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void save(View view) {
        ListView listView = findViewById(R.id.LVmainRegions);
        HashMap<String, String> map = new HashMap<>();

        map.put("UserId", UserId);
        map.put("email", Email.getText().toString());
        map.put("FullName", FullName.getText().toString());
        map.put("SalePersonNumber", SalePersonNumber.getText().toString());
        map.put("UserName", SalePersonNumber.getText().toString());
        map.put("MainRegionId", mainRegion);
        map.put("PhoneNumber", PhoneNumber.getText().toString());

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/UserManagement/EditUser");
        httpCall.setParams(map);

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                JSONObject json;
                super.onResponse(response);
                if (response.equals("200")) {
                    json = new JSONObject("{'result':'User has been edited successfully'}");
                } else {
                    json = new JSONObject(response);
                }
                if (json.has("result")) {

                    Intent intent;
                    if(userIDx.equals(UserId)) {
                       intent = new Intent(EditMyProfileActivity.this, ControlPanel.class);
                    } else {
                        intent = new Intent(EditMyProfileActivity.this, AdminControlPanel.class);
                    }
                    intent.putExtra("message", json.get("result").toString());
                    intent.putExtra("userID", userIDx);
                    intent.putExtra("roles", strRoles);
                    startActivity(intent);
                } else if (json.has("Message")) {
                    Toast.makeText(EditMyProfileActivity.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EditMyProfileActivity.this, "Fatal Error", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(httpCall);
    }
}
