package br.com.wasys.gn.motorista.models;

/**
 * Created by fernandamoncores on 4/26/16.
 */
public class Carro {

    private String id;
    private String nome;
    private String placa;

    public Carro(String id,String nome, String placa)
    {
        this.id = id;
        this.nome = nome;
        this.placa = placa;

    }

    public String getId()
    {
        return this.id;
    }

    public String getNome()
    {
        return this.nome;
    }

    public String getPlaca()
    {
        return this.placa;
    }
}
