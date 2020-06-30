package com.ptit.timetable.utils;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpServices {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String SHARE_PREF_NAME = "sharedPreferenceManager";
    static OkHttpClient client = new OkHttpClient();
    private static Context mContext;
    public static String getCurrentToken(){
        SharedPreferences preferences = mContext.getSharedPreferences(SHARE_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("TOKEN","");
    }

    public static Call postWithToken(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + getCurrentToken())
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public static Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
//    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.github.help").newBuilder();
//    urlBuilder.addQueryParameter("v", "1.0");
//    urlBuilder.addQueryParameter("user", "vogella");
//    String url = urlBuilder.build().toString();
    public static Call getWithToken(String url, Callback callback){
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + getCurrentToken())
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public static Call get(String url, Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public static Call putWithToken(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + getCurrentToken())
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
    public static Call put(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static void setContext(Context mContext) {
        HttpServices.mContext = mContext;
    }

}
