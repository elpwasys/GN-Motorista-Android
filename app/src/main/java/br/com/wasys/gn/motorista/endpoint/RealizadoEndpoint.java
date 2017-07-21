package br.com.wasys.gn.motorista.endpoint;

import br.com.wasys.gn.motorista.models.Realizado;

import br.com.wasys.library.http.Result;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by pascke on 30/08/16.
 */
public interface RealizadoEndpoint {

    @POST("realizado/salvar")
    Call<Result> salvar(@Body Realizado realizado);

    @GET("realizado/excluir/{id}")
    Call<Result> excluir(@Path("id") Long id);
}
