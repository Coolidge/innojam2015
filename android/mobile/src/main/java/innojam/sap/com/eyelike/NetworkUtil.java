package innojam.sap.com.eyelike;

import android.app.IntentService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by I034326 on 17/02/2015.
 */
public class NetworkUtil {

    private static final String BACKEND_URL = "https://eyelikei305845trial.hanatrial.ondemand.com/EyeLikeServlet/MainServlet";

    public static String getCurrantBackendValue() throws IOException, JSONException {
        InputStream is = null;
        try {

            URL url = new URL(BACKEND_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            JSONObject jObject = new JSONObject(contentAsString);
            return jObject.getString("value");

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    public static String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[20];
        reader.read(buffer);
        return new String(buffer);
    }
}
