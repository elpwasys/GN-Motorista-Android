package br.com.wasys.library.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Date;

import br.com.wasys.library.utils.DateUtils;

/**
 * Created by pascke on 10/05/16.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json != null) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive != null) {
                String value = primitive.getAsString();
                if (StringUtils.isNotEmpty(value)) {
                    return DateUtils.parse(value);
                }
            }
        }
        return null;
    }
}