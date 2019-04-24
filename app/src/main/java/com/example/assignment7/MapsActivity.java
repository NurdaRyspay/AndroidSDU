package com.example.assignment7;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    RadioGroup mapState;

    EditText locName;
    EditText locDes;


    DBHelper dbHelper;
    ArrayList<Marker> markers;


    String to_change = "list";
    ListView location;
    RelativeLayout map_rel;
   /* RelativeLayout list_rel;*/
    TextView mTitle;

    RelativeLayout main_cont_rel;

    ArrayAdapter<String> adapter;


    //Animations
    Animation fadein, fadeout, zoomin, zoomout, zoomin_1;
    String[] markers_arr;

    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = toolbar.findViewById(R.id.toolbar_title);

        zoomin = AnimationUtils.loadAnimation(this,R.anim.zoomin);
        zoomout = AnimationUtils.loadAnimation(this,R.anim.zoomout);
        fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        location = findViewById(R.id.loacation);

        map_rel = findViewById(R.id.map_rel);
        main_cont_rel = findViewById(R.id.main_cont);
        /*list_rel = findViewById(R.id.list_rel);*/

        main_cont_rel.removeView(location);

        dbHelper = new DBHelper(this);
        markers = new ArrayList<>();
        markers = dbHelper.getMarkers();

        /*Log.d("!!!!!!0",markers.size()+"????");*/

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapState = findViewById(R.id.mapState);

        mapState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = mapState.findViewById(checkedId);
                int index = mapState.indexOfChild(radioButton);
                switch (index){
                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;

                }
            }
        });

        markers_arr = new String[markers.size()];
        for (int i = 0; i < markers.size(); i++) {
            markers_arr[i] = markers.get(i).getLocName();
        }


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, markers_arr);
        location.setAdapter(adapter);


    }
    public void updateMarkers(){
        markers = dbHelper.getMarkers();
        ArrayList<String> s = new ArrayList<>();
        for (Marker a :
                markers) {
            s.add(a.getLocName());
        }
        adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,s);
        location.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        adapter.notifyDataSetChanged();
        mTitle.setText("");
        if(to_change == "list") {
            to_change = "place";


            map_rel.startAnimation(fadeout);
            location.startAnimation(zoomin);

            main_cont_rel.removeView(map_rel);
            main_cont_rel.addView(location);

            /*location.setVisibility(View.VISIBLE);
            map_rel.setVisibility(View.INVISIBLE);*/


            item.setTitle("Place");


            location.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MapsActivity.this, "" + position, Toast.LENGTH_SHORT).show();

                    Double lat_list = Double.parseDouble(markers.get(position).getLat());
                    Double lng_list = Double.parseDouble(markers.get(position).getLng());
                    to_change="list";
                    item.setTitle("List");

                    main_cont_rel.removeView(location);
                    main_cont_rel.addView(map_rel);

                    /*location.setVisibility(View.INVISIBLE);
                    map_rel.setVisibility(View.VISIBLE);*/


                    /*mMap.addMarker(new MarkerOptions().position(new LatLng(lat_list,lng_list)).title(name).snippet(des));*/
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat_list,lng_list)));
                    mTitle.setText(markers_arr[position]);
                }
            });

        }else {

            mTitle.setText("");
            to_change="list";
            item.setTitle("List");
            location.startAnimation(fadeout);
            map_rel.startAnimation(zoomin);



           main_cont_rel.removeView(location);
           main_cont_rel.addView(map_rel);

            /*location.setVisibility(View.INVISIBLE);
            map_rel.setVisibility(View.VISIBLE);*/

        }

        return super.onOptionsItemSelected(item);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        for(int i = 0;i<markers.size();i++){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(markers.get(i).getLat()), Double.parseDouble(markers.get(i).getLng())))
                    .title(markers.get(i).getLocName())
                    .snippet(markers.get(i).getLocDes()));
        }

        /*mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MapsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_location, null);
                locName = mView.findViewById(R.id.etLocName);
                locDes = mView.findViewById(R.id.etLocDes);


                final Double lat = latLng.latitude;
                final Double lng = latLng.longitude;

                mBuilder.setView(mView).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = locName.getText().toString();
                        final String des = locDes.getText().toString();
                        Marker marker = new Marker(name,des,lat.toString(),lng.toString());
                        if (name.isEmpty() || des.isEmpty()){
                            Toast.makeText(MapsActivity.this, "Please enter Name and Description", Toast.LENGTH_SHORT).show();
                        }else{
                            dbHelper.inserMarkers(marker);
                            dialog.cancel();

                            mMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet(des));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            updateMarkers();
                        }


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();


                /*
                count++;
                mMap.addMarker(new MarkerOptions().position(latLng).title(count+" Pin"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                */
            }
        });


        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postolCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    Toast.makeText(MapsActivity.this, city, Toast.LENGTH_SHORT).show();
                }catch (IOException e){

                }
            }
        });*/

    }
}
