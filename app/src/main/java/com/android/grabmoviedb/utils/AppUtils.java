package com.android.grabmoviedb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.grabmoviedb.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp pc on 27-12-2015.
 */
public final class AppUtils {

    private AppUtils() {
        //Empty Constructor
    }

    /**
     * Check for network connection
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public static String createImageUrl(Context context, String imageUrl) {
        return "http://image.tmdb.org/t/p/w500" + imageUrl + "?api_key=" + context.getResources().getString(R.string.api_key);
    }

    public static String getFormattedDate(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd - MMM - yy");
        return format.format(newDate);

    }
}
