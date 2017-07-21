package br.com.wasys.gn.motorista.models;

import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by pascke on 26/08/16.
 */
public class Realizado implements Serializable {

    public Long id;
    public Date data;
    public Long duracao;
    public Long distancia;
    public Double pedagio;
    public Double estacionamento;

    public Endereco inicio;
    public Endereco termino;
    public Solicitacao solicitacao;

    public List<Marcacao> marcacoes;

    public Realizado() {

    }

    public Realizado(Long id) {
        this.id = id;
    }

    public static abstract class Table implements BaseColumns {
        public static final String NAME = "realizado";
        public static final String INICIO_ID = "inicio_id";
        public static final String TERMINO_ID = "termino_id";
        public static final String SOLICITACAO_ID = "solicitacao_id";
        public static final String DATA = "data";
        public static final String DURACAO = "duracao";
        public static final String DISTANCIA = "distancia";
        public static final String PEDAGIO = "pedagio";
        public static final String ESTACIONAMENTO = "estacionamento";
    }
}
