package br.com.wasys.gn.motorista.activities;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.services.DetalhesAgendamentoTransporteService;
import br.com.wasys.gn.motorista.services.DirectionsService;
import br.com.wasys.gn.motorista.services.IniciarServicoData;
import br.com.wasys.gn.motorista.services.IniciarServicoService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IniciarServicoDetalhesActivity extends AppCompatActivity
        implements View.OnClickListener, OnMapReadyCallback {


    GoogleMap mGoogleMap;
    Bundle savedInstanceState;
    String solicitacao_id = "";
    View dialogView;
    ViewGroup vg;
    final ArrayList<LatLng> final_directions = new ArrayList<LatLng>();
    @Bind(R.id.txt_motorista)
    TextView txt_motorista;
    @Bind(R.id.txt_status_confirmado)
    TextView txt_status_confirmado;
    @Bind(R.id.txt_meia_diaria)
    TextView txt_meia_diaria;
    @Bind(R.id.txt_codigo)
    TextView txt_codigo;
    @Bind(R.id.txt_partida)
    TextView txt_partida;
    @Bind(R.id.txt_chegada)
    TextView txt_chegada;
    @Bind(R.id.txt_duracao)
    TextView txt_duracao;
    @Bind(R.id.txt_data_agendamento)
    TextView txt_data_agendamento;
    @Bind(R.id.txt_observacoes)
    TextView txt_observacoes;
    @Bind(R.id.btn_aceitar)
    Button btn_aceitar;
    @Bind(R.id.txt_distancia)
    TextView txt_distancia;
    @Bind(R.id.txt_valor_final)
    TextView txt_valor;
    @Bind(R.id.txt_tipo_corrida)
    TextView txt_tipo_corrida;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.c = this;
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_iniciar_servico_detalhes);
        MapsInitializer.initialize(this);
        ButterKnife.bind(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vg = (ViewGroup) inflater.inflate(R.layout.dialog_partida, null);
        setTitleActionBar();

        btn_aceitar.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeValues();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    public void initializeValues()
    {

        btn_aceitar.setOnClickListener(this);
        Bundle b = getIntent().getExtras();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetalhesAgendamentoTransporteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("Entrandooooo");

        DetalhesAgendamentoTransporteService service = retrofit.create(DetalhesAgendamentoTransporteService.class);

        Call<ResponseBody> response = service.detalhes(b.getLong("id"));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject response_json = new JSONObject(response.body().string());
                    JSONObject result = response_json.getJSONObject("result");
                    JSONObject solicitacao = result.getJSONObject("solicitacao");
                    solicitacao_id = solicitacao.getString("id");
                    JSONArray trechos = solicitacao.getJSONArray("itens");
                    System.out.println("Teste:" + response_json.toString());
                    if (response_json.getString("status").equals("SUCCESS")) {
                        txt_motorista.setText(result.getJSONObject("colaborador").getString("nome"));
                        txt_data_agendamento.setText(solicitacao.getString("dataInicial"));
                        txt_status_confirmado.setText(solicitacao.getString("situacao"));
                        txt_meia_diaria.setText(solicitacao.getString("tipo"));
                        txt_codigo.setText(solicitacao.getString("codigo"));
                        JSONObject trecho = trechos.getJSONObject(0);
                        txt_partida.setText(trecho.getString("origem"));
                        txt_chegada.setText(trecho.getString("destino"));
                        txt_duracao.setText(solicitacao.getString("distanciaTotal"));
                        txt_tipo_corrida.setText(solicitacao.getString("modo"));
                        txt_distancia.setText(solicitacao.getString("distanciaTotal"));
                        txt_valor.setText("R$ "+trecho.getString("valor"));
                        initializeMaps(savedInstanceState);
                    }
                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setAlertDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Title...");


        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        // Button btn_iniciar = (Button) findViewById(R.id.btn_iniciar_servico);
        //btn_iniciar.setOnClickListener(this);
    }

    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setDisplayHomeAsUpEnabled(true);
        app.setCustomView(R.layout.action_bar);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Transportes Agendados");
    }

    public void initializeMaps(Bundle savedInstanceState)
    {
        Geocoder coder = new Geocoder(this);

        try {
            //Address origem = coder.getFromLocationName("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", 10).get(0);
            System.out.println("Partida:"+txt_partida.getText().toString());
            System.out.println("Chegada:"+txt_chegada.getText().toString());
            Address origem = coder.getFromLocationName(txt_partida.getText().toString(), 1).get(0);
            final LatLng lat_long_origem = new LatLng(origem.getLatitude(), origem.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(lat_long_origem).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_gold_final)));

            //Address destino = coder.getFromLocationName("Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil", 10).get(0);
            Address destino = coder.getFromLocationName(txt_chegada.getText().toString(), 1).get(0);
            final LatLng lat_long_destino = new LatLng(destino.getLatitude(), destino.getLongitude());
            mGoogleMap.addMarker(new MarkerOptions().position(lat_long_destino).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_dark_blue)));

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat_long_origem, 6));

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(DirectionsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class);

            //Call<ResponseBody> response = service.getJson("Rua Alcebíades Plaisant - Água Verde, Curitiba - State of Paraná, Brazil", "Rua Amazonas - Água Verde, Curitiba - State of Paraná, Brazil");
            Call<ResponseBody> response = service.getJson(txt_partida.getText().toString(), txt_chegada.getText().toString());


            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        //System.out.println(response_json);
                        JSONArray routes = response_json.getJSONArray("routes");
                        for (int i = 0; i < routes.length(); i++) {
                            System.out.println("Entrei no routes");
                            JSONArray legs = ((JSONObject) routes.get(i)).getJSONArray("legs");
                            List path = new ArrayList<HashMap<String, String>>();
                            for (int j = 0; j < routes.length(); j++) {
                                System.out.println("Entrei no legs");
                                JSONArray steps = ((JSONObject) legs.get(j)).getJSONArray("steps");
                                for (int k = 0; k < steps.length(); k++) {
                                    System.out.println("Entrei aqui no steps");
                                    String polyline = "";
                                    polyline = (String) ((JSONObject) ((JSONObject) steps.get(k)).get("polyline")).get("points");
                                    System.out.println("PolyLine:" + polyline);
                                    List<LatLng> list = decodePoly(polyline);
                                    for (int l = 0; l < list.size(); l++) {
                                        double lat_string = ((LatLng) list.get(l)).latitude;
                                        double long_string = ((LatLng) list.get(l)).longitude;
                                        System.out.println("latitude,longitude" + lat_string + long_string);
                                        LatLng obj_lat_long = new LatLng(lat_string, long_string);
                                        System.out.println("Antes");
                                        final_directions.add(obj_lat_long);
                                        System.out.println("Depois");
                                    }
                                }

                            }
                        }
                        Polyline line = mGoogleMap.addPolyline(new PolylineOptions().addAll(final_directions)
                                .width(5).color(Color.GRAY).geodesic(true));

                        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                .include(lat_long_origem)
                                .include(lat_long_destino)
                                .build();
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 75));

                    } catch (Exception e) {
                        System.out.println("Aqui no erro");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            System.out.println("Aqui:"+p.latitude);
            poly.add(p);
        }

        return poly;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_aceitar:
                System.out.println("Entrei no botão aceitar");
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_partida);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.setTitle("Title...");
                System.out.println("Entrei no botão aceitar 1");
                final AutoCompleteTextView dialog_partida = (AutoCompleteTextView) dialog.findViewById(R.id.edt_local_de_partida);
                dialog_partida.setText(txt_partida.getText());
                System.out.println("Entrei no botão aceitar 2");

                final EditText edt_km = (EditText) dialog.findViewById(R.id.edt_km);

             //   Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
//
               Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(IniciarServicoService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                System.out.println("SOLICITACAO ID:"+solicitacao_id);
                System.out.println("Carro Id" + Helper.get_current_carro_id(c));

                        IniciarServicoService service = retrofit.create(IniciarServicoService.class);

                Call<ResponseBody> response =  service.aceitar(new IniciarServicoData(solicitacao_id,dialog_partida.getText().toString(),edt_km.getText().toString()));

                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{

                            JSONObject response_json = new JSONObject(response.body().string());
                            System.out.println("Resposta do Serviço"+response_json.toString());
                            if (response_json.getString("status").equals("SUCCESS")) {
                                Toast.makeText(c, "Serviço Iniciado!", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                Intent intent = ServicoActivity.newIntent(c, Long.parseLong(solicitacao_id));
                                startActivity(intent);
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });
//
            dialog.show();
                break;
        }
    }
}
