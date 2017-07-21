package br.com.wasys.gn.motorista.models;

import android.provider.BaseColumns;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pascke on 26/08/16.
 */
public class Marcacao implements Serializable {

    public Long id;
    public Date data;
    public Integer ordem;
    public Double latitude;
    public Double longitude;

    public Realizado realizado;
    public Solicitacao solicitacao;

    public static abstract class Table implements BaseColumns {
        public static final String NAME = "marcacao";
        public static final String REALIZADO_ID = "realizado_id";
        public static final String SOLICITACAO_ID = "solicitacao_id";
        public static final String DATA = "data";
        public static final String ORDEM = "ordem";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}