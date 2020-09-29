package com.example.desafio.utils;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafio.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Onibus> Onibus;

    public RecyclerAdapter(List<Onibus> Onibus) {

        this.Onibus = Onibus;
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

        Onibus onibus1 = Onibus.get(position);

        String numeroOnibus =  "Nº do ônibus " + onibus1.getPrefixo();

        ((HolderOnibus) holder).nome.setText(numeroOnibus);
        ((HolderOnibus) holder).horario.setText(onibus1.getHorarioPrevisto());
    }


    @Override
    public int getItemCount() {
        return Onibus.size();
    }

}
