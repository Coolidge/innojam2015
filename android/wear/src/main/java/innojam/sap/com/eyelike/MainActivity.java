package innojam.sap.com.eyelike;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Scanner;

public class MainActivity extends Activity {
    private static final boolean D = true;
    /**
     * Messages from Phone to Wear:
     */
    private MessageApi.MessageListener mMessageListener = new MessageApi.MessageListener() {
        @Override
        public void onMessageReceived(MessageEvent m) {
            Scanner s = new Scanner(m.getPath());
            String value = s.next();
            switch (value) {
                case "start":
                    Log.d(TAG, "mMessageListener start");
                    break;
                case "stop":
                    moveTaskToBack(true);
                    break;
                default:
                    updateValue(value);
                    break;
            }
        }
    };

    private static final String TAG = "EyeLike";
    private GoogleApiClient mGoogleApiClient = null;
    private Node mPhoneNode = null;
    private String value = "no value";
    private MainActivity.MyView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MainActivity.MyView(this);
        setContentView(view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        findPhoneNode();
                        Wearable.MessageApi.addListener(mGoogleApiClient, mMessageListener);
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        if (mPhoneNode != null) {
            sendToPhone("start", null, null);
        } else {
            findPhoneNode();
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
        if (mPhoneNode != null) {
            sendToPhone("stop", null, null);
        } else {
            findPhoneNode();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPhoneNode != null) {
            sendToPhone("stop", null, new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                    }
                    moveTaskToBack(true);
                }
            });
        } else {
            findPhoneNode();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhoneNode != null) {
            sendToPhone("stop", null, new ResultCallback<MessageApi.SendMessageResult>() {
                @Override
                public void onResult(MessageApi.SendMessageResult result) {
                    if (!result.getStatus().isSuccess()) {
                        Log.e(TAG, "ERROR: failed to send Message: " + result.getStatus());
                    }
                    moveTaskToBack(true);
                }
            });
        }
        Wearable.MessageApi.removeListener(mGoogleApiClient, mMessageListener);
    }


    private void updateValue(String value) {
        Log.d(TAG, "new value: " + value);
        this.value = value;
        view.post(new Runnable() {
            @Override
            public void run() {
                view.invalidate();
            }
        });

    }

    void findPhoneNode() {
        PendingResult<NodeApi.GetConnectedNodesResult> pending = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        pending.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                if (result.getNodes().size() > 0) {
                    mPhoneNode = result.getNodes().get(0);
                    if (D)
                        Log.d(TAG, "Found phone: name=" + mPhoneNode.getDisplayName() + ", id=" + mPhoneNode.getId());
                    sendToPhone("start", null, null);
                } else {
                    mPhoneNode = null;
                }
            }
        });
    }

    private void sendToPhone(String path, byte[] data, final ResultCallback<MessageApi.SendMessageResult> callback) {
        Log.d(TAG, "sendToPhone " + path);
        if (mPhoneNode != null) {
            PendingResult<MessageApi.SendMessageResult> pending = Wearable.MessageApi.sendMessage(mGoogleApiClient, mPhoneNode.getId(), path, data);

            // Result from the phone:
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

    public class MyView extends View {

        private Paint circle;
        private Paint label;
        private Rect bounds;

        public MyView(Context context) {
            super(context);
            circle = new Paint();
            label = new Paint();
            bounds = new Rect();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int x = getWidth();
            int y = getHeight();
            int radius;
            radius = 120;

            label.setColor(Color.BLACK);
            label.setTextSize(18f);
            label.setAntiAlias(true);
            label.setTextAlign(Paint.Align.CENTER);

            label.getTextBounds(value, 0, value.length(), bounds);

            circle.setStyle(Paint.Style.FILL);
            circle.setColor(Color.BLACK);

            // Use Color.parseColor to define HTML colors
            int color = Color.RED;
            int valueAsInt = 0;
            try {
                valueAsInt = Integer.parseInt(value);
            } catch (NumberFormatException ignored) {}
            if (valueAsInt >= 75) {
                color = Color.GREEN;
            } else if (valueAsInt >= 25) {
                color = Color.YELLOW;
            }
            circle.setColor(color);
            canvas.drawCircle(x / 2, y / 2, radius, circle);
            Log.d(TAG, "paint value: " + value);
            canvas.drawText(value, x / 2, y / 2, label);

        }
    }

}
