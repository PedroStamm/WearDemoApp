package com.example.pedro.weardemoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.nio.ByteBuffer;
import java.util.Date;

public class MobileActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private String START_ACTIVITY_PATH = "/start/WearActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build();
        mGoogleApiClient.connect();
    }

    public void resolveNodes(View view){
        Log.d("Button", "Button has been clicked");
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                for (Node node : getConnectedNodesResult.getNodes()) {
                    Log.d("GetNodes", "Sending message to node "+node.getId());
                    sendMessage(node.getId());
                }
            }
        });
    }

    private void sendMessage(String node) {
        Date date = new Date();
        long cur = date.getTime();
        Wearable.MessageApi.sendMessage(mGoogleApiClient , node , START_ACTIVITY_PATH , longToBytes(cur)).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Log.e("GoogleApi", "Failed to send message with status code: "
                            + sendMessageResult.getStatus().getStatusCode());
                } else {
                    Log.d("GoogleApi", "Message sent");
                }
            }
        });
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE);
        buffer.putLong(x);
        return buffer.array();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d("GoogleApi", "onConnected: " + bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleApi", "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("GoogleApi", "onConnectionFailed: " + connectionResult);
    }

}
