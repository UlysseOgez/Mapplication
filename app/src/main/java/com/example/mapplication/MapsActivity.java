package com.example.mapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private final int PERMISSION_FINE_LOCATION = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        //Pour changer le type de map à afficher on va utilikser la fonction setMapType
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Pour controler le niveau dezoom autorisé dans l'application on va utiliser setMinZoomPreference
        // et setMaxZoomPreference
        mMap.setMaxZoomPreference(18.0f);
        mMap.setMinZoomPreference(6.0f);

        // On definit la position de certaines villes
        LatLng strasbourg = new LatLng(48.581, 7.75);
        LatLng colmar = new LatLng(48.07,7.36);
        LatLng selestat = new LatLng(48.25,7.38);
        LatLng mulhouse = new LatLng(47.75,7.29);

        // On definit des limites qui representent à peu près l'alsace
        LatLngBounds limiteAlsace = new LatLngBounds(new LatLng(47.5f,7.0f), new LatLng(49.0f,8.0f));

        // On ajoute des marqueurs pour certaines villes
        mMap.addMarker(new MarkerOptions()
                .position(strasbourg)
                .title("Marker in Strasbourg")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions()
                .position(colmar)
                .title("Marker in Colmar")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions()
                .position(selestat)
                .title("Marker in Sélestat")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions()
                .position(mulhouse)
                .title("Marker in Mulhouse")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // On déplace la caméra
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(strasbourg,9.0f));
        // On va restreindre les mouvements de la caméra dans les limites déclarées ci dessus
        mMap.setLatLngBoundsForCameraTarget(limiteAlsace);
        //On definit un nouvel objet CameraPosition
//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(colmar)
//                .zoom(15.0f)
//                .bearing(0.0f)
//                .tilt(45.0f)
//                .build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_FINE_LOCATION);
            //Toast.makeText(this, "PERMISSION REFUSE !!!", Toast.LENGTH_LONG).show());
        }
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        if (Geocoder.isPresent())
        {
            Toast.makeText(this, "Geocoder OK !!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Geocoder NON !!!", Toast.LENGTH_LONG).show();
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName("CCI Formation Colmar 68000", 1);
            if(addresses.size() > 0) {
                double latitude= addresses.get(0).getLatitude();
                double longitude= addresses.get(0).getLongitude();

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("CCI Formation Colmar !")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mMap.setMyLocationEnabled(true);
//                    mMap.setOnMyLocationButtonClickListener(this);
//                    mMap.setOnMyLocationClickListener(this);
                    // permission was granted!
                } else {

                }
                return;
            }

        }
    }
}
