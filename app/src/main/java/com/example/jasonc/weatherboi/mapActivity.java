package com.example.jasonc.weatherboi;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


//MapActivity initializes a map view based on given latitude/longitude information
public class mapActivity extends AppCompatActivity {

    MapView mMapView;
    GoogleMap googleMap;
    Location location;
    GoogleMap gmap;
    double longi, lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Retrieves location information from the previous activity
        Bundle bundle = getIntent().getExtras();
        longi = bundle.getDouble("longi");
        lat = bundle.getDouble("lat");

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng coordinates = new LatLng(lat,longi);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15)); //sets the map to given location and to a zoom of 15
                mMapView.onResume();
            }});

       //onResume() needed to get the map to display immediately
        onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }
}
