package br.com.wasys.gn.motorista.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.inputmethod.InputMethodManager;

import br.com.wasys.gn.motorista.models.Carro;
import br.com.wasys.gn.motorista.models.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 4/17/16.
 */
public class Helper {

    //public static String BASE_URL = "http://192.168.100.18:8080";
    public static String BASE_URL = "http://mlife02.wasys.com.br:8080";

    public static int MEIA_DIARIA = 0;
    public static int DIARIA = 1;
    public static int PERNOITE = 2;
    public static int TRANSLADO = 3;
    public static final String TAG = "erro" ;
    public static final String PREFS_NAME = "AOP_PREFS_MOTORISTA";

    public static void saveFirstLogin(Context c, String data)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("login", data);
        editor.commit();

        ArrayList<Carro> carros = getCarros(c);
        if (carros != null && carros.size() == 1) {
            Carro carro = carros.get(0);
            set_current_carro_id(c, carro.getId());
        }
    }

    public static Usuario current_user(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        JSONObject response_json = null;
        Usuario obj_usuario = new Usuario();
        try {
            response_json = new JSONObject(json_string);
            obj_usuario.setEmail(response_json.getJSONObject("usuario").getString("email"));
            obj_usuario.setNome(response_json.getJSONObject("usuario").getString("nome"));
            JSONArray colaboradores = response_json.getJSONObject("usuario").getJSONArray("motoristas");
            obj_usuario.setColaborador(colaboradores.getJSONObject(0).getString("id"));
            obj_usuario.setId(colaboradores.getJSONObject(0).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj_usuario;
    }

    public static String getStringLogin(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        return json_string;
    }

    public static boolean firstLogin(Context c)
    {
        System.out.println("Contexto"+c);
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String first_login = settings.getString("login",null);
        if(first_login == null) {
            return true;
        }else
        {
            return false;
        }
    }

    public static void set_current_carro_id(Context c, String carro_id)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("current_carro_id", carro_id);
        editor.commit();

    }

    public static ArrayList<Carro> getCarros(Context c)
    {

        ArrayList<Carro> dados = new ArrayList<Carro>();
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json_string = settings.getString("login", null);
        JSONObject response_json = null;
        try {
            response_json = new JSONObject(json_string);
            System.out.println("JSON USUARIOS FINAL"+response_json.getJSONObject("usuario").getJSONArray("motoristas"));
            JSONArray carros = response_json.getJSONObject("usuario").getJSONArray("motoristas").getJSONObject(0).getJSONArray("carros");
            System.out.println("Array Carros:"+carros);
            for(int i=0;i<carros.length();i++)
            {
                Carro obj_carro = new Carro(carros.getJSONObject(i).getString("id"),carros.getJSONObject(i).getString("modelo"),carros.getJSONObject(i).getString("placa"));
                dados.add(obj_carro);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dados;

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static Carro getCurrentCarro(Context c)
    {
        String carro_id = Helper.get_current_carro_id(c);
        Carro current_carro = null;
        ArrayList<Carro> carros = Helper.getCarros(c);
        for(Carro obj_carro: carros)
        {
            if(obj_carro.getId().equals(carro_id))
            {
                current_carro = obj_carro;
                break;
            }
        }

        return current_carro;
    }

    public static String get_current_carro_id(Context c)
    {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String carro_id = settings.getString("current_carro_id", null);
        return carro_id;
    }

}
