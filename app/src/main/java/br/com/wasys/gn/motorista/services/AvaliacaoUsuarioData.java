package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/29/16.
 */
public class AvaliacaoUsuarioData {


    @SerializedName("solicitacao_id")
    String solicitacao_id;
    @SerializedName("nota")
    String nota;
    @SerializedName("comentario")
    String comentario;

    public AvaliacaoUsuarioData(String solicitacao_id, String nota,String comentario) {
        this.solicitacao_id = solicitacao_id;
        this.nota = nota;
        this.comentario = comentario;
    }
}
