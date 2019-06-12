package svu.org;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminControlPanel extends AppCompatActivity {

    String userID, strRoles;
    String[] roles;

    //a List of type hero for holding list items
    List<User> heroList;
    UserListAdapter adapter = null;

    //the listview
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_panel);

        if (this.getIntent().hasExtra("userID")) {
            userID = this.getIntent().getStringExtra("userID");
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

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.GET);
        httpCall.setUrl("https://esalesperson.azurewebsites.net/api/UserManagement");

        new HttpRequest() {
            @Override
            protected void onResponse(String response) throws JSONException {
                super.onResponse(response);
                JSONArray json = new JSONArray(response);

                //initializing objects
                heroList = new ArrayList<>();
                listView = findViewById(R.id.listViewUsers);

                for(int i=0; i < json.length(); ++i) {
                    heroList.add(new User(json.getJSONObject(i).get("UserId").toString(), json.getJSONObject(i).get("FullName").toString(), json.getJSONObject(i).get("SalePersonNumber").toString()));
                }
                //creating the adapter
                adapter = new UserListAdapter(AdminControlPanel.this, R.layout.custome_user_list, heroList);

                //attaching adapter to the listview
                listView.setAdapter(adapter);
            }
        }.execute(httpCall);
    }

    public class UserListAdapter extends ArrayAdapter<User> {
        List<User> heroList;

        private TextView text;

        //activity context
        Context context;

        //the layout resource file for the list items
        int resource;

        //constructor initializing the values
        public UserListAdapter(Context context, int resource, List<User> heroList) {
            super(context, resource, heroList);
            this.context = context;
            this.resource = resource;
            this.heroList = heroList;
        }

        //this will return the ListView Item as a View
        @NonNull
        @Override
        public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {

            //we need to get the view of the xml for our list item
            //And for this we need a layoutinflater
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            //getting the view
            final View view = layoutInflater.inflate(resource, parent, false);

            //getting the view elements of the list from the view
            //ImageView imageView = view.findViewById(R.id.imageView);
            final TextView textViewName = view.findViewById(R.id.textUserId);
            ImageButton imageButtonView= view.findViewById(R.id.viewUser);
            ImageButton imageButtonEdit= view.findViewById(R.id.imageButtonEdit);
            ImageButton imageButtonDelete= view.findViewById(R.id.imageButtonDelete);
            ImageButton imageButtonAddCom = view.findViewById(R.id.imageButtonAddCom);
            //getting the hero of the specified position
            User hero = heroList.get(position);

            //adding values to the list item
            //imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
            textViewName.setText(hero.toString());


            //adding a click listener to the button to remove item from the list
            imageButtonDelete.setOnClickListener(new View.OnClickListener() {
                // @Override
                public void onClick(View view) {
                    //we will call this method to remove the selected value from the list
                    //we are passing the position which is to be removed in the method

                    HttpCall httpCall = new HttpCall();
                    httpCall.setMethodtype(HttpCall.POST);
                    httpCall.setUrl("https://esalesperson.azurewebsites.net/api/UserManagement/DeleteUser?id=" + heroList.get(position).getId());

                    new HttpRequest() {
                        @Override
                        protected void onResponse(String response) throws JSONException {
                            JSONObject json;
                            super.onResponse(response);
                            if( response.equals("200")) {
                                json = new JSONObject("{'result':'User has been deleted successfully'}");
                            } else {
                                json = new JSONObject(response);
                            }
                            if (json.has("result")) {
                                Toast.makeText(AdminControlPanel.this, json.get("result").toString(), Toast.LENGTH_LONG).show();
                                heroList.remove(position);
                                adapter.notifyDataSetChanged();
                            } else if (json.has("Message")) {
                                Toast.makeText(AdminControlPanel.this, json.get("Message").toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AdminControlPanel.this, "Fatal Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }.execute(httpCall);
                }
            });

            imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(context, EditUserData.class);

                    context.startActivity(intent);

                }
            });

            imageButtonAddCom.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(context, AddCommission.class);
                    intent.putExtra("empNumber",heroList.get(position).empNumber);
                    intent.putExtra("userID",heroList.get(position).id);
                    intent.putExtra("roles", strRoles);

                    context.startActivity(intent);

                }
            });

            imageButtonView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent intent = new Intent(context, ViewUserData.class);

                    intent.putExtra("empNumber",heroList.get(position).empNumber);
                    intent.putExtra("userID",heroList.get(position).id);
                    intent.putExtra("userName",heroList.get(position).name);
                    intent.putExtra("roles", strRoles);

                    context.startActivity(intent);

                }
            });


            //finally returning the view
            return view;
        }
    }
}
