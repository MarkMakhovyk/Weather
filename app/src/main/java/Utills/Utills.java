package Utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utills {
    private static final String TAG = "Load";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        Log.e(TAG, "isOnline: " + (netInfo != null && netInfo.isConnectedOrConnecting()));
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
