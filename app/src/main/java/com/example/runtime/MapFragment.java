package com.example.runtime;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends FragmentActivity implements OnMapReadyCallback {


    GoogleMap map;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

        //Toast.makeText(MapFragment.this,Double.toString(longitude),Toast.LENGTH_LONG).show();

        float zoomLevel = 16.0f; //This goes up to 21

        LatLng latLng = new LatLng(32.109333,34.855499);
        map.addMarker(new MarkerOptions().position(latLng).title("Tel Aviv"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoomLevel));
    }


}
