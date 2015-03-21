
package com.example.administrator.locationmanager;

import android.location.Location;
import android.location.LocationListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private MyLocationManager mLocationManager;

    private MyLocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = MyLocationManager.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLocationManager.isInternetAvailable() && mLocationManager.isGPSEnabled()
                && mLocationManager.isHighAccuracyLocationMode()) {
            if (null == mLocationListener)
                mLocationListener = new MyLocationListener();
            mLocationManager.requestLocationUpdates(mLocationListener);
        } else if (!mLocationManager.isGPSEnabled()
                || !mLocationManager.isHighAccuracyLocationMode()) {
            CustomDialogFragment.show(getFragmentManager(), true, this);
        } else if (!mLocationManager.isInternetAvailable()) {
            CustomDialogFragment.show(getFragmentManager(), false, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mLocationListener) {
            mLocationManager.unregisterListener(mLocationListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            ((TextView)findViewById(R.id.latitude)).setText(getResources().getString(R.string.lat)
                    + ": " + String.valueOf(location.getLatitude()));
            ((TextView)findViewById(R.id.longitude)).setText(getResources().getString(R.string.lon)
                    + ": " + String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            mLocationManager.unregisterListener(this);
        }

    }

    public void forceUpdateLocation(View view) {
        if (mLocationManager.isInternetAvailable() && mLocationManager.isGPSEnabled()
                && mLocationManager.isHighAccuracyLocationMode()) {
            if (null == mLocationListener)
                mLocationListener = new MyLocationListener();
            mLocationManager.requestLocationUpdates(mLocationListener);
        } else if (!mLocationManager.isGPSEnabled()
                || !mLocationManager.isHighAccuracyLocationMode()) {
            CustomDialogFragment.show(getFragmentManager(), true, this);
        } else if (!mLocationManager.isInternetAvailable()) {
            CustomDialogFragment.show(getFragmentManager(), false, this);
        }
    }
}
