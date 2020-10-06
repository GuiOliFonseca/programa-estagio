package com.example.desafio.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIinterface {

    String URL_BASE = "http://api.olhovivo.sptrans.com.br/v2.1/";


    @Headers("Content-Type: application/json")
    @POST("Login/autenticar")
    Call<String> autenticar(@Query("token") String token);

    @Headers("Content-Type: application/json")
    @GET("Parada/Buscar")
    Call<List<Parada>> retornaParadas(@Header ("Cookie") String cookie, @Query("termosBusca") String termo);

    @Headers("Content-Type: application/json")
    @GET("Previsao/Parada")
    Call<Previsao> retornaPrevisao(@Header ("Cookie") String cookie, @Query("codigoParada") String codigoParada);

    @Headers("Content-Type: application/json")
    @GET("Posicao")
    Call<Posicao> retornaPosicao(@Header ("Cookie") String cookie);

    @Headers("Content-Type: application/json")
    @GET("Posicao/Linha")
    Call<Linha> retornaPosicaoPorLinha(@Header ("Cookie") String cookie, @Query("codigoLinha") int codigoLinha);

    @Headers("Content-Type: application/json")
    @GET("Parada/BuscarParadasPorLinha")
    Call<List<Parada>> retornaParadasPorLinha(@Header ("Cookie") String cookie, @Query("codigoLinha") int codigoLinha);
}
