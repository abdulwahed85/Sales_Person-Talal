package svu.org;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AdminControlPanel extends AppCompatActivity {

    //a List of type hero for holding list items
    List<User> heroList;

    //the listview
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_panel);

        //initializing objects
        heroList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewUsers);

        //adding some values to our list

        heroList.add(new User( "Joker"));
        heroList.add(new User( "Iron Man"));
        heroList.add(new User( "Doctor Strange"));
        heroList.add(new User( "Captain America"));
        heroList.add(new User( "Batman"));

        //creating the adapter
        UserListAdapter adapter = new UserListAdapter(this, R.layout.custome_user_list, heroList);

        //attaching adapter to the listview
        listView.setAdapter(adapter);
    }
}
