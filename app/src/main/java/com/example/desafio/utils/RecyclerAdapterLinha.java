package com.example.desafio.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafio.R;

import java.util.List;

public class RecyclerAdapterLinha extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Linha> linhas;

    public RecyclerAdapterLinha(List<Linha> linhas) {

        this.linhas = linhas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_linha, parent, false);
        HolderLinha HolderLinha = new HolderLinha(view);

        return HolderLinha;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        String codigo = "Código: " + linhas.get(position).getCodigo();
        String origem = " " + linhas.get(position).getOrigem();
        String destino = " " + linhas.get(position).getDestino();

        ((HolderLinha) holder).destino.setText(destino);
        ((HolderLinha) holder).origem.setText(origem);
        ((HolderLinha) holder).codigo.setText(codigo);
    }


    @Override
    public int getItemCount() {
        return linhas.size();
    }

}
