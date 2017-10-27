package com.example.android.testing.notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.android.testing.notiyActivity.ModelNoty;
import com.example.android.testing.notiyActivity.Notify;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Toast.makeText(this, "nai hora", Toast.LENGTH_SHORT).show();
            }

        }


        Intent intent = new Intent();
        intent.putExtra("extra", 5);
        intent.setAction("com.my.app.onMessageReceived");
        sendBroadcast(intent);

    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), Notify.class);

            //if there is no image
            if (imageUrl.equals("null")) {
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            } else {
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (Exception e) {
        }
    }

}