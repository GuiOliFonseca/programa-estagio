package com.example.desafio.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Linha {

    @SerializedName("vs")
    @Expose
    private List<Onibus> onibusList;

    public List<Onibus> getOnibusList() {
        return onibusList;
    }

    public void setOnibusList(List<Onibus> onibusList) {
        this.onibusList = onibusList;
    }
}
