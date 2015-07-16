package com.parse.starter;

import android.app.Application;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseCrashReporting;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MyListViewTest extends Activity {

    ArrayList<String> newList = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

//		ParseObject myLittleGladiator = new ParseObject("Gladiator");
//		myLittleGladiator.put("name", "Androidasaurus Rex");
//		myLittleGladiator.saveInBackground();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Gladiator");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> o, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject po : o) {
                        Log.d("MyLog", po.getString("name"));
                        newList.add(po.getString("name"));
                    }
                } else {
                    Log.d("MyLog", e.getMessage());
                }
            }
        });

        //ListView Base Code
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newList);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(MyListViewTest.this, String.valueOf(parent.getItemAtPosition(position)), Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }
}
