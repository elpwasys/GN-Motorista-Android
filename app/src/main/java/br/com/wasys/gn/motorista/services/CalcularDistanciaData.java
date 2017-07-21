package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CalcularDistanciaData implements Serializable {

    @SerializedName("origem")
    private String origem;

    @SerializedName("destino")
    private String destino;

    public CalcularDistanciaData(String origem, String destino)
    {
        this.origem = origem;
        this.destino = destino;

    }
}