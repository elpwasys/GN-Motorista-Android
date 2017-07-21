package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 5/20/16.
 */
public class EnviarLocalizacaoData {

    @SerializedName("motorista_id")
    private String motorista_id;
    @SerializedName("carro_id")
    private String carro_id;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public EnviarLocalizacaoData(String motorista_id, String carro_id, String latitude, String longitude)
    {
        this.motorista_id = motorista_id;
        this.carro_id = carro_id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
