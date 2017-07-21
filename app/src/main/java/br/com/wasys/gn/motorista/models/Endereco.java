package br.com.wasys.gn.motorista.models;

import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by pascke on 08/08/16.
 */
public class Endereco implements Serializable {

    public Long id;
    public String cidade;
    public String completo;
    public Double latitude;
    public Double longitude;

    public Endereco() {

    }

    public Endereco(Long id) {
        this.id = id;
    }

    public Endereco(String completo) {
        this.completo = completo;
    }

    public Endereco(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Endereco(String completo, Double latitude, Double longitude) {
        this(latitude, longitude);
        this.completo = completo;
    }

    public Endereco(String cidade, String completo, Double latitude, Double longitude) {
        this(completo, latitude, longitude);
        this.cidade = cidade;
    }

    public static abstract class Table implements BaseColumns {
        public static final String NAME = "endereco";
        public static final String CIDADE = "cidade";
        public static final String COMPLETO = "completo";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }
}