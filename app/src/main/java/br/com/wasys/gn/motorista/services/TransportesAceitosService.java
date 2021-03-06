package br.com.wasys.gn.motorista.services;

import br.com.wasys.gn.motorista.helpers.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fernandamoncores on 4/26/16.
 */
public interface TransportesAceitosService {

    public static final String BASE_URL = Helper.BASE_URL+"/aceleragn/mb/solicitacao/";

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

    @POST("get_confirmadas_by_motorista/{id}")
    Call<ResponseBody> get_confirmadas_by_motorista(@Path("id") long id);
}
