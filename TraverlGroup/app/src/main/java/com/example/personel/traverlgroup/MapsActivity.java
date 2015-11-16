package com.example.personel.traverlgroup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        long timem = 30000;
        float dis = 5;
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, timem, dis, this);

        Button signout = (Button)findViewById(R.id.radioPopular);

        signout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final SharedPreferences settings = getSharedPreferences("phone", Context.MODE_PRIVATE);
                settings.edit().remove("phone");
                settings.edit().remove("friends");
                finish();
            }
        });

        Button friends2 = (Button)findViewById(R.id.friends);

        friends2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, friends.class);
                startActivity(intent);
            }});

        updateFriends();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        updateFriends();
    }

    public void moveCameraForFriend(String phone){
        mMap.clear();

    }

    @Override
    public void onLocationChanged(Location location) {
        ParseQuery< ParseObject > query = ParseQuery.getQuery("traveler");

        final SharedPreferences settings = getSharedPreferences("phone", Context.MODE_PRIVATE);
        query.whereEqualTo("phone", settings.getString("phone", ""));
        try {


                ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                List<ParseObject> pr = query.find();
                pr.get(0).put("lastLoc", point);
                pr.get(0).save();

        } catch (Exception e) {
           return;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void updateFriends(){
        final SharedPreferences prefs = getSharedPreferences("phone", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("friends", null);
        if (set == null) return;
        for(String friend : set){
            ParseQuery< ParseObject > query = ParseQuery.getQuery("traveler");
            query.whereEqualTo("phone", friend);
            try {
                ParseObject friendParseObject = query.getFirst();
                ParseGeoPoint pgp = friendParseObject.getParseGeoPoint("lastLoc");
                LatLng sydney2 = new LatLng(pgp.getLatitude(), pgp.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney2).title("friends"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney2, 15));
                //              mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2));
            }catch(Exception e){
                int x;
            }
        }

    }

}
