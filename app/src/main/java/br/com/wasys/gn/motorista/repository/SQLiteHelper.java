package br.com.wasys.gn.motorista.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by pascke on 16/05/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private String[] drop;
    private String[] create;

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    public SQLiteHelper(Context context, String name, int version, String[] drop, String[] create) {
        super(context, name, null, version);
        this.drop = drop;
        this.create = create;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Criando banco de dados " + getDatabaseName());
        if (ArrayUtils.isNotEmpty(create)) {
            for (String sql : create) {
                db.execSQL(sql);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Atualizando banco de dados " + getDatabaseName());
        if (ArrayUtils.isNotEmpty(drop)) {
            for (String sql : drop) {
                db.execSQL(sql);
            }
        }
        onCreate(db);
    }
}
