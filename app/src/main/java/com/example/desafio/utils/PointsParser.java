package com.example.desafio.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskCallback;
    String directionMode;

    public PointsParser(Context mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {

            jObject = new JSONObject(jsonData[0]);
            DataParser parser = new DataParser();

            routes = parser.parse(jObject);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {

        ArrayList<LatLng> points;
        PolylineOptions lineOptions;

        points = new ArrayList<>();
        lineOptions = new PolylineOptions();

        List<HashMap<String, String>> path = result.get(0);

        for (int j = 0; j < path.size(); j++) {

            HashMap<String, String> point = path.get(j);
            double lat = Double.parseDouble(point.get("lat"));
            double lng = Double.parseDouble(point.get("lng"));
            LatLng position = new LatLng(lat, lng);
            points.add(position);
        }

        lineOptions.addAll(points);

        if (directionMode.equalsIgnoreCase("walking")) {

            lineOptions.width(10);
            lineOptions.color(Color.MAGENTA);
        } else {

            lineOptions.width(10);
            lineOptions.color(Color.parseColor("#0086ca"));
        }

        taskCallback.onTaskDone(lineOptions);
    }
}
