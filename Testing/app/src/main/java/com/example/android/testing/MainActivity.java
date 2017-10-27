package com.example.android.testing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.testing.myact.MyLoc;
import com.example.android.testing.notification.SharedPrefManager;
import com.example.android.testing.notiyActivity.Notify;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //defining views
    private Button buttonRegister, ShowToken;
    private EditText editTextEmail;
    private ProgressDialog progressDialog;
    TextView tv;
    Toolbar toolbar;
    //URL to RegisterDevice.php
    private static final String URL_REGISTER_DEVICE = "http://192.168.43.127/faltu/RegisterDevice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting views from xml
        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        // toolbar.inflateMenu(R.menu.main);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        ShowToken = (Button) findViewById(R.id.shoe_token_button);
        tv = (TextView) findViewById(R.id.shoe_token_textview);

        //adding listener to view
        buttonRegister.setOnClickListener(this);
        ShowToken.setOnClickListener(this);

        //for notification
    }



    //storing token to mysql server
    private void sendTokenToServer() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        final String token = SharedPrefManager.getInstance(this).getDeviceToken();
        final String email = editTextEmail.getText().toString();
        SharedPrefManager.getInstance(getApplicationContext()).saveEmail(email);

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
                            Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void showNotification(View v) {
        startActivity(new Intent(MainActivity.this, Notify.class));
    }


    @Override
    public void onClick(View view) {
        if (view == buttonRegister) {
            sendTokenToServer();
        }
        if (view == ShowToken) {
            startActivity(new Intent(MainActivity.this, MyLoc.class));
        }
    }
}
