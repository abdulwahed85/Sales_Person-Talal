package svu.org;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class UserListAdapter extends ArrayAdapter <User> {
    List<User> heroList;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //we need to get the view of the xml for our list item
        //And for this we need a layoutinflater
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //getting the view
        View view = layoutInflater.inflate(resource, null, false);

        //getting the view elements of the list from the view
        //ImageView imageView = view.findViewById(R.id.imageView);
        TextView textViewName = view.findViewById(R.id.textUserId);
        ImageButton imageButtonView= view.findViewById(R.id.imageButtonView);
        ImageButton imageButtonEdit= view.findViewById(R.id.imageButtonEdit);
        ImageButton imageButtonDelete= view.findViewById(R.id.imageButtonDelete);
        ImageButton imageButtonAddCom = view.findViewById(R.id.imageButtonAddCom);
        //getting the hero of the specified position
        User hero = heroList.get(position);

        //adding values to the list item
        //imageView.setImageDrawable(context.getResources().getDrawable(hero.getImage()));
        textViewName.setText(hero.getName());


        //adding a click listener to the button to remove item from the list
       // buttonDelete.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
                //we will call this method to remove the selected value from the list
                //we are passing the position which is to be removed in the method
               // removeHero(position);
           // }
       // });

        //finally returning the view
        return view;
    }
}
