package br.com.wasys.library.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

import br.com.wasys.library.utils.DateUtils;

/**
 * Created by pascke on 10/05/16.
 */
public class DateSerializer implements JsonSerializer<Date> {
    @Override
    public JsonElement serialize(Date src, Type type, JsonSerializationContext context) {
        if (src != null) {
            return new JsonPrimitive(DateUtils.format(src));
        }
        return null;
    }
}
