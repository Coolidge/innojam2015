package innojam.sap.com.eyelike;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class EyeLikeIntentService extends IntentService implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;

    public EyeLikeIntentService() {
        super("EyeLikeIntesntService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Wearable.API)
                .build();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            while(true) {
                try {
                    Thread.sleep(2000);
                    String value = NetworkUtil.getCurrantBackendValue();
                    Log.d("debug","current value is:" + value);
                    PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/value");
                    putDataMapReq.getDataMap().putString("value",value);
                    PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
