package br.com.wasys.gn.motorista.services;

import br.com.wasys.gn.motorista.helpers.Helper;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by fernandamoncores on 5/19/16.
 */
public interface SolicatacaoService {

    public static final String BASE_URL = Helper.BASE_URL+"/aceleragn/mb/requisicao/";

    @Headers({
            "deviceSO: 1",
            "deviceIMEI: 1",
            "deviceModel: 1",
            "deviceWidth: 1",
            "deviceHeight: 1",
            "deviceSOVersion: 1",
            "deviceAppVersion: 1",
            //"userID: 1",
            "Content-Type: application/json"
    })

    @POST("criar")
    Call<ResponseBody> criarSolicitacao(@Body JSONObject dados);
}
