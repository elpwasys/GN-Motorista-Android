package br.com.wasys.gn.motorista.google;

import com.google.gson.annotations.SerializedName;

public class Distance {

	@SerializedName("value")
	public long value;
	
	@SerializedName("text")
	public String text;
}
