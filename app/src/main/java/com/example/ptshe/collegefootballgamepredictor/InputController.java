package com.example.ptshe.collegefootballgamepredictor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class InputController {

    public InputController(){

    }

    public static JsonArray downloadAPIData(String sURL) throws IOException {

        URL url = new URL(sURL);
        URLConnection request = url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonArray rootobj = root.getAsJsonArray();
        return rootobj;

    }

    public static JsonArray readFileData(String filename) throws FileNotFoundException {
        JsonParser jp = new JsonParser();
        FileReader file = new FileReader(filename);
        JsonElement root = jp.parse(new JsonReader(file));
        JsonArray rootObj = root.getAsJsonArray();
        return rootObj;
    }

}