package br.com.wasys.gn.motorista.services;

import br.com.wasys.gn.motorista.helpers.Helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by fernandamoncores on 4/22/16.
 */
public interface LoginService {

    //public static final String BASE_URL = "http://mlife02.wasys.com.br:8081/m5/mb/";
    public static final String BASE_URL = Helper.BASE_URL+"/aceleragn/mb/";
    //public static final String BASE_URL = "http://192.168.1.3:8080/m5/mb/";


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

    @POST("login")
    Call<ResponseBody> autentica(@Body LoginData loginData);


}
