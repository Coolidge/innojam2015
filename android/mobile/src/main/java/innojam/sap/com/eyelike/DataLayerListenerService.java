package innojam.sap.com.eyelike;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by I309235 on 19/02/2015.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static final boolean D = true;
    private static final String TAG = "WearListener";

    @Override
    public void onCreate() {
        if (D) Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (D) Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onPeerConnected(Node peer) {
        if (D) Log.d(TAG, "onPeerConnected");
        super.onPeerConnected(peer);
        if (D) Log.d(TAG, "Connected: name=" + peer.getDisplayName() + ", id=" + peer.getId());
    }

    @Override
    public void onMessageReceived(MessageEvent m) {
        if (D) Log.d(TAG, "onMessageReceived: " + m.getPath());
        if (m.getPath().equals("start")) {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        // i don't care
    }
}