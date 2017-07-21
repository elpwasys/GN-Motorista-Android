package br.com.wasys.gn.motorista.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/22/16.
 */

public class Usuario {

    @SerializedName("id")
    private String id;
    @SerializedName("nome")
    private String nome;
    @SerializedName("colaborador")
    private String colaborador;
    @SerializedName("email")
    private String email;
    @SerializedName("cpf")
    private String cpf;

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

    public String getColaborador() {
        return colaborador;
    }

    public void setColaborador(String colaborador) {
        this.colaborador = colaborador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
