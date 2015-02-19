package innojam.sap.com.eyelike;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONObject;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private static final boolean D = true;
    private static final String TAG = "EyeLike";
    private GoogleApiClient mGoogleApiClient = null;
    private Node mWearableNode = null;
    private ConnectivityManager connMgr;
    private static final String BACKEND_URL = "https://eyelikei305845trial.hanatrial.ondemand.com/EyeLikeServlet/MainServlet";

    private MessageApi.MessageListener mMessageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent m) {
            if (D) Log.d(TAG, "onMessageReceived: " + m.getPath());
            Scanner s = new Scanner(m.getPath());
            String command = s.next();
            if (command.equals("stop")) {
                moveTaskToBack(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (D) Log.d(TAG, "onConnected: " + bundle);
                        findWearableNode();
                        Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (D) Log.d(TAG, "onConnectionSuspended: " + i);
                    }

                }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        if (D) Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                })
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        setContentView(R.layout.activity_main);


    }

    private void findWearableNode() {
        PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                if (result.getNodes().size() > 0) {
                    mWearableNode = result.getNodes().get(0);
                    if (D)
                        Log.d(TAG, "Found wearable: name=" + mWearableNode.getDisplayName() + ", id=" + mWearableNode.getId());
                } else {
                    mWearableNode = null;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Timer timer = new Timer();
        if (networkInfo != null && networkInfo.isConnected()) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(BACKEND_URL);
                        InputSource is = new InputSource(url.openStream());
                        InputStream byteStream = is.getByteStream();
                        String contentAsString = readIt(byteStream);
                        JSONObject jObject = new JSONObject(contentAsString);
                        String value = jObject.getString("value");
                        sendToWearable(value, null, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }

    public static String readIt(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[20];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected void onPause() {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    private void sendToWearable(String path, byte[] data, final ResultCallback<MessageApi.SendMessageResult> callback) {
        if (mWearableNode != null) {
            Log.d(TAG, "Trying to send message " + path);
            PendingResult<MessageApi.SendMessageResult> pending = Wearable.MessageApi.sendMessage(mGoogleApiClient, mWearableNode.getId(), path, data);
            pending.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult result) {
                    if (callback != null) {
                        callback.onResult(result);
                    }
                    if (!result.getStatus().isSuccess()) {
                        if (D) Log.d(TAG, "ERROR: failed to send Message: " + result.getStatus());
                    }
                }
            });
        } else {
            if (D) Log.d(TAG, "ERROR: tried to send message before device was found");
        }
    }
}
