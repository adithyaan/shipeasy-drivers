package com.example.adithyaan.guide;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;


public class FireInstanceService extends FirebaseInstanceIdService
{
    public FireInstanceService() {
    }

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("token", "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");


    }
}
