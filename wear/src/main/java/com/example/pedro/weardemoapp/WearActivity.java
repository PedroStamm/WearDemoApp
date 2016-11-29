package com.example.pedro.weardemoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WearActivity extends Activity{

    private TextView mTextView;
    private GoogleApiClient mGoogleApiClient;
    private int notifId;
    private Date date;
    DateFormat timeFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setText(timeFormat.format(date));
            }
        });
    }



    @Override
    public void onResume(){
        super.onResume();
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            notifId = (int) extras.get("notifId");
            long cur = (long) extras.get("date");
            date = new Date();
            date.setTime(cur);
        }
        else{
            Log.d("Intent","Wrong intent");
        }
    }

}
