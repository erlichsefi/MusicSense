package metaextract.nkm.com.myplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import static metaextract.nkm.com.myplayer.MainActivity.getDateString;
import static metaextract.nkm.com.myplayer.MainActivity.getTimeString;

public class ShowPhoneGps extends AppCompatActivity {

    private static TextView dateText, timeText, latitudeText, longitudeText;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_gps_phone_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Gps data");
        toolbar.setBackgroundColor(Color.rgb(87, 121, 106));
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dateText = findViewById(R.id.dateTextBox);
        timeText = findViewById(R.id.timeTextBox);
        latitudeText = findViewById(R.id.latitudeTextBox);
        longitudeText = findViewById(R.id.longitudeTextBox);
        Intent intent = new Intent(getApplicationContext(), GpsService.class);
        startService(intent);
        timeDate();
    }

    public void timeDate() {
        dateText.append("Date : " + getDateString());
        timeText.append("Time : " + getTimeString());
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));

            latitudeText.setText("Latitude: " + Double.toString(latitude));
            longitudeText.setText("Longitude: " + Double.toString(longitude));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GpsService.str_receiver));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}