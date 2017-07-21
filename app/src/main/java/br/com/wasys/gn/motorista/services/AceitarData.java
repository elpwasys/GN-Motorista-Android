package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/26/16.
 */
public class AceitarData {

    @SerializedName("solicitacao_id")
    String solicitacao_id;
    @SerializedName("carro_id")
    String carro_id;
    @SerializedName("motivo")
    String motivo;

    public AceitarData(String solicitacao_id, String carro_id, String motivo) {
        this.solicitacao_id = solicitacao_id;
        this.carro_id = carro_id;
        this.motivo = motivo;
    }
}
