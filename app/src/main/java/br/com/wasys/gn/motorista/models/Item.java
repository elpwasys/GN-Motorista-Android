package br.com.wasys.gn.motorista.models;

/**
 * Created by fernandamoncores on 4/29/16.
 */
public class Item {

    public String origem;
    public String destino;
    public String distancia;
    public String pedagio;
    public String estacionamento;
    public boolean concluido;

    public Item(String origem, String destino, String pedagio, String estacionamento) {
        this.origem = origem;
        this.destino = destino;
        this.pedagio = pedagio;
        this.estacionamento = estacionamento;
    }

    public Item(String origem, String destino, String distancia, String pedagio, String estacionamento)
    {
        this.origem = origem;
        this.distancia = distancia;
        this.destino = destino;
        this.pedagio = pedagio;
        this.estacionamento = estacionamento;

    }
}
