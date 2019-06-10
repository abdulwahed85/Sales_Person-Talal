package svu.org;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControlPanel extends AppCompatActivity {

    String userID;
    String[] roles;

    Spinner spinner,spinner2;
    ListView listView;

    //TextView  TVfullName = (TextView) findViewById(R.id.TVfullName);
    TextView  TVmonthlyCommission;
    TextView  TVrsgistrationDate;
    TextView  TVsalesPersonNumber;

    String[] months_array = {"0", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String[] mobileArray; //= {"South region=70000","Costal region=5000","Northern rgion=5000","Eastern region=60000", "Lebanon=13500"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (this.getIntent().hasExtra("userID")) {
            userID = this.getIntent().getStringExtra("userID");
            userID = "e080e402-9040-4078-99e1-81fd86ff3afe";
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

        setContentView(R.layout.activity_control_panel);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        listView= (ListView) findViewById(R.id.LVNews);
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
                    mobileArray = new String[json.length() - 3];
                    mobileArray[0] = "SouthSalesCommission:    " + json.get("SouthSalesCommission").toString();
                    mobileArray[1] = "CoastalSalesCommission:    " + json.get("CoastalSalesCommission").toString();
                    mobileArray[2] = "NorthSalesCommission:    " + json.get("NorthSalesCommission").toString();
                    mobileArray[3] = "EastSalesCommission:    " + json.get("EastSalesCommission").toString();
                    mobileArray[4] = "LebanonSalesCommission:    " + json.get("LebanonSalesCommission").toString();

                    ArrayAdapter<String> adapter3= new ArrayAdapter<String>(ControlPanel.this, android.R.layout.simple_list_item_1, mobileArray);

                    listView.setAdapter(adapter3);

                    TVmonthlyCommission.setText("Monthly Commission:    " + json.get("TotalMonthlyCommission").toString());
                    TVrsgistrationDate.setText("Rsgistration Date:    " + json.get("RegistrationDate").toString());
                    TVsalesPersonNumber.setText("SalesPerson Number:    " + json.get("SalePersonNumber").toString());
                }

                //TVfullName;
            }
        }.execute(httpCall);


    }

    public void addCommission(View view) {
        Intent intent1=new Intent(this,AddCommission.class);
        startActivity(intent1);

    }

    public void adminPage(View view) {
        Intent intent2=new Intent(this,AdminControlPanel.class);
        startActivity(intent2);

    }


    SearchView searchView;
    Menu myMenu;
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.control_menu, menu);
        myMenu=menu;
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //final Context co=this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//code for search
                //OldNewsStatus.OnlyOneRequest=true;
                //loadUrl(query, OldNewsStatus.ToolTypeID, 0, 1, 20);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}
