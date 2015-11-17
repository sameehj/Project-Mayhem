package com.example.personel.traverlgroup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "X1yqTBGahkcgFVJt4KrfMJJlynfprxu6568Yb2Ej", "4tY7uG8buer54bifTJpL0YsYsCnxsmlgATZPizLP");
//        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//        String mPhoneNumber = tMgr.getLine1Number();
        final EditText phone = (EditText)findViewById(R.id.editText);

        Button button = (Button)findViewById(R.id.button);

        final SharedPreferences settings = getSharedPreferences("phone", Context.MODE_PRIVATE);



        String url = settings.getString("phone", "");

        if(url.length() > 0)
        {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }


        // location


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor edit = settings.edit();
                edit.putString("phone", phone.getText().toString());
                edit.apply();
                ParseLogin(phone.getText().toString());
            }
        });

    }

    void ParseLogin(final String phone) {
        ParseQuery < ParseObject > query = ParseQuery.getQuery("traveler");
        query.whereEqualTo("phone", phone);
        try {
            List<ParseObject> pr = query.find();
            validatePhoneNumber vpn = new validatePhoneNumber(phone);
            if(vpn.valideIt(getContentResolver()) == false)
            {
                return;
            }
            if(pr.size() == 0 ){
                ParseObject traveler = new ParseObject("traveler");
                traveler.put("phone", phone);
                traveler.save();
            }
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

    }

}
