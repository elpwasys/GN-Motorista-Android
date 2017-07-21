package br.com.wasys.gn.motorista.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface EsqueciMinhaSenhaService {

    public static final String BASE_URL = "http://mlife02.wasys.com.br:8081/m5/mb/motorista/";
    //public static final String BASE_URL = "http://192.168.1.3:8080/m5/mb/colaborador/";


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

    @POST("gerar_senha")
    Call<ResponseBody> esqueciMinhaSenha(@Body EsqueciMinhaSenhaData esqueciMinhaSenhaData);
}
