package br.com.wasys.gn.motorista.processor;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.endpoint.Endpoint;
import br.com.wasys.gn.motorista.endpoint.GoogleMapsApiEndpoint;
import br.com.wasys.gn.motorista.endpoint.RealizadoEndpoint;
import br.com.wasys.gn.motorista.exception.AppException;
import br.com.wasys.gn.motorista.google.DistanceMatrixResult;
import br.com.wasys.gn.motorista.google.Element;
import br.com.wasys.gn.motorista.google.Row;
import br.com.wasys.gn.motorista.google.Status;
import br.com.wasys.gn.motorista.models.Marcacao;
import br.com.wasys.gn.motorista.models.Realizado;
import br.com.wasys.gn.motorista.models.Solicitacao;
import br.com.wasys.gn.motorista.repository.Filtro;
import br.com.wasys.gn.motorista.repository.MarcacaoRepository;
import br.com.wasys.gn.motorista.repository.RealizadoRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wasys.library.http.Result;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pascke on 30/08/16.
 */
public class RealizadoProcessor {

    private Context context;
    private MarcacaoRepository marcacaoRepository;
    private RealizadoRepository realizadoRepository;

    public RealizadoProcessor(Context context) {
        this.context = context;
        this.marcacaoRepository = new MarcacaoRepository(context);
        this.realizadoRepository = new RealizadoRepository(context);
    }

    public void excluir(Long id) throws AppException {
        try {
            // Envia para o servidor
            RealizadoEndpoint endpoint = Endpoint.create(RealizadoEndpoint.class);
            Call<Result> call = endpoint.excluir(id);
            Response<Result> response = call.execute();
            if (response.isSuccessful()) {
                realizadoRepository.delete(id);
            }
            else {
                throw new AppException("Falha ao sincronizar os dados com servidor. Status code " + response.code());
            }
        } catch (Throwable throwable) {
            if (throwable instanceof AppException) {
                throw (AppException) throwable;
            }
            else {
                throw new AppException(throwable);
            }
        }
    }

    public void salvar(Realizado realizado) throws AppException {
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addresses;
            // ORIGEM
            addresses = geocoder.getFromLocationName(realizado.inicio.completo, 1);
            if (CollectionUtils.isNotEmpty(addresses)) {
                Address address = addresses.get(0);
                realizado.inicio.cidade = address.getLocality();
                realizado.inicio.latitude = address.getLatitude();
                realizado.inicio.longitude = address.getLongitude();
            }
            else {
                throw new AppException(context.getString(R.string.msg_geo_local_nao_encontrado, realizado.inicio.completo));
            }
            // DESTINO
            addresses = geocoder.getFromLocationName(realizado.termino.completo, 1);
            if (CollectionUtils.isNotEmpty(addresses)) {
                Address address = addresses.get(0);
                realizado.termino.cidade = address.getLocality();
                realizado.termino.latitude = address.getLatitude();
                realizado.termino.longitude = address.getLongitude();
            }
            else {
                throw new AppException(context.getString(R.string.msg_geo_local_nao_encontrado, realizado.termino.completo));
            }
            /*
             * Configura os parametros para chamar a Direction API
             */
            Map<String, String> parameters = new HashMap<>();
            parameters.put("origins", realizado.inicio.completo);
            parameters.put("destinations", realizado.termino.completo);
            parameters.put("mode", "driving");
            parameters.put("language", "pt-BR");
            /*
             * Chama a Direction API
             */
            Retrofit directionRetrofit = new Retrofit.Builder()
                    .baseUrl(GoogleMapsApiEndpoint.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GoogleMapsApiEndpoint googleMapsApiEndpoint = directionRetrofit.create(GoogleMapsApiEndpoint.class);
            Call<DistanceMatrixResult> distanceMatrixResultCall = googleMapsApiEndpoint.distanceMatrix(parameters);
            Response<DistanceMatrixResult> distanceMatrixResultResponse = distanceMatrixResultCall.execute();
            String message = null;
            if (distanceMatrixResultResponse.isSuccessful()) {
                DistanceMatrixResult distanceMatrixResult = distanceMatrixResultResponse.body();
                if (Status.OK.equals(distanceMatrixResult.status)) {
                    if (ArrayUtils.isNotEmpty(distanceMatrixResult.rows)) {
                        Row row = distanceMatrixResult.rows[0];
                        if (ArrayUtils.isNotEmpty(row.elements)) {
                            Element element = row.elements[0];
                            if (Status.OK.equals(element.status)) {
                                realizado.duracao = element.duration.value;
                                realizado.distancia = element.distance.value;
                            }
                            else {
                                message = context.getString(R.string.msg_falha_distance_matrix_api, element.status.name());
                            }
                        }
                        else {
                            message = context.getString(R.string.msg_falha_distance_matrix_api_dados_insuficientes);
                        }
                    }
                    else {
                        message = context.getString(R.string.msg_falha_distance_matrix_api_dados_insuficientes);
                    }
                }
                else {
                    if (StringUtils.isNotBlank(distanceMatrixResult.errorMessage)) {
                        message = distanceMatrixResult.errorMessage;
                    }
                    else {
                        message = context.getString(R.string.msg_falha_distance_matrix_api, distanceMatrixResult.status.name());
                    }
                }
            }
            else {
                message = "Falha ao obter as distancias de origem e destino. Distance Matriz API status code " + distanceMatrixResultResponse.code();
            }
            if (StringUtils.isNotBlank(message)) {
                throw new AppException(message);
            }
            realizadoRepository.salvar(realizado);
            Filtro filtro = new Filtro();
            Solicitacao solicitacao = realizado.solicitacao;
            filtro.add(Marcacao.Table.SOLICITACAO_ID, solicitacao.id);
            List<Marcacao> marcacoes = marcacaoRepository.listar(filtro, true);
            if (CollectionUtils.isNotEmpty(marcacoes)) {
                realizado.marcacoes = marcacoes;
            }
            // Envia para o servidor
            RealizadoEndpoint realizadoEndpoint = Endpoint.create(RealizadoEndpoint.class);
            Call<Result> resultCall = realizadoEndpoint.salvar(realizado);
            Response<Result> resultResponse = resultCall.execute();
            if (resultResponse.isSuccessful()) {
                marcacaoRepository.update(realizado);
            }
            else {
                throw new AppException("Falha ao sincronizar os dados com servidor. Status code " + resultResponse.code());
            }
        } catch (Throwable throwable) {
            if (throwable instanceof AppException) {
                throw (AppException) throwable;
            }
            else {
                throw new AppException(throwable);
            }
        }
    }
}
