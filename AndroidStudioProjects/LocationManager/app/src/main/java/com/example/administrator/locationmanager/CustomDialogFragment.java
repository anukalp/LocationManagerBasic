
package com.example.administrator.locationmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by a.katyal on 2015-03-21.
 */
public class CustomDialogFragment extends DialogFragment {

    private static final String GPS_DIALOG = "custom_gps_dialog";
    private static final String KEY_TITLE_STRING = "title";
    private static final String KEY_ALERT_STRING = "alert";
    private static final String KEY_ALERT_TYPE = "type";

    public static void show(FragmentManager fragmentManager, boolean isGPSDialog, Context mContext) {
        CustomDialogFragment instance = (CustomDialogFragment) fragmentManager
                .findFragmentByTag(GPS_DIALOG);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE_STRING, mContext.getResources().getString(isGPSDialog ? R.string.gps_alert_title : R.string.connection_error_title));
        bundle.putString(KEY_ALERT_STRING, mContext.getResources().getString(isGPSDialog ? R.string.gps_alert_msg : R.string.connection_error));
        bundle.putBoolean(KEY_ALERT_TYPE, isGPSDialog);
        if (instance == null) {
            instance = new CustomDialogFragment();
        } else {
            instance.dismissAllowingStateLoss();
        }
        instance.setArguments(bundle);
        instance.show(fragmentManager, GPS_DIALOG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context mContext = getActivity();
        final Bundle args = getArguments();
        final String mTitleText = args.getString(KEY_TITLE_STRING);
        final String mAlertMessage = args.getString(KEY_ALERT_STRING);
        final boolean isGpsDialog = args.getBoolean(KEY_ALERT_TYPE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(mAlertMessage)
                .setTitle(mTitleText)
                .setCancelable(false)
                .setPositiveButton(mContext.getResources().getString(isGpsDialog ? R.string.gps_alert_done : android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (!isAdded())
                                    return;
                                if (isGpsDialog) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    getActivity().startActivity(callGPSSettingIntent);
                                } else {
                                    dialog.dismiss();
                                }
                            }
                        });
        if (isGpsDialog) {
            builder.setNegativeButton(mContext.getResources().getString(R.string.gps_alert_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (!isAdded())
                                return;
                            dialog.dismiss();
                            getActivity().finish();
                        }
                    });
        }
        AlertDialog alert = builder.create();
        return alert;
    }
}
