package br.com.wasys.gn.motorista.endpoint;

import br.com.wasys.gn.motorista.result.SolicitacaoResult;

import br.com.wasys.library.http.Result;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by pascke on 30/08/16.
 */
public interface SolicitacaoEndpoint {

    @GET("solicitacao/obter/{id}")
    Call<SolicitacaoResult> obter(@Path("id") Long id);

    @GET("solicitacao/finalizar/{id}/{km}/{cpf}")
    Call<Result> finalizar(@Path("id") Long id, @Path("km") Double km, @Path("cpf") String cpf);
}
