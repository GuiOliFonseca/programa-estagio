package com.example.desafio.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafio.R;

public class HolderOnibus extends RecyclerView.ViewHolder {

    public TextView nome, horario;

    public HolderOnibus(@NonNull View itemView) {
        super(itemView);

        nome = itemView.findViewById(R.id.nome);
        horario = itemView.findViewById(R.id.horario);

    }
}
