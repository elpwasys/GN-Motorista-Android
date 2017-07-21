package br.com.wasys.library.http;

import com.google.gson.Gson;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import br.com.wasys.library.enumerator.MediaType;
import br.com.wasys.library.utils.GsonUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pascke on 03/08/16.
 */
public class Endpoint {

    public static <T> T create(Class<T> clazz, String baseUrl, final Map<String, String> headers) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", MediaType.APPLICATION_JSON.value);
                if (MapUtils.isNotEmpty(headers)) {
                    Set<Map.Entry<String, String>> entries = headers.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
        Gson gson = GsonUtils.create();
        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .client(client)
                .build();
        T endpoint = retrofit.create(clazz);
        return endpoint;
    }
}