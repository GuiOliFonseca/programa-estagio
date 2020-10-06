package com.example.desafio.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafio.R;

import java.util.List;

public class RecyclerAdapterOnibus extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Onibus> onibus;
    private List<Linha> linhas;

    public RecyclerAdapterOnibus(List<Onibus> onibus, List<Linha> linhas) {

        this.onibus = onibus;
        this.linhas = linhas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_onibus, parent, false);
        HolderOnibus HolderOnibus = new HolderOnibus(view);

        return HolderOnibus;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Onibus onibus1 = onibus.get(position);
        Linha linha1 = new Linha();

        for (Linha linha : linhas){

            if(linha.getOnibusList().contains(onibus1)){

                linha1 = linha;
                break;
            }
        }

        String destinoOnibus =  "Destino: " + linha1.getDestino();

        String tempoRestante =onibus1.getHorarioPrevisto();

        ((HolderOnibus) holder).nome.setText(destinoOnibus);
        ((HolderOnibus) holder).horario.setText(tempoRestante);
    }


    @Override
    public int getItemCount() {
        return onibus.size();
    }

}
