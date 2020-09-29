package com.example.desafio.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.desafio.utils.RecyclerAdapter;
import com.example.desafio.utils.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExibeParada extends AppCompatActivity {

    private String nomeParada;
    private String cookie;
    private String codigParada;
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
        codigParada = aux[2];

        carregaLista();
    }

    private void configuraTela() {

        TextView cabecalho = findViewById(R.id.nome);

        String cabecalhoStr = parada.getNome();
        cabecalho.setText(cabecalhoStr);

        TextView data = findViewById(R.id.data);

        String dataStr = "Última atualização às " + horario;
        data.setText(dataStr);

        List<Linha> linhas = parada.getLinhas();
        List<Onibus> onibus = new ArrayList<>();

        for (Linha linha : linhas) {

            List<Onibus> aux = linha.getOnibusList();

            onibus.addAll(aux);
        }

        RecyclerAdapter adapter = new RecyclerAdapter(onibus);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.lista_onibus);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void carregaLista(){

        APIinterface apIinterface = RestClient.getService();

        apIinterface.retornaPrevisao(cookie, codigParada).enqueue(new Callback<Previsao>() {
            @Override
            public void onResponse(Call<Previsao> call, Response<Previsao> response) {

                if (response.isSuccessful()) {

                    if (response.body().getParada() != null) {
                        Log.e("menssagem", response.body().getHorario());
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
}