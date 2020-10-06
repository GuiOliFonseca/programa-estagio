package com.example.desafio.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Linha {


    @SerializedName("lt0")
    @Expose
    private String destino;

    @SerializedName("lt1")
    @Expose
    private String origem;

    @SerializedName("vs")
    @Expose
    private List<Onibus> onibusList;

    @SerializedName("cl")
    @Expose
    private int codigo;

    @SerializedName("sl")
    @Expose
    private int sentido;



    public Linha() {
    }

    public List<Onibus> getOnibusList() {
        return onibusList;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getSentido() {
        return sentido;
    }
}
