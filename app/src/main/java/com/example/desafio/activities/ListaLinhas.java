package com.example.desafio.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desafio.R;
import com.example.desafio.utils.APIinterface;
import com.example.desafio.utils.Linha;
import com.example.desafio.utils.Posicao;
import com.example.desafio.utils.RecyclerAdapterLinha;
import com.example.desafio.utils.RecyclerAdapterOnibus;
import com.example.desafio.utils.RecyclerItemClick;
import com.example.desafio.utils.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaLinhas extends AppCompatActivity {

    private String cookie;
    private List<Linha> linhasAPI = new ArrayList<>();
    private List<Linha> linhas = new ArrayList<>();
    private String pesquisaOrigem = "", pesquisaDestino = "";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_linhas);

        SearchView barraPesquisaOrigem = findViewById(R.id.search_bar_origem);
        SearchView barraPesquisaDestino = findViewById(R.id.search_bar_destino);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.lista_linhas);
        recyclerView.setVisibility(View.GONE);

        consultaAPI();

        barraPesquisaOrigem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                progressBar.setVisibility(View.VISIBLE);
                pesquisaOrigem = newText;
                carregaLista();
                return false;
            }
        });

        barraPesquisaDestino.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                progressBar.setVisibility(View.VISIBLE);
                pesquisaDestino = newText;
                carregaLista();
                return false;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClick(this, new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                startActivity(new Intent(ListaLinhas.this, MapaLinha.class).putExtra("codigoLinha", linhas.get(position).getCodigo()));
            }
        }));
    }

    public void voltarMenu(View view) {

        startActivity(new Intent(this, MainActivity.class));
    }

    public void abrirMapa(View view) {

        startActivity(new Intent(this, MapsActivity.class));
    }

    private void consultaAPI() {

        final APIinterface apIinterface = RestClient.getService();
        linhasAPI.clear();

        apIinterface.autenticar("66aae5541a229cf146b7e01a622079c818f22efbf8b9b3732e3abd8f01eed225").enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                cookie = response.headers().get("Set-Cookie");

                apIinterface.retornaPosicao(cookie).enqueue(new Callback<Posicao>() {
                    @Override
                    public void onResponse(Call<Posicao> call, Response<Posicao> response) {

                        if (response.isSuccessful()) {

                            for(Linha linha : response.body().getLinhas()){

                                if(linha.getSentido() == 1){

                                    linhasAPI.add(linha);
                                }else{

                                    String destinoAux = linha.getDestino();

                                    linha.setDestino(linha.getOrigem());
                                    linha.setOrigem(destinoAux);

                                    linhasAPI.add(linha);
                                }
                            }

                            carregaLista();
                        }
                    }

                    @Override
                    public void onFailure(Call<Posicao> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void carregaLista() {

        TextView nenhumaLinha = findViewById(R.id.nenhuma_linha);
        nenhumaLinha.setVisibility(View.GONE);

        linhas.clear();

        if (!pesquisaOrigem.isEmpty() || !pesquisaDestino.isEmpty()) {

            for (Linha linha : linhasAPI) {

                if (linha.getOrigem().toLowerCase().contains(pesquisaOrigem.toLowerCase()) && linha.getDestino().toLowerCase().contains(pesquisaDestino.toLowerCase())) {

                    linhas.add(linha);
                }
            }
        } else {

            linhas.addAll(linhasAPI);
        }

        progressBar.setVisibility(View.GONE);
        if (linhas.size() == 0) {

            nenhumaLinha = findViewById(R.id.nenhuma_linha);
            nenhumaLinha.setVisibility(View.VISIBLE);
            View barra = findViewById(R.id.barra);
            barra.setVisibility(View.GONE);
        } else {

            Log.e("tamanho linhas", String.valueOf(linhas.size()));


            RecyclerAdapterLinha adapter = new RecyclerAdapterLinha(linhas);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setVisibility(View.VISIBLE);
        }

    }
}