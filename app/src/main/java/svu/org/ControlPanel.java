package svu.org;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ControlPanel extends AppCompatActivity {

    String userID, strRoles ;
    String[] roles;

    Spinner spinner,spinner2;
    ArrayAdapter<CharSequence> adapter, adapter2;

    ListView listView;

    //TextView  TVfullName = (TextView) findViewById(R.id.TVfullName);
    TextView  TVmonthlyCommission, TVrsgistrationDate, TVsalesPersonNumber;

    String[] months_array = {"0", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] years_array =  {"2015", "2016", "2017", "2018", "2019", "2020"};

    String[] mobileArray; //= {"South region=70000","Costal region=5000","Northern rgion=5000","Eastern region=60000", "Lebanon=13500"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

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

        if (!Arrays.asList(roles).contains("Admin")) {
            Button butAdmin = (Button)findViewById(R.id.butAdmin);
            butAdmin.setVisibility(View.INVISIBLE); //To set visible
        } else if (!Arrays.asList(roles).contains("SalesPerson")){
            Button butAddComm = (Button)findViewById(R.id.Submit);
            butAddComm.setVisibility(View.INVISIBLE); //To set visible

            Button report = (Button)findViewById(R.id.button3);
            report.setVisibility(View.INVISIBLE); //To set visible
        }



    }

    public void addCommission(View view) {
        Intent intent = new Intent(this,AddCommission.class);

        intent.putExtra("userID",userID);
        intent.putExtra("roles", strRoles);

        startActivity(intent);

    }

    public void editProfile(View view) {

        HashMap<String, String> map = new HashMap<>();

        map.put("userId", userID);

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/UserManagement/GetUserById");
        httpCall.setParams(map);

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                JSONObject json;
                super.onResponse(response);
                if( response.equals("404")) {
                    json = new JSONObject("{'result':'User has been not founded'}");
                } else {
                    json = new JSONObject(response);
                }
                if (json.has("result")) {
                    Toast.makeText(ControlPanel.this, json.get("result").toString(), Toast.LENGTH_LONG).show();
                } else if (json.has("UserId")) {
                    Intent intent = new Intent(ControlPanel.this, EditMyProfileActivity.class);
                    intent.putExtra("user", response);
                    intent.putExtra("userIDx", userID);
                    intent.putExtra("roles", strRoles);

                    startActivity(intent);

                }
            }
        }.execute(httpCall);

    }

    public void adminPage(View view) {
        Intent intent=new Intent(this,AdminControlPanel.class);

        intent.putExtra("userID",userID);
        intent.putExtra("roles", strRoles);


        startActivity(intent);

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

    public void userReport(View view) {
        Intent intent = new Intent(ControlPanel.this, UserReport.class);
        intent.putExtra("userID",userID);
        intent.putExtra("roles",strRoles);
        startActivity(intent);
    }
}
