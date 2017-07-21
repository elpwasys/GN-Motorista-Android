package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/28/16.
 */
public class IniciarServicoData {


    @SerializedName("solicitacao_id")
    String solicitacao_id;
    @SerializedName("partida")
    String partida;
    @SerializedName("km_inicial")
    String km_inicial;

    public IniciarServicoData(String solicitacao_id, String partida,String km) {
        this.solicitacao_id = solicitacao_id;
        this.partida = partida;
        this.km_inicial = km;
    }
}
