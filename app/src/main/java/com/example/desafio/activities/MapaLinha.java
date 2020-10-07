package com.example.desafio.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.desafio.R;
import com.example.desafio.utils.APIinterface;
import com.example.desafio.utils.FetchURL;
import com.example.desafio.utils.Linha;
import com.example.desafio.utils.Onibus;
import com.example.desafio.utils.Parada;
import com.example.desafio.utils.RestClient;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaLinha extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private int codigoLinha;
    private List<Marker> paradas = new ArrayList<>();
    private List<String> codigoParadas = new ArrayList<>();
    private List<Marker> onibus = new ArrayList<>();
    private String cookie;
    private Location posicaoAtual;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_linha);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        buscarPosicaoAtual();

        codigoLinha = getIntent().getIntExtra("codigoLinha", 0);
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
        mMap.setOnMarkerClickListener(this);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        consultaAPI();
    }

    private void consultaAPI() {

        paradas.clear();
        onibus.clear();
        codigoParadas.clear();

        LatLng localizacaoAtual = new LatLng(posicaoAtual.getLatitude(), posicaoAtual.getLongitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacaoAtual, 13));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(localizacaoAtual).title("Você"));

        final APIinterface apIinterface = RestClient.getService();

        apIinterface.autenticar("66aae5541a229cf146b7e01a622079c818f22efbf8b9b3732e3abd8f01eed225").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()){

                    cookie = response.headers().get("Set-Cookie");

                    apIinterface.retornaParadasPorLinha(cookie, codigoLinha).enqueue(new Callback<List<Parada>>() {
                        @Override
                        public void onResponse(Call<List<Parada>> call, Response<List<Parada>> response) {

                            if (response.isSuccessful()){

                                Log.e("tamanho", String.valueOf(response.body().size()));
                                for (Parada parada : response.body()){

                                    Log.e("latitude", String.valueOf(parada.getLatitude()));
                                    Log.e("longitude", String.valueOf(parada.getLongitude()));
                                    LatLng paradaLatLng = new LatLng(parada.getLatitude(), parada.getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions().position(paradaLatLng).icon(bitmapDescriptorFromVector(MapaLinha.this, R.drawable.ic_baseline_local_parking_24)).title(parada.getNome()).visible(true);
                                    Marker marker = mMap.addMarker(markerOptions);
                                    paradas.add(marker);
                                    codigoParadas.add(parada.getCodigo());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Parada>> call, Throwable t) {

                        }
                    });

                    apIinterface.retornaPosicaoPorLinha(cookie, codigoLinha).enqueue(new Callback<Linha>() {
                        @Override
                        public void onResponse(Call<Linha> call, Response<Linha> response) {

                            if (response.isSuccessful()){

                                for (Onibus onibus1 : response.body().getOnibusList()){

                                    LatLng onibusLatLng = new LatLng(onibus1.getLatitude(), onibus1.getLongitude());

                                    MarkerOptions markerOptions = new MarkerOptions().position(onibusLatLng).icon(bitmapDescriptorFromVector(MapaLinha.this, R.drawable.ic_baseline_directions_bus_24));
                                    Marker marker = mMap.addMarker(markerOptions);
                                    onibus.add(marker);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Linha> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

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
    public boolean onMarkerClick(Marker marker) {

        if (paradas.contains(marker)){

            startActivity(new Intent(this, ExibeParada.class).putExtra("variaveis", new String[]{marker.getTitle(), cookie, codigoParadas.get(paradas.indexOf(marker))}));
        }

        return false;
    }
}