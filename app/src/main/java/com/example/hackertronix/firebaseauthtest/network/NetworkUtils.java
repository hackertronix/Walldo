package com.example.hackertronix.firebaseauthtest.network;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hackertronix on 18/03/17.
 */

public class NetworkUtils {


    public static String getResponseFromUnsplash(String url) throws IOException
    {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();


        Response response = client.newCall(request)
                .execute();

        String responseBody = response.body().string();

        if(responseBody != null && !TextUtils.isEmpty(responseBody))
        {
            return responseBody;
        }
        else {
            return null;
        }
    }
}
