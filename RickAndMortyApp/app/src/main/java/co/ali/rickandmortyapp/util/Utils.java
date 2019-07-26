package co.ali.rickandmortyapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.Nullable;

import co.ali.rickandmortyapp.R;

public class Utils {
    static AlertDialog.Builder mAlertDialogBuilder;
    static AlertDialog mAlertDialog;

    public static boolean checkNetworkConnectivity(Context context){
        NetworkInfo wifiState;
        NetworkInfo dataState;
        ConnectivityManager connManager;

        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiState = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        dataState = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(!wifiState.isConnected() && !dataState.isConnected()){
            return false;
        }

        return true;
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static void createAlertDialog(final Context context){
        if(mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(context);
        }

        mAlertDialogBuilder.setTitle(context.getString(R.string.alert_title_text));
        mAlertDialogBuilder.setMessage(context.getString(R.string.alert_msg_text));
        mAlertDialogBuilder.setNegativeButton(context.getString(R.string.alert_negative_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(myIntent);
            }
        });
        mAlertDialogBuilder.setPositiveButton(context.getString(R.string.alert_positive_button_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(myIntent);
            }
        });
        clearAlertDialog();
        mAlertDialog = mAlertDialogBuilder.show();
    }

    public static void clearAlertDialog(){
        if(mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }
}
