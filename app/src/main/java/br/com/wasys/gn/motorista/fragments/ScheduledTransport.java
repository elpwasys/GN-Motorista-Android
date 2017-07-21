package br.com.wasys.gn.motorista.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.activities.IniciarServicoDetalhesActivity;
import br.com.wasys.gn.motorista.activities.ServicoActivity;
import br.com.wasys.gn.motorista.adapters.ScheduledTransportAdapter;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.models.Carro;
import br.com.wasys.gn.motorista.services.EnviarLocalizacaoData;
import br.com.wasys.gn.motorista.services.EnviarLocalizacaoService;
import br.com.wasys.gn.motorista.services.TransportesAceitosService;
import br.com.wasys.gn.motorista.services.TransportesConfirmadosData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ScheduledTransport extends Fragment implements AdapterView.OnItemClickListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    final Handler locationHandler = new Handler();
    private ArrayList<TransportesConfirmadosData> records;
    ScheduledTransportAdapter adapter;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;

    private static final int REQUEST_LOCATION = 1;

    Runnable locationRunnable = new Runnable() {
        @Override
        public void run() {
            sendLocation();
            locationHandler.postDelayed(this, 10000 * 60 * 1);
        }
    };

    private LocationManager locationManager;


    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        //Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
    }



    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}

    public void sendLocation() {
        boolean granted = true;
        String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        };
        Context context = getContext();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }
        if (granted) {
            processLocation();
        }
        else {
            FragmentActivity activity = getActivity();
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                boolean granted = true;
                if (grantResults != null) {
                    for (int grantResult : grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            granted = false;
                            break;
                        }
                    }
                }
                if (granted) {
                    processLocation();
                }
                break;
            }
        }
    }

    public void processLocation() {
        try {
            System.out.println("Pegando a location");
            locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getActivity(), "GPS desabilitado", Toast.LENGTH_LONG).show();
                //return null;
            } else {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // return null ;

                    Toast.makeText(getActivity(),"Sem permissão de acesso!",Toast.LENGTH_LONG).show();
                }

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,0,this);
                Location l = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                System.out.println("LOcation:"+l);
                System.out.println("latitude:"+l.getLatitude());
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(), l.getLongitude(), 1);
                System.out.println("Pegando a latitude e longitude" + l.getLatitude() + "," + l.getLongitude());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(EnviarLocalizacaoService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                EnviarLocalizacaoService service = retrofit.create(EnviarLocalizacaoService.class);
                String motorista_id = Helper.current_user(getActivity()).getColaborador();
                String carro_id = Helper.get_current_carro_id(getActivity());
                Call<ResponseBody> response = service.enviarLocalizacao(new EnviarLocalizacaoData(motorista_id,carro_id,String.valueOf(l.getLatitude()),String.valueOf(l.getLongitude())));
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            System.out.println("Entrei aquiiiiii");
                            JSONObject response_json = new JSONObject(response.body().string());
                            System.out.println("JSON RESPONSE:" + response_json.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("Está dando erro");

                    }

                });


            }

        }catch (Exception e)
        {
            e.printStackTrace();
            // return null;
            //Toast.makeText(getActivity(),"Em desenvolvimento!",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_scheduled_transport, container, false);

        setHasOptionsMenu(true);

        records = new ArrayList<TransportesConfirmadosData>();
        locationHandler.post(locationRunnable);
        adapter = new ScheduledTransportAdapter(getContext(), records);

        ListView list = (ListView) android.findViewById(R.id.list_view_menu);
        list.setAdapter(adapter);

        /*
         * Everton Luiz Pascke
         * Ajuste Lup 141
         *
        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        list.setOnItemClickListener(this);
        //setListViewHeightBasedOnItems(list);
        return android;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

    public void loadData()
    {
        try {
            System.out.println("Chamando LoadData");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TransportesAceitosService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TransportesAceitosService service = retrofit.create(TransportesAceitosService.class);
            Call<ResponseBody> response = service.get_confirmadas_by_motorista(Long.parseLong(Helper.current_user(getActivity()).getId()));
            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        if (response_json.getString("status").equals("SUCCESS")) {
                            System.out.println("Resposta Final:"+response_json.getJSONObject("result"));
                            JSONArray results = response_json.getJSONObject("result").getJSONArray("confirmadas");
                            System.out.println("Size"+results.length());
                            System.out.println("REsults:"+results);
                            records = new ArrayList<TransportesConfirmadosData>(results.length());
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                JSONObject itens = result.getJSONArray("itens").getJSONObject(0);
                                System.out.println(result.toString());
                                String origem = itens.getString("cidadeOrigem");
                                String destino = itens.getString("cidadeDestino");
                                TransportesConfirmadosData obj_transporte = new TransportesConfirmadosData(result.getString("id"), result.getString("dataInicial"), result.getString("tipo"), "", result.getString("distanciaTotal"),result.getString("valorTotal"),origem,destino,result.getString("situacao"));
                                if (result.has("snapshot")) {
                                    obj_transporte.setSnapshot(result.getString("snapshot"));
                                }
                                records.add(obj_transporte);
                            }
                            adapter.records = records;
                            adapter.notifyDataSetChanged();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }

            });
        } catch (Exception e) {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Context context = getContext();
        Carro carro = Helper.getCurrentCarro(context);
        if (carro == null) {
            Toast.makeText(context, context.getString(R.string.msg_selecionar_veiculo), Toast.LENGTH_SHORT).show();
        }
        else {
            TransportesConfirmadosData obj_transporte = adapter.getItem(position);
            if(obj_transporte.getSituacao().equals("ANDAMENTO"))
            {
                Intent intent = ServicoActivity.newIntent(context, id);
                startActivity(intent);
            }
            else
            {
                Bundle b = new Bundle();
                Intent i = new Intent(context, IniciarServicoDetalhesActivity.class);
                b.putLong("id",id);
                i.putExtras(b);
                startActivity(i);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scheduled_transport, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu:
                loadData();
                return true;
            default:
                break;
        }
        return false;
    }
}
