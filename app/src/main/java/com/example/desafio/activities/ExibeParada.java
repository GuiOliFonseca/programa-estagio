package com.example.desafio.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.desafio.R;
import com.example.desafio.utils.APIinterface;
import com.example.desafio.utils.Linha;
import com.example.desafio.utils.Onibus;
import com.example.desafio.utils.Parada;
import com.example.desafio.utils.Previsao;
import com.example.desafio.utils.RecyclerAdapterOnibus;
import com.example.desafio.utils.RecyclerItemClick;
import com.example.desafio.utils.RestClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExibeParada extends AppCompatActivity {

    private String nomeParada;
    private String cookie;
    private String codigoParada;
    private Parada parada;
    private String horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibe_parada);

        String[] aux = getIntent().getStringArrayExtra("variaveis");

        assert aux != null;
        nomeParada = aux[0];
        cookie = aux[1];
        codigoParada = aux[2];

        carregaLista();
    }

    private void configuraTela() {

        TextView cabecalho = findViewById(R.id.nome);

        String cabecalhoStr = parada.getNome();
        cabecalho.setText(cabecalhoStr);

        TextView data = findViewById(R.id.data);

        String dataStr = "Última atualização às " + horario;
        data.setText(dataStr);

        final List<Linha> linhas = parada.getLinhas();
        final List<Onibus> onibus = new ArrayList<>();

        onibus.clear();
        for (Linha linha : linhas) {

            List<Onibus> aux = linha.getOnibusList();

            onibus.addAll(aux);
        }

        RecyclerAdapterOnibus adapter = new RecyclerAdapterOnibus(onibus, linhas);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.lista_onibus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClick(this, new RecyclerItemClick.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                for (Linha linha : linhas){

                    if (linha.getOnibusList().contains(onibus.get(position))){

                        onibus.get(position).setDestino(linha.getDestino());
                        onibus.get(position).setOrigem(linha.getOrigem());

                        SharedPreferences sharedPreferences = getSharedPreferences("preferencias", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(onibus.get(position));
                        editor.putString("Onibus", json);
                        editor.apply();

                        startActivity(new Intent(ExibeParada.this, MapaOnibus.class).putExtra("latitude", parada.getLatitude()).putExtra("longitude", parada.getLongitude()));
                    }
                }
            }
        }));
    }

    private void carregaLista(){

        APIinterface apIinterface = RestClient.getService();

        apIinterface.retornaPrevisao(cookie, codigoParada).enqueue(new Callback<Previsao>() {
            @Override
            public void onResponse(Call<Previsao> call, Response<Previsao> response) {

                if (response.isSuccessful()) {

                    if (response.body().getParada() != null) {

                        parada = response.body().getParada();
                        horario = response.body().getHorario();

                        configuraTela();
                    }else{

                        TextView cabecalho = findViewById(R.id.nome);

                        String cabecalhoStr = nomeParada;
                        cabecalho.setText(cabecalhoStr);

                        RecyclerView recyclerView = findViewById(R.id.lista_onibus);
                        recyclerView.setVisibility(View.GONE);
                        TextView nenhumOnibus = findViewById(R.id.nenhum_onibus);
                        nenhumOnibus.setVisibility(View.VISIBLE);
                    }
                } else {

                    Log.e("erro", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Previsao> call, Throwable t) {

                Log.e("erro", t.getMessage());

            }
        });


    }

    public void voltarMenu(View view) {

        startActivity(new Intent(this, MainActivity.class));
    }

    public void atualizaLista(View view) {

        carregaLista();
    }
}