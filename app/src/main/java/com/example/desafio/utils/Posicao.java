package com.example.desafio.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posicao {

    @SerializedName("l")
    @Expose
    private List<Linha> linhas;

    public List<Linha> getLinhas() {
        return linhas;
    }
}
