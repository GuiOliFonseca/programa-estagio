package com.example.desafio.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Onibus {

    @SerializedName("p")
    @Expose
    private String prefixo;

    @SerializedName("t")
    @Expose
    private String horarioPrevisto;

    @SerializedName("ta")
    @Expose
    private String horarioLocalizacao;

    @SerializedName("py")
    @Expose
    private double latitude;

    @SerializedName("px")
    @Expose
    private double longitude;

    private String destino;

    private String origem;

    public String getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public String getHorarioLocalizacao() {
        return horarioLocalizacao;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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
}
