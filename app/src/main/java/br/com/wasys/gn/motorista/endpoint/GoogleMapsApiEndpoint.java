package br.com.wasys.gn.motorista.endpoint;

import br.com.wasys.gn.motorista.BuildConfig;
import br.com.wasys.gn.motorista.google.DistanceMatrixResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by pascke on 16/08/16.
 */
public interface GoogleMapsApiEndpoint {

    public static final String BASE_URL = BuildConfig.GOOGLE_MAPS_API_BASE_URL;

    @GET("distancematrix/json")
    Call<DistanceMatrixResult> distanceMatrix(@QueryMap Map<String, String> parameters);
}
