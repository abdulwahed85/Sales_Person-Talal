package svu.org;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditMyProfileActivity extends AppCompatActivity {

    JSONObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        if (this.getIntent().hasExtra("user")) {
            String str = this.getIntent().getStringExtra("user");
            try {
                user = new JSONObject(str);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
