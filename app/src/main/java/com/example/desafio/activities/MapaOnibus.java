package com.example.desafio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.desafio.R;
import com.example.desafio.utils.FetchURL;
import com.example.desafio.utils.Onibus;
import com.example.desafio.utils.Parada;
import com.example.desafio.utils.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapaOnibus extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private Onibus onibus;
    private GoogleMap mMap;
    private double paradaLatitude;
    private double paradaLongitude;
    private Polyline rota;
    private MarkerOptions markerOnibus, markerParada;
    private Location posicaoAtual;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_onibus);

        paradaLatitude = getIntent().getDoubleExtra("latitude", 0);
        paradaLongitude = getIntent().getDoubleExtra("longitude", 0);

        SharedPreferences mPrefs = getSharedPreferences("preferencias", MODE_PRIVATE);

        if (mPrefs.contains("Onibus")) {
            Gson gson = new Gson();
            String json = mPrefs.getString("Onibus", "");
            onibus = gson.fromJson(json, Onibus.class);
        }

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        buscarPosicaoAtual();

        LatLng onibusLatLng = new LatLng(onibus.getLatitude(), onibus.getLongitude());
        LatLng paradaLatLng = new LatLng(paradaLatitude, paradaLongitude);

        markerOnibus = new MarkerOptions().position(onibusLatLng).icon(bitmapDescriptorFromVector(MapaOnibus.this, R.drawable.ic_baseline_directions_bus_24));
        markerParada = new MarkerOptions().position(paradaLatLng).icon(bitmapDescriptorFromVector(MapaOnibus.this, R.drawable.ic_baseline_local_parking_24));


        String url = getUrl(markerOnibus.getPosition(), markerParada.getPosition(), "driving");

        new FetchURL(this).execute(url, "driving");
    }

    private void buscarPosicaoAtual() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }

        Task<Location> task = locationClient.getLastLocation();
        task.addOnSuccessListener(location -> {

            if (location != null) {

                posicaoAtual = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 44:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buscarPosicaoAtual();
                }

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        mMap.addMarker(markerOnibus);

        mMap.addMarker(markerParada);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerOnibus.getPosition(), 15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private String getUrl(LatLng origem, LatLng destino, String direction) {

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+ origem.latitude + "," + origem.longitude + "&destination=" + destino.latitude + "," + destino.longitude + "&mode=" +
                     direction + "&key=" + getString(R.string.google_maps_key);

        return url;
    }

    @Override
    public void onTaskDone(Object... values) {

        if (rota != null) {

            rota.remove();
        }

        rota = mMap.addPolyline((PolylineOptions) values[0]);
    }
}