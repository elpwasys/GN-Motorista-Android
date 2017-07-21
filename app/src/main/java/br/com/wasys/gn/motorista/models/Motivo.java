package br.com.wasys.gn.motorista.models;

/**
 * Created by fernandamoncores on 5/19/16.
 */
public class Motivo {


    public String id;
    public String nome;
    public String observacao;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
