package com.moloco.sample;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

class Utils {
    static final String LOGTAG = "Moloco Sample App";

    private Utils() {}

    static void logToast(Context context, String message) {
        Log.d(LOGTAG, message);

        if (context != null && context.getApplicationContext() != null) {
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
