package innojam.sap.com.eyelike;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onStartService();
    }

    public void onStartService() {
        // Construct our Intent specifying the Service
        Intent i = new Intent(this, EyeLikeIntentService.class);
        startService(i);
    }
}
