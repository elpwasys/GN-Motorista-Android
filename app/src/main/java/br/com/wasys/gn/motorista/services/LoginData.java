package br.com.wasys.gn.motorista.services;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fernandamoncores on 4/22/16.
 */
public class LoginData {

    @SerializedName("email")
    String username;
    @SerializedName("senha")
    String password;
    @SerializedName("motorista")
    boolean motorista;


    public LoginData(String username, String password, boolean motorista) {
        this.username = username;
        this.password = password;
        this.motorista = motorista;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
