package svu.org;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText UserName;
    EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (this.getIntent().hasExtra("message")) {
            String message = this.getIntent().getStringExtra("message");
            if(message.length() > 0 ) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                message = "";
            }
        }
    }

    public void login(View view){

        TextView EDTUserName = (TextView) findViewById(R.id.EDTFullName);
        TextView EDTpassword = (TextView) findViewById(R.id.EDTpassword);

        HashMap<String, String> map = new HashMap<>();
            map.put("grant_type", "password");
        map.put("username", EDTUserName.getText().toString());
        map.put("password", EDTpassword.getText().toString());

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/token");
        httpCall.setParams(map);

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                JSONObject json = new JSONObject(response);

                if (json.has("access_token")) {
                    if(json.get("access_token").toString().length() > 0 ) {
                        Intent intent = new Intent(MainActivity.this, ControlPanel.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Username / Password are not correct or maybe user is not exists", Toast.LENGTH_LONG).show();
                    }
                } else if (json.has("error_description")) {
                    Toast.makeText(MainActivity.this, json.get("error_description").toString(), Toast.LENGTH_LONG).show();
                }
            }
        }.execute(httpCall);


    }

    public void register(View view) {
        Intent intent1=new Intent(this,Registration.class);
        startActivity(intent1);

    }

}
