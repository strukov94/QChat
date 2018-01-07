package com.strukov.qchat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.SparseArray;

import io.reactivex.Observable;
import retrofit2.HttpException;


/**
 * Created by Matthew on 14.12.2017.
 */

public class NetworkUtils {
    private static String TAG = "RESPONSE_ERROR";
    private static SparseArray<String> errorsMap = new SparseArray<>();
    static {
        errorsMap.append(301, "Moved permanently");
        errorsMap.append(400, "Bad request");
        errorsMap.append(401, "Unauthorized");
        errorsMap.append(403, "Forbidden");
        errorsMap.append(404, "Not found");
        errorsMap.append(500, "Internal server error");
        errorsMap.append(502, "Bad gateway");
        errorsMap.append(503, "Service unavailable");
    }

    public static void responseError(Throwable t){
        try {
            int code = ((HttpException)t).code();
            Log.v(TAG, String.format("Error: %s Error code: %d", errorsMap.get(code, "Undescribed error"), code));
        } catch (Exception e) {
            Log.v(TAG, t.getMessage());
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static Observable<Boolean> isNetworkAvailableObservable(Context context) {
        return Observable.just(NetworkUtils.isNetworkAvailable(context));
    }
}
