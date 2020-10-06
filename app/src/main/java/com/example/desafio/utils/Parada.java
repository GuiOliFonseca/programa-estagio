package com.example.desafio.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Parada {

    @SerializedName("cp")
    @Expose
    private String codigo;

    @SerializedName("np")
    @Expose
    private String nome;

    @SerializedName("py")
    @Expose
    private double latitude;

    @SerializedName("px")
    @Expose
    private double longitude;

    @SerializedName("l")
    @Expose
    private List<Linha> linhas;


    public String getNome() {
        return nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCodigo() {
        return codigo;
    }

    public List<Linha> getLinhas() {
        return linhas;
    }
}
