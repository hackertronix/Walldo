package com.example.hackertronix.firebaseauthtest.network;

import com.example.hackertronix.firebaseauthtest.model.Wallpaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hackertronix on 18/03/17.
 */

public class JSONParser {

    public static ArrayList<Wallpaper> parseWallpapperData (String jsonResponse) throws JSONException{

        JSONArray responseBody = new JSONArray(jsonResponse);
        ArrayList<Wallpaper> Wallpapers = new ArrayList<>();

        for (int i = 1; i < 101; i++) {
            Wallpaper wallpaper = new Wallpaper();
            JSONObject jsonObject = responseBody.getJSONObject(i);


            wallpaper.setFormat(jsonObject.getString("format"));
            wallpaper.setAuthor(jsonObject.getString("author"));
            wallpaper.setAuthor_url(jsonObject.getString("author_url"));
            wallpaper.setFilename(jsonObject.getString("filename"));
            wallpaper.setPost_url(jsonObject.getString("post_url"));

            wallpaper.setWidth(jsonObject.getInt("width"));
            wallpaper.setHeight(jsonObject.getInt("height"));
            wallpaper.setId(jsonObject.getInt("id"));

            Wallpapers.add(wallpaper);
        }

        return Wallpapers;


    }
}
