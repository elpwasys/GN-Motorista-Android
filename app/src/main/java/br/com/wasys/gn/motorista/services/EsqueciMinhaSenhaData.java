package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/25/16.
 */
public class EsqueciMinhaSenhaData {

    @SerializedName("cpf")
    private String cpf;
    @SerializedName("senha")
    private String senha;
    @SerializedName("data_de_nascimento")
    private String data_de_nascimento;
    @SerializedName("confirmacao")
    private String confirmacao_de_senha;

    public EsqueciMinhaSenhaData(String cpf, String senha, String data_de_nascimento, String confirmacao)
    {
        setCpf(cpf);
        setSenha(senha);
        setData_de_nascimento(data_de_nascimento);
        setConfirmacao_de_senha(confirmacao);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getData_de_nascimento() {
        return data_de_nascimento;
    }

    public void setData_de_nascimento(String data_de_nascimento) {
        this.data_de_nascimento = data_de_nascimento;
    }

    public String getConfirmacao_de_senha() {
        return confirmacao_de_senha;
    }

    public void setConfirmacao_de_senha(String confirmacao_de_senha) {
        this.confirmacao_de_senha = confirmacao_de_senha;
    }
}
