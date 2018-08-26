package metaextract.nkm.com.myplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class SendToWear implements GoogleApiClient.ConnectionCallbacks {

    private static SendToWear sendToWear;
    private static String TAG = "SEND-TO-WEAR";
    private GoogleApiClient mGoogleApiClient;
    private Node mNode;

    private SendToWear(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).addConnectionCallbacks(this).build();
        mGoogleApiClient.connect();
    }

    public static synchronized SendToWear getInstance(Context context) {
        if (sendToWear == null) {
            sendToWear = new SendToWear(context.getApplicationContext());
        }
        return sendToWear;
    }

    private boolean validateConnection() {
        if (mGoogleApiClient.isConnected()) {
            return true;
        } else {
            mGoogleApiClient.connect();
        }
        return mGoogleApiClient.isConnected();
    }

    public synchronized void sendMessage(String type, final String message) {
        resolveNode();
        if (mGoogleApiClient != null && validateConnection() && mNode != null) {
            Log.d(TAG, "Message is going to be sent to wear");

            Wearable.MessageApi.sendMessage(mGoogleApiClient, mNode.getId(), type, message.getBytes()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
                    if (sendMessageResult.getStatus().isSuccess()) {
                        Log.e(TAG, "Message successfully sent to wear: " + message);
                    } else {
                        Log.e(TAG, "Message failed to be sent to wear: " + message);
                    }
                }
            });
        }
    }

    private void resolveNode() {
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                // Find the node I want to communicate with
                for (Node node : nodes.getNodes()) {
                    if (node != null && node.isNearby()) {
                        mNode = node;
                        Log.d(TAG, "Sending data to: " + node.getDisplayName());
                    }
                }
                if (mNode == null) {
                    Log.d(TAG, "Failed to send data");
                }
            }
        }) // Returns a set of nods
        ;
    }

    @Override
    // From addConnectionCallbacks
    public void onConnected(@Nullable Bundle bundle) {
        // Which node to connect to
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(@NonNull NodeApi.GetConnectedNodesResult nodes) {
                for (Node node : nodes.getNodes()) {
                    if (node != null && node.isNearby()) {
                        mNode = node;
                        Log.d(TAG, "Connected to:  " + mNode.getDisplayName());
                    }
                }
                if (mNode == null) {
                    Log.d(TAG, "Not connected");
                }
            }
        })
        ;
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}