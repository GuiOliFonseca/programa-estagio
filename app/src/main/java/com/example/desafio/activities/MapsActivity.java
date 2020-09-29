package com.example.desafio.activities;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.desafio.R;
import com.example.desafio.utils.APIinterface;
import com.example.desafio.utils.Parada;
import com.example.desafio.utils.RestClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private String cookie;
    private List<Marker> paradas = new ArrayList<>();
    private List<Parada> codigoParadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        paradas.clear();
        codigoParadas.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnMarkerClickListener(this);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mMap.setMyLocationEnabled(true);

        }else{

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        // Add a marker in Sydney and move the camera

        LatLng sp = new LatLng(-23.536181, -46.603953);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sp, 10));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(-24.036225, -46.918987),
                new LatLng(-23.362295, -46.370901)
        );

        mMap.setLatLngBoundsForCameraTarget(latLngBounds);

        final APIinterface apIinterface = RestClient.getService();
        apIinterface.autenticar("66aae5541a229cf146b7e01a622079c818f22efbf8b9b3732e3abd8f01eed225").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                if (response.isSuccessful()) {

                    cookie = response.headers().get("Set-Cookie");
                    apIinterface.retornaParadas(response.headers().get("Set-Cookie"),"").enqueue(new Callback<List<Parada>>() {
                        @Override
                        public void onResponse(Call<List<Parada>> call, Response<List<Parada>> response) {

                            if (response.isSuccessful()){


                                for (Parada parada : response.body()){

                                    LatLng paradaLatLng = new LatLng(Double.parseDouble(parada.getLatitude()), Double.parseDouble(parada.getLongitude()));

                                    MarkerOptions markerOptions = new MarkerOptions().position(paradaLatLng).title(parada.getNome()).icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_baseline_local_parking_24)).title(parada.getNome()).visible(false);
                                    Marker marker = mMap.addMarker(markerOptions);
                                    paradas.add(marker);
                                    codigoParadas.add(parada);
                                }
                            }else{

                                Log.e("erro", response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Parada>> call, Throwable t) {

                            Log.e("erro", t.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.e("erro", t.getMessage());
            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onCameraIdle() {

        CameraPosition cameraPosition = mMap.getCameraPosition();

        Log.e("zoom", String.valueOf(cameraPosition.zoom));
        Log.e("tamanho", String.valueOf(paradas.size()));

        if(cameraPosition.zoom >= 14){

            if (!paradas.get(0).isVisible()) {
                for (Marker marker : paradas) {

                    Log.e("mudando", "visibilidade");
                    marker.setVisible(true);
                }
            }
        }else{

            for (Marker marker : paradas){

                marker.setVisible(false);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.e("cliquei", "marcador");

        for (Parada parada : codigoParadas){

            if (parada.getNome().equals(marker.getTitle())){

                startActivity(new Intent(this, ExibeParada.class).putExtra("variaveis", new String[]{marker.getTitle(), cookie, parada.getCodigo()}));
            }
        }

        return false;
    }
}