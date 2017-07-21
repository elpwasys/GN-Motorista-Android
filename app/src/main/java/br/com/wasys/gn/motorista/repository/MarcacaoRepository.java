package br.com.wasys.gn.motorista.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.wasys.gn.motorista.models.Marcacao;
import br.com.wasys.gn.motorista.models.Realizado;
import br.com.wasys.gn.motorista.models.Solicitacao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.wasys.library.utils.DateUtils;

/**
 * Created by pascke on 24/08/16.
 */
public class MarcacaoRepository extends Repository<Marcacao> {

    private static final String TAG = MarcacaoRepository.class.getSimpleName();

    public MarcacaoRepository(Context context) {
        super(context);
    }

    public int getNextOrdem(Long id) {

        StringBuilder sql = new StringBuilder();
        sql .append("select ");
        sql .append(    "max(").append(Marcacao.Table.ORDEM).append(") ");
        sql .append("from ").append(Marcacao.Table.NAME).append(" ");
        sql .append("where ").append(Marcacao.Table.SOLICITACAO_ID).append(" = ? ");

        Cursor cursor = database.rawQuery(String.valueOf(sql), new String[] { String.valueOf(id) } );
        int ordem = cursor.moveToFirst() ? cursor.getInt(0) : 0;
        ordem++;

        return ordem;
    }

    public void clear(Realizado realizado) {

        Log.i(TAG, "Atualizando Marcacoes...");

        ContentValues values = new ContentValues();
        values.putNull(Marcacao.Table.REALIZADO_ID);

        database.update(Marcacao.Table.NAME, values, Marcacao.Table.REALIZADO_ID + " = ?", new String[] { String.valueOf(realizado.id) });

        Log.i(TAG, "Marcacoes atualizadas com sucesso!");
    }

    public void deleteBySolicitacao(Long id) {
        database.delete(Marcacao.Table.NAME, Marcacao.Table.SOLICITACAO_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void update(Realizado realizado) {

        Log.i(TAG, "Atualizando Marcacoes...");

        ContentValues values = new ContentValues();
        values.put(Marcacao.Table.REALIZADO_ID, realizado.id);

        database.update(Marcacao.Table.NAME, values, Marcacao.Table.REALIZADO_ID + " is null", null);

        Log.i(TAG, "Marcacoes atualizadas com sucesso!");
    }

    public void salvar(Marcacao marcacao) {

        Log.i(TAG, "Salvando Marcacao...");

        ContentValues values = new ContentValues();

        values.put(Marcacao.Table.DATA, DateUtils.format(marcacao.data, DateUtils.DateType.DATA_BASE.getPattern()));
        values.put(Marcacao.Table.LATITUDE, marcacao.latitude);
        values.put(Marcacao.Table.LONGITUDE, marcacao.longitude);

        Solicitacao solicitacao = marcacao.solicitacao;
        values.put(Marcacao.Table.SOLICITACAO_ID, solicitacao.id);

        Realizado realizado = marcacao.realizado;
        if (realizado != null) {
            values.put(Marcacao.Table.REALIZADO_ID, realizado.id);
        }

        if (marcacao.id == null) {
            values.put(Marcacao.Table.ORDEM, getNextOrdem(solicitacao.id));
            marcacao.id = database.insert(Marcacao.Table.NAME, null, values);
        }
        else {
            values.put(Marcacao.Table.ORDEM, marcacao.ordem);
            database.update(Marcacao.Table.NAME, values, Marcacao.Table._ID + " = ?", new String[] { String.valueOf(marcacao.id) });
        }

        Log.i(TAG, "Marcacao salva com sucesso!");
    }

    public List<Marcacao> listar(Filtro filtro) {
        return listar(filtro, false);
    }

    public List<Marcacao> listar(Filtro filtro, boolean realizadoNull) {
        String clauses = null;
        String[] arguments = null;
        if (filtro != null) {
            clauses = filtro.getClauses();
            arguments = filtro.getArguments();
        }
        if (realizadoNull) {
            if (clauses == null) {
                clauses = "realizado_id is null";
            }
            else {
                clauses += " and realizado_id is null";
            }
        }
        String[] columns = {
                Marcacao.Table._ID,
                Marcacao.Table.REALIZADO_ID,
                Marcacao.Table.SOLICITACAO_ID,
                Marcacao.Table.DATA,
                Marcacao.Table.ORDEM,
                Marcacao.Table.LATITUDE,
                Marcacao.Table.LONGITUDE
        };
        String sortOrder = Marcacao.Table._ID + " desc";
        Cursor cursor = database.query(
                Marcacao.Table.NAME,
                columns,
                clauses,
                arguments,
                null,
                null,
                sortOrder
        );
        List<Marcacao> marcacoes = null;
        Map<Long, Realizado> realizados = new HashMap<>();
        if (cursor.moveToFirst()) {
            marcacoes = new LinkedList<>();
            // Loop
            do {
                // Marcador
                Marcacao marcacao = new Marcacao();
                marcacao.id =  cursor.getLong(0);
                long realizadoId = cursor.getLong(1);
                if (realizadoId > 0) {
                    Realizado realizado = realizados.get(realizadoId);
                    if (realizado == null) {
                        realizado = new Realizado(realizadoId);
                        realizados.put(realizadoId, realizado);
                    }
                    marcacao.realizado = realizado;
                }
                marcacao.solicitacao = new Solicitacao(cursor.getLong(2));
                marcacao.data = DateUtils.parse(cursor.getString(3), DateUtils.DateType.DATA_BASE.getPattern());
                marcacao.ordem = cursor.getInt(4);
                marcacao.latitude = cursor.getDouble(5);
                marcacao.longitude = cursor.getDouble(6);
                marcacoes.add(marcacao);
            } while (cursor.moveToNext());
        }
        return marcacoes;
    }
}
