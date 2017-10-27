package com.example.android.testing.notiyActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.testing.R;
import com.example.android.testing.notification.MyFirebaseMessagingService;
import com.example.android.testing.notification.MyNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class Notify extends AppCompatActivity {

    TextView noty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //get xml view
        noty = (TextView) findViewById(R.id.noty);
        Button b = (Button) findViewById(R.id.bbb);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noty = (TextView) findViewById(R.id.noty);
                noty.setText("4");
            }
        });


    }

    public void updateDisplay(int state) {
        noty = (TextView) findViewById(R.id.noty);
        noty.setText(String.valueOf(state));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.my.app.onMessageReceived");
        MyBroadcastRece receive = new MyBroadcastRece();
        registerReceiver(receive, intentFilter);
    }

    private class MyBroadcastRece extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extra = intent.getExtras();
            int state = extra.getInt("extra");
            updateDisplay(state);
            Toast.makeText(Notify.this, String.valueOf(state), Toast.LENGTH_LONG).show();
        }
    }
}