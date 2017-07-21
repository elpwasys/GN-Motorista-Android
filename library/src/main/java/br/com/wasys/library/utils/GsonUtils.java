package br.com.wasys.library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import br.com.wasys.library.gson.DateDeserializer;
import br.com.wasys.library.gson.DateSerializer;

/**
 * Created by pascke on 11/08/16.
 */
public class GsonUtils {

    public static Gson create() {
        return build().create();
    }

    public static GsonBuilder build() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer());
    }
}
