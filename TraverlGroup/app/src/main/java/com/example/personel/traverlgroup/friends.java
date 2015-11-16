package com.example.personel.traverlgroup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
        setContentView(R.layout.activity_friends);

        final SharedPreferences prefs = getSharedPreferences("phone", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();


        Set<String> set = prefs.getStringSet("friends", null);
        if(set == null)
        {
            set = new HashSet<String>();
            editor.putStringSet("friends", set);
            editor.commit();
        }
        ListView lv = (ListView)findViewById(R.id.listView);

        String[] lv_arr = new String[set.size()];
        lv_arr = set.toArray(lv_arr);

        final Context context = this;

        lv.setAdapter(new ArrayAdapter<String>(friends.this,
                android.R.layout.simple_list_item_1, lv_arr));

        Button button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter friend phone:");

// Set up the input
                final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Set<String> set = prefs.getStringSet("friends", null);

                        String friend = input.getText().toString();

                        set.add(friend);
                        editor.putStringSet("friends", set);
                        editor.commit();
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(friends.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Set<String> set = prefs.getStringSet("friends", null);
                        String[] lv_arr = new String[set.size()];
                        lv_arr = set.toArray(lv_arr);
                        set.remove(lv_arr[positionToRemove]);

                        editor.putStringSet("friends", set);
                        editor.commit();
                        finish();
                        startActivity(getIntent());
                    }
                });
                adb.show();
            }
        });
    }
}
