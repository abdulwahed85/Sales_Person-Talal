package svu.org;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void registerNewUser(View view) {
        TextView EDTUserName = (TextView) findViewById(R.id.EDTUserName);
        TextView EDemail = (TextView) findViewById(R.id.EDemail);
        TextView EDTpassword = (TextView) findViewById(R.id.EDTpassword);
        TextView EDTpasswordR = (TextView) findViewById(R.id.EDTpasswordR);
        TextView EDTPhoneNumber = (TextView) findViewById(R.id.EDTPhoneNumber);

        if(EDTpassword.toString() != EDTpasswordR.toString() ) {
            HashMap<String, String> map = new HashMap<>();
            map.put("email", EDemail.getText().toString());
            map.put("password", EDTpassword.getText().toString());
            map.put("confirmPassword", EDTpasswordR.getText().toString());

            HttpCall httpCall = new HttpCall();
            httpCall.setMethodtype(HttpCall.POST);
            httpCall.setUrl("https://esalesperson.azurewebsites.net/api/account/register");
            httpCall.setParams(map);

            new HttpRequest() {
                @Override
                protected void onResponse(String response) throws JSONException {
                    super.onResponse(response);
                    Toast.makeText(Registration.this, response, Toast.LENGTH_LONG).show();
                }
            }.execute(httpCall);

            Toast.makeText(this, "Sucssed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Password & Confirm password are not matched", Toast.LENGTH_LONG).show();
        }
    }


}


