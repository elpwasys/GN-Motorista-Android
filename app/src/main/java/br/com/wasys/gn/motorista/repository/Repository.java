package br.com.wasys.gn.motorista.repository;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RawRes;

import br.com.wasys.gn.motorista.R;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by pascke on 16/05/16.
 */
public abstract class Repository<T> {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "m5.db";

    protected String table;

    protected SQLiteOpenHelper helper;
    protected SQLiteDatabase database;

    public Repository(Context context) {

        String[] drop = null;
        String[] create = null;

        try {
            String sql;
            // DROP
            sql = getSQL(context, R.raw.drop);
            if (StringUtils.isNotBlank(sql)) {
                drop = sql.split(";");
            }
            // CREATE
            sql = getSQL(context, R.raw.create);
            if (StringUtils.isNotBlank(sql)) {
                create = sql.split(";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        helper = new SQLiteHelper(context, DB_NAME, DB_VERSION, drop, create);
        database = helper.getWritableDatabase();
    }

    private String getSQL(Context context, @RawRes int rawId) throws IOException {

        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(rawId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line = bufferedReader.readLine();
        StringBuilder builder = new StringBuilder();

        while (line != null) {
            builder.append(line);
            line = bufferedReader.readLine();
        }

        bufferedReader.close();

        return String.valueOf(builder);
    }
}