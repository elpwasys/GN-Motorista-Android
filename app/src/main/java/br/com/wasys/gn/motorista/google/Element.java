package br.com.wasys.gn.motorista.google;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pascke on 29/08/16.
 */
public class Element {

    @SerializedName("status")
    public Status status;

    @SerializedName("distance")
    public Distance distance;

    @SerializedName("duration")
    public Duration duration;
}
