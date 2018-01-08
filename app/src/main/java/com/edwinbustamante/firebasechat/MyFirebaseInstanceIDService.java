package com.edwinbustamante.firebasechat;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by EDWIN on 19/09/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String TAG = "NOTICIAS";

    @Override
    public void onTokenRefresh() {
         super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "token: " + refreshedToken);
    }
}
