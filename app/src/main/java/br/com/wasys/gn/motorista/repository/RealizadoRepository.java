package br.com.wasys.gn.motorista.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.wasys.gn.motorista.models.Endereco;
import br.com.wasys.gn.motorista.models.Realizado;
import br.com.wasys.gn.motorista.models.Solicitacao;

import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

import br.com.wasys.library.utils.DateUtils;

/**
 * Created by pascke on 24/08/16.
 */
public class RealizadoRepository extends Repository<Realizado> {

    private MarcacaoRepository marcacaoRepository;
    private EnderecoRepository enderecoRepository;
    private static final String TAG = RealizadoRepository.class.getSimpleName();

    public RealizadoRepository(Context context) {
        super(context);
        marcacaoRepository = new MarcacaoRepository(context);
        enderecoRepository = new EnderecoRepository(context);
    }

    public void delete(Long id) {
        Realizado realizado = get(id);
        Endereco inicio = realizado.inicio;
        Endereco termino = realizado.termino;
        enderecoRepository.deleteById(inicio.id);
        enderecoRepository.deleteById(termino.id);
        marcacaoRepository.clear(realizado);
        database.delete(Realizado.Table.NAME, Realizado.Table._ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void deleteBySolicitacao(Long id) {
        marcacaoRepository.deleteBySolicitacao(id);
        Filtro filtro = new Filtro();
        filtro.add(Realizado.Table.SOLICITACAO_ID, id);
        List<Realizado> realizados = listar(filtro);
        if (CollectionUtils.isNotEmpty(realizados)) {
            database.delete(Realizado.Table.NAME, Realizado.Table.SOLICITACAO_ID + " = ?", new String[] { String.valueOf(id) });
            for (Realizado realizado : realizados) {
                Endereco inicio = realizado.inicio;
                Endereco termino = realizado.termino;
                enderecoRepository.deleteById(inicio.id);
                enderecoRepository.deleteById(termino.id);
            }
        }
    }

    public Realizado get(Long id) {
        Filtro filtro = new Filtro();
        filtro.add(Realizado.Table._ID, id);
        List<Realizado> realizados = listar(filtro);
        if (CollectionUtils.isNotEmpty(realizados)) {
            return realizados.get(0);
        }
        return null;
    }

    public void salvar(Realizado realizado) {

        Log.i(TAG, "Salvando Realizado...");

        ContentValues values = new ContentValues();

        Endereco inicio = realizado.inicio;
        Endereco termino = realizado.termino;
        Solicitacao solicitacao = realizado.solicitacao;

        enderecoRepository.salvar(inicio);
        enderecoRepository.salvar(termino);

        values.put(Realizado.Table.INICIO_ID, inicio.id);
        values.put(Realizado.Table.TERMINO_ID, termino.id);
        values.put(Realizado.Table.SOLICITACAO_ID, solicitacao.id);

        values.put(Realizado.Table.DATA, DateUtils.format(realizado.data, DateUtils.DateType.DATA_BASE.getPattern()));
        values.put(Realizado.Table.DURACAO, realizado.duracao);
        values.put(Realizado.Table.DISTANCIA, realizado.distancia);

        if (realizado.pedagio != null) {
            values.put(Realizado.Table.PEDAGIO, realizado.pedagio);
        }
        if (realizado.estacionamento != null) {
            values.put(Realizado.Table.ESTACIONAMENTO, realizado.estacionamento);
        }

        if (realizado.id == null) {
            realizado.id = database.insert(Realizado.Table.NAME, null, values);
        }
        else {
            database.update(Realizado.Table.NAME, values, Realizado.Table._ID + " = ?", new String[] { String.valueOf(realizado.id) });
        }

        Log.i(TAG, "Realizado salvo com sucesso!");
    }

    public List<Realizado> listar(Filtro filtro) {
        String clauses = null;
        String[] arguments = null;
        if (filtro != null) {
            clauses = filtro.getClauses();
            arguments = filtro.getArguments();
        }
        String[] columns = {
                Realizado.Table._ID,
                Realizado.Table.INICIO_ID,
                Realizado.Table.TERMINO_ID,
                Realizado.Table.SOLICITACAO_ID,
                Realizado.Table.DATA,
                Realizado.Table.DURACAO,
                Realizado.Table.DISTANCIA,
                Realizado.Table.PEDAGIO,
                Realizado.Table.ESTACIONAMENTO
        };
        String sortOrder = Realizado.Table._ID + " desc";
        Cursor cursor = database.query(
                Realizado.Table.NAME,
                columns,
                clauses,
                arguments,
                null,
                null,
                sortOrder
        );
        List<Realizado> realizados = null;
        if (cursor.moveToFirst()) {
            realizados = new LinkedList<>();
            // Loop
            do {
                // Realizado
                Realizado realizado = new Realizado();
                realizado.id = cursor.getLong(0);
                realizado.inicio = enderecoRepository.get(cursor.getLong(1));
                realizado.termino = enderecoRepository.get(cursor.getLong(2));
                realizado.solicitacao = new Solicitacao(cursor.getLong(3));
                realizado.data = DateUtils.parse(cursor.getString(4), DateUtils.DateType.DATA_BASE.getPattern());
                realizado.duracao = cursor.getLong(5);
                realizado.distancia = cursor.getLong(6);
                double pedagio = cursor.getDouble(7);
                if (pedagio > 0) {
                    realizado.pedagio = pedagio;
                }
                double estacionamento = cursor.getDouble(8);
                if (estacionamento > 0) {
                    realizado.estacionamento = estacionamento;
                }
                realizados.add(realizado);
            } while (cursor.moveToNext());
        }
        return realizados;
    }
}
