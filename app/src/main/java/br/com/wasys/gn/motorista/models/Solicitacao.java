package br.com.wasys.gn.motorista.models;

import android.support.annotation.StringRes;

import br.com.wasys.gn.motorista.R;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by pascke on 30/08/16.
 */
public class Solicitacao implements Serializable {

    public Long id;
    public Modo modo;
    public Tipo tipo;
    public Date dataInicial;
    public String snapshot;
    public Double kmInicial;
    public Double distanciaTotal;
    public Situacao situacao;
    public Categoria categoria;

    public List<Trecho> trechos;
    public List<Realizado> realizados;

    public enum Modo {
        TRASLADO (R.string.translado),
        IDA_VOLTA (R.string.ida_volta),
        MULTIPLOS (R.string.multiplos);
        public int stringRes;
        Modo(@StringRes int stringRes) {
            this.stringRes = stringRes;
        }
    }

    public enum Tipo {
        MEIA (R.string.meia_diaria),
        DIARIA (R.string.diaria),
        PERNOITE (R.string.pernoite),
        TRASLADO (R.string.translado);
        public int stringRes;
        Tipo(@StringRes int stringRes) {
            this.stringRes = stringRes;
        }
    }

    public enum Situacao {
        PENDENTE (R.string.pendente),
        RECUSADA (R.string.recusada),
        ANDAMENTO (R.string.andamento),
        FINALIZADA (R.string.finalizada),
        CONFIRMADA (R.string.confirmada);
        public int stringRes;
        Situacao(@StringRes int stringRes) {
            this.stringRes = stringRes;
        }
    }

    public enum Categoria {
        EXECUTIVO (R.string.executivo),
        INTERMEDIARIO (R.string.intermediario);
        public int stringRes;
        Categoria(@StringRes int stringRes) {
            this.stringRes = stringRes;
        }
    }

    public Solicitacao() {

    }

    public Solicitacao(Long id) {
        this.id = id;
    }
}
