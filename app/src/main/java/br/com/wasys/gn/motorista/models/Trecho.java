package br.com.wasys.gn.motorista.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by pascke on 30/08/16.
 */
public class Trecho implements Serializable {

    public Long id;
    public Date data;
    public String horario;
    public Boolean pernoite;

    public Endereco inicio;
    public Endereco termino;
}
