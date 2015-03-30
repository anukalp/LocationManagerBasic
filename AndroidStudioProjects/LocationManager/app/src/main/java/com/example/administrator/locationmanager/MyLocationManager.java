
package com.example.administrator.locationmanager;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by katyal on 2015-03-21.
 */
public abstract class MyLocationManager {

    private static MyLocationManager mInstance;

    public static MyLocationManager getInstance(Context context) {
        Context applicationContext = context.getApplicationContext();
        if (null == mInstance) {
            mInstance = createLocationManager(applicationContext);
        }
        return mInstance;
    }

    public static synchronized MyLocationManager createLocationManager(Context context) {
        return new LocationManagerImpl(context);
    }

    public abstract void requestLocationUpdates(LocationListener listener);

    public abstract void unregisterListener(LocationListener listener);

    public abstract boolean isGPSEnabled();

    public abstract boolean isInternetAvailable();

    public abstract boolean isHighAccuracyLocationMode();

}

class LocationManagerImpl extends MyLocationManager {

    private Context mContext;

    private LocationManager mLocationManager;

    private static final int MIN_TIME = 5000; // 5 sec min time to receive
                                              // updates

    public LocationManagerImpl(Context context) {
        mContext = context;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void requestLocationUpdates(LocationListener locationListener) {
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0,
                locationListener);
    }

    @Override
    public void unregisterListener(LocationListener locationListener) {
        mLocationManager.removeUpdates(locationListener);
    }

    @Override
    public boolean isGPSEnabled() {
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager)mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean isHighAccuracyLocationMode() {
        boolean isHighAccuracy = false;
        try {
            if (Settings.Secure.LOCATION_MODE_HIGH_ACCURACY == Settings.Secure.getInt(
                    mContext.getContentResolver(), Settings.Secure.LOCATION_MODE)) {
                isHighAccuracy = true;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return isHighAccuracy;

    }

}
