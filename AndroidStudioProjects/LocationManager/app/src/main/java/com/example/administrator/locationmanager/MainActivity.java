
package com.example.administrator.locationmanager;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    private MyLocationManager mLocationManager;

    private MyLocationListener mLocationListener;
    
    private ConnectivityBroadcastReceiver mReceiver;

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                if (mLocationManager.isInternetAvailable() && mLocationManager.isGPSEnabled()
                        && mLocationManager.isHighAccuracyLocationMode()) {
                    CustomDialogFragment.dismissDialog(getFragmentManager());
                    mLocationManager.requestLocationUpdates(mLocationListener);
                } else if (!mLocationManager.isGPSEnabled()
                        || !mLocationManager.isHighAccuracyLocationMode()) {
                    CustomDialogFragment.show(getFragmentManager(), true, MainActivity.this);
                } else if (!mLocationManager.isInternetAvailable()) {
                    CustomDialogFragment.show(getFragmentManager(), false, MainActivity.this);
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationManager = MyLocationManager.getInstance(this);
        if (null == mLocationListener)
            mLocationListener = new MyLocationListener(getFragmentManager());
        mReceiver = new ConnectivityBroadcastReceiver(); 
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(mLocationListener);
        if (!mLocationManager.isInternetAvailable()) {
            CustomDialogFragment.show(getFragmentManager(), false, MainActivity.this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mLocationListener) {
            mLocationManager.unregisterListener(mLocationListener);
        }
        unregisterReceiver(mReceiver);
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

        private final FragmentManager mFragmentManager;

        public MyLocationListener(FragmentManager fm) {
            mFragmentManager = fm;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (mLocationManager.isInternetAvailable() && mLocationManager.isGPSEnabled()
                    && mLocationManager.isHighAccuracyLocationMode() && location.hasAccuracy()) {
                ((TextView)findViewById(R.id.latitude)).setText(getResources().getString(
                        R.string.lat)
                        + ": " + String.valueOf(location.getLatitude()));
                ((TextView)findViewById(R.id.longitude)).setText(getResources().getString(
                        R.string.lon)
                        + ": " + String.valueOf(location.getLongitude()));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (mLocationManager.isInternetAvailable() && mLocationManager.isGPSEnabled()
                    && mLocationManager.isHighAccuracyLocationMode()) {
                CustomDialogFragment.dismissDialog(mFragmentManager);
                mLocationManager.requestLocationUpdates(this);
            } else if (!mLocationManager.isGPSEnabled()
                    || !mLocationManager.isHighAccuracyLocationMode()) {
                CustomDialogFragment.show(mFragmentManager, true, MainActivity.this);
            } else if (!mLocationManager.isInternetAvailable()) {
                CustomDialogFragment.show(mFragmentManager, false, MainActivity.this);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (!mLocationManager.isGPSEnabled() || !mLocationManager.isHighAccuracyLocationMode()) {
                CustomDialogFragment.show(mFragmentManager, true, MainActivity.this);
            } else if (!mLocationManager.isInternetAvailable()) {
                CustomDialogFragment.show(mFragmentManager, false, MainActivity.this);
            }
        }

    }

    public void forceUpdateLocation(View view) {
        mLocationManager.requestLocationUpdates(mLocationListener);
    }
}
