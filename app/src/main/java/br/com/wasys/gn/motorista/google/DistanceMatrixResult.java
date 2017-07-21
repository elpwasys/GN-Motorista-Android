package br.com.wasys.gn.motorista.google;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pascke on 29/08/16.
 */
public class DistanceMatrixResult {

    @SerializedName("status")
    public Status status;

    @SerializedName("error_message")
    public String errorMessage;

    @SerializedName("rows")
    public Row[] rows;
}
