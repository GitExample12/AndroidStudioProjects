package com.example.android.testing.myact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.testing.R;
import com.example.android.testing.notification.SharedPrefManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.src;
import static com.example.android.testing.R.id.latitude;
import static com.example.android.testing.R.id.longitude;

public class MyLoc extends AppCompatActivity implements OnMapReadyCallback, com.google.android.gms.location.LocationListener {

    GoogleApiClient mGoogleApiClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    TextView lat, lng, add;
    ProgressDialog progressDialog;
    String slat;
    String slng;
    Button sentToServer;
    private static final String URL_REGISTER_DEVICE = "http://192.168.43.127/faltu/ComingRequest.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loc);
        lat = (TextView) findViewById(latitude);
        lng = (TextView) findViewById(longitude);
        Button getloc = (Button) findViewById(R.id.getlocation);
        sentToServer = (Button) findViewById(R.id.toserver);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        sentToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTokenToServer();
            }
        });
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();

            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void showCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                MyLoc.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MyLoc.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(MyLoc.this, "I know you said no, but I'm asking again", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(MyLoc.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        Location currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (currentLocation == null) {
            Toast.makeText(this, "Couldn't connect!", Toast.LENGTH_SHORT).show();
        } else {
            lat.setText(String.valueOf(currentLocation.getLatitude()));
            lng.setText(String.valueOf(currentLocation.getLongitude()));
            slat = String.valueOf(currentLocation.getLatitude());
            slng = String.valueOf(currentLocation.getLongitude());

        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();
        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String mail = SharedPrefManager.getInstance(this).getEmail();
        if (token == null) {
            progressDialog.dismiss();
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(MyLoc.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MyLoc.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("email", mail);
                params.put("lat", slat);
                params.put("lng", slng);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}