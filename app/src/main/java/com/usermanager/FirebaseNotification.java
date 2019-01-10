package com.usermanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by aminmekacher on 10.01.19.
 */

public class FirebaseNotification extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void sendNotification(View view) {
        Log.d("Notif", "You called me!");
    }
}
