package com.example.personel.traverlgroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class friends extends AppCompatActivity {


    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    TextView theFact;
    String shareFact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);d

        SharedPreferences prefs = getSharedPreferences("phone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


        Set<String> set = prefs.getStringSet("friends", null);
        if(set == null)
        {
            set = new HashSet<String>();
            editor.putStringSet("key", set);
            editor.commit();
        }
        ListView lv = (ListView)findViewById(R.id.listView);

        String[] lv_arr = (String[]) set.toArray();
        lv.setAdapter(new ArrayAdapter<String>(friends.this,
                android.R.layout.simple_list_item_1, lv_arr));

    }
}
