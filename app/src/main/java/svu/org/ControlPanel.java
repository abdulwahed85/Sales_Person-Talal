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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControlPanel extends AppCompatActivity {

    Spinner spinner,spinner2;
    ListView listView;



    String[] mobileArray;// = {"South region=70000","Costal region=5000","Northern rgion=5000","Eastern region=60000", "Lebanon=13500"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/Regions");


        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                JSONArray json = new JSONArray(response);
                mobileArray = new String[json.length()];
                for(int i = 0; i < json.length(); ++ i) {
                    mobileArray[i] = ((json.getJSONObject(i)).get("RegionName")).toString();

                }

                ArrayAdapter<String> adapter3= new ArrayAdapter<String>(ControlPanel.this, android.R.layout.simple_list_item_1, mobileArray);

                listView.setAdapter(adapter3);
            }
        }.execute(httpCall);
    }

    public void postRegionTest(View view) {
        HashMap<String, String> map = new HashMap<>();
        map.put("RegionName", "Jeddah");

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/Regions/PostRegion/");
        httpCall.setParams(map);

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                JSONObject json = new JSONObject(response);
                Toast.makeText(ControlPanel.this,  json.get("RegionName").toString() + " has been added successfully", Toast.LENGTH_LONG).show();
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
