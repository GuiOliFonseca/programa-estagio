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
    private String latitude;

    @SerializedName("px")
    @Expose
    private String longitude;

    public String getHorarioPrevisto() {
        return horarioPrevisto;
    }

    public void setHorarioPrevisto(String horarioPrevisto) {
        this.horarioPrevisto = horarioPrevisto;
    }

    public String getHorarioLocalizacao() {
        return horarioLocalizacao;
    }

    public void setHorarioLocalizacao(String horarioLocalizacao) {
        this.horarioLocalizacao = horarioLocalizacao;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrefixo() {
        return prefixo;
    }

    public void setPrefixo(String prefixo) {
        this.prefixo = prefixo;
    }
}
