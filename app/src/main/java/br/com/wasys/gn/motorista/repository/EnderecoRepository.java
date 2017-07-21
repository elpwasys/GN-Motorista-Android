package br.com.wasys.gn.motorista.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.wasys.gn.motorista.models.Endereco;

import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by pascke on 24/08/16.
 */
public class EnderecoRepository extends Repository<Endereco> {

    private static final String TAG = EnderecoRepository.class.getSimpleName();

    public EnderecoRepository(Context context) {
        super(context);
    }

    public Endereco get(Long id) {
        Filtro filtro = new Filtro();
        filtro.add(Endereco.Table._ID, id);
        List<Endereco> enderecos = listar(filtro);
        if (CollectionUtils.isNotEmpty(enderecos)) {
            return enderecos.get(0);
        }
        return null;
    }

    public void deleteById(Long id) {
        database.delete(Endereco.Table.NAME, Endereco.Table._ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void salvar(Endereco endereco) {

        Log.i(TAG, "Salvando Endereco...");

        ContentValues values = new ContentValues();

        values.put(Endereco.Table.CIDADE, endereco.cidade);
        values.put(Endereco.Table.COMPLETO, endereco.completo);
        values.put(Endereco.Table.LATITUDE, endereco.latitude);
        values.put(Endereco.Table.LONGITUDE, endereco.longitude);

        if (endereco.id == null) {
            endereco.id = database.insert(Endereco.Table.NAME, null, values);
        }
        else {
            database.update(Endereco.Table.NAME, values, Endereco.Table._ID + " = ?", new String[] { String.valueOf(endereco.id) });
        }

        Log.i(TAG, "Endereco salvo com sucesso!");
    }

    public List<Endereco> listar(Filtro filtro) {
        String clauses = null;
        String[] arguments = null;
        if (filtro != null) {
            clauses = filtro.getClauses();
            arguments = filtro.getArguments();
        }
        String[] columns = {
                Endereco.Table._ID,
                Endereco.Table.CIDADE,
                Endereco.Table.COMPLETO,
                Endereco.Table.LATITUDE,
                Endereco.Table.LONGITUDE
        };
        String sortOrder = Endereco.Table._ID + " desc";
        Cursor cursor = database.query(
                Endereco.Table.NAME,
                columns,
                clauses,
                arguments,
                null,
                null,
                sortOrder
        );
        List<Endereco> enderecos = null;
        if (cursor.moveToFirst()) {
            enderecos = new LinkedList<>();
            // Loop
            do {
                // Endereco
                Endereco endereco = new Endereco();
                endereco.id =  cursor.getLong(0);
                endereco.cidade = cursor.getString(1);
                endereco.completo = cursor.getString(2);
                endereco.latitude = cursor.getDouble(3);
                endereco.longitude = cursor.getDouble(4);
                enderecos.add(endereco);
            } while (cursor.moveToNext());
        }
        return enderecos;
    }
}
