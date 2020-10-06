package com.example.desafio.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafio.R;

public class HolderLinha extends RecyclerView.ViewHolder {

    public TextView origem, destino, codigo;

    public HolderLinha(@NonNull View itemView) {
        super(itemView);

        origem = itemView.findViewById(R.id.origem);
        destino = itemView.findViewById(R.id.destino);
        codigo = itemView.findViewById(R.id.codigo);

    }
}
