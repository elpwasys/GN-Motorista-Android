package br.com.wasys.gn.motorista.processor;

import android.content.Context;

import br.com.wasys.gn.motorista.endpoint.Endpoint;
import br.com.wasys.gn.motorista.endpoint.SolicitacaoEndpoint;
import br.com.wasys.gn.motorista.exception.AppException;
import br.com.wasys.gn.motorista.repository.RealizadoRepository;

import java.io.IOException;

import br.com.wasys.library.http.Result;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pascke on 31/08/16.
 */
public class SolicitacaoProcessor {

    private Context context;
    private RealizadoRepository realizadoRepository;

    public SolicitacaoProcessor(Context context) {
        this.context = context;
        this.realizadoRepository = new RealizadoRepository(context);
    }

    public Result finalizar(Long id, Double km, String cpf) throws AppException {
        SolicitacaoEndpoint endpoint = Endpoint.create(SolicitacaoEndpoint.class);
        Call<Result> call = endpoint.finalizar(id, km, cpf);
        try {
            Response<Result> response = call.execute();
            if (response.isSuccessful()) {
                Result result = response.body();
                if (Result.Status.SUCCESS.equals(result.status)) {
                    realizadoRepository.deleteBySolicitacao(id);
                    return result;
                }
                else {
                    String message = result.getFormattedErrorMessage();
                    throw new AppException(message);
                }
            }
            else {
                throw new AppException("Falha ao sincronizar os dados com servidor. Status code " + response.code());
            }
        } catch (IOException e) {
            throw new AppException(e);
        }
    }
}
