package br.com.wasys.gn.motorista.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.models.Item;
import br.com.wasys.gn.motorista.services.CalcularDistanciaData;
import br.com.wasys.gn.motorista.services.CalcularDistanciaService;

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
 * Created by fernandamoncores on 4/29/16.
 */

public class AdicionarTrecho extends BaseAdapter implements LocationListener  {

    private Listener listener;
    private LocationManager locationManager;
    private Context context;
    private AdicionarTrecho adapter_context;
    public ArrayList<Item> dados = new ArrayList<Item>();
    boolean isGPSEnabled;
    boolean isNetworkEnabled;


    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        //Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
    }



    public void onStatusChanged(String provider, int status, Bundle extras) {}
    public void onProviderEnabled(String provider) {}
    public void onProviderDisabled(String provider) {}


    public AdicionarTrecho(Context c) {
        this.context = c;
        this.adapter_context = this;
    }


    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Item getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        System.out.println("Entrei na lista");

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_trecho, null);
        final Item obj_item = dados.get(position);
        final Button btn_concluir = (Button) convertView.findViewById(R.id.btn_concluir);
        Button btn_location = (Button) convertView.findViewById(R.id.btn_location);
        Button btn_estacionamento = (Button) convertView.findViewById(R.id.btn_estacionamento);
        Button btn_pedagio = (Button) convertView.findViewById(R.id.btn_pedagio);


        btn_pedagio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_pedagio);
                final EditText edt_km = (EditText) dialog.findViewById(R.id.edt_pedagio);
                if (!TextUtils.isEmpty(obj_item.pedagio)) {
                    edt_km.setText(obj_item.pedagio);
                }
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obj_item.pedagio = edt_km.getText().toString();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        btn_estacionamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_estacionamento);
                final EditText edt_km = (EditText) dialog.findViewById(R.id.edt_km);
                if (!TextUtils.isEmpty(obj_item.estacionamento)) {
                    edt_km.setText(obj_item.estacionamento);
                }
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        obj_item.estacionamento = edt_km.getText().toString();
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });


        final AutoCompleteTextView txt_origem = (AutoCompleteTextView) convertView.findViewById(R.id.txt_origem);
        txt_origem.setAdapter(new GooglePlacesAutocompleteAdapter(this.context, R.layout.autocomplete_list_item));
        txt_origem.setText(obj_item.origem);

        txt_origem.setAdapter(new GooglePlacesAutocompleteAdapter(this.context, R.layout.autocomplete_list_item));
        txt_origem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                System.out.println("Origem Google:" + str);
                obj_item.origem = str;
                dados.get(position).origem = str;
                adapter_context.notifyDataSetChanged();
                txt_origem.setText(str);
                // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        });



        final AutoCompleteTextView txt_destino = (AutoCompleteTextView) convertView.findViewById(R.id.txt_destino);
        final int position_trecho = position;
        txt_destino.setAdapter(new GooglePlacesAutocompleteAdapter(this.context, R.layout.autocomplete_list_item));
        txt_destino.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) parent.getItemAtPosition(position);
                System.out.println("DEsinto Google:" + str);
                dados.get(position_trecho).destino = str;
                obj_item.destino = str;
                adapter_context.notifyDataSetChanged();
                // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
            }
        });

        txt_destino.setText(obj_item.destino);

        final TextView txt_distancia_calculada = (TextView) convertView.findViewById(R.id.txt_distancia_calculada);

        if (position == 0) {
            btn_concluir.setVisibility(View.VISIBLE);
        }
        else {
            Item itemAnterior = dados.get(position - 1);
            if (itemAnterior.concluido) {
                btn_concluir.setVisibility(View.VISIBLE);
            }
        }

        btn_concluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obj_item.origem = txt_origem.getText().toString();
                obj_item.destino = txt_destino.getText().toString();
                dados.get(position_trecho).origem = obj_item.origem;
                dados.get(position_trecho).destino = obj_item.destino;
                dados.get(position_trecho).estacionamento = obj_item.estacionamento;
                dados.get(position_trecho).pedagio = obj_item.pedagio;
                adapter_context.notifyDataSetChanged();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(CalcularDistanciaService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                CalcularDistanciaService service = retrofit.create(CalcularDistanciaService.class);
                System.out.println("Entrei aqui final");
                Call<ResponseBody> response = service.calcular_distancia(new CalcularDistanciaData(obj_item.origem, obj_item.destino));
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            System.out.println("Fiz a request");
                            JSONObject response_json = new JSONObject(response.body().string());
                            if (response_json.getString("status").equals("SUCCESS")) {
                                System.out.println("Entrei aqui maluco:" + response_json.getString("distancia"));
                                obj_item.distancia = response_json.getString("distancia");
                                obj_item.concluido = true;
                                adapter_context.notifyDataSetChanged();

                                if (listener != null) {
                                    listener.concluir();
                                }
                            }
                            System.out.println("Resposta doida:" + response_json);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        System.out.println("Falhei");

                    }
                });
            }
        });


        boolean concluido = obj_item.concluido;
        if (concluido) {
            String distancia = obj_item.distancia;
            if (!TextUtils.isEmpty(distancia)) {
                txt_distancia_calculada.setText(distancia + " km");
            }
        }

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //txt_destino.setText();
                if (txt_destino.hasFocus()) {
                    getCurrentAddress(txt_destino, position);
                } else {
                    getCurrentAddress(txt_origem, position);
                }


            }
        });
        return convertView;
    }

    public void getCurrentAddress(EditText txt_destino, int position) {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(context.LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(context,"GPS desabilitado",Toast.LENGTH_LONG).show();
                //return null;
            } else {
                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   // return null ;

                    Toast.makeText(context,"Sem permissão de acesso!",Toast.LENGTH_LONG).show();
                }

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000,0,this);

                Toast.makeText(context,"Aguarde.....Estamos capturando sua localização!",Toast.LENGTH_LONG).show();
                Location l = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                System.out.println("LOcation:"+l);
                System.out.println("latitude:"+l.getLatitude());
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(l.getLatitude(),l.getLongitude(),1);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String destino = addresses.get(0).getAddressLine(0)+", "+city+" - "+state+", "+country;
                //return addresses.get(0).getLocality();
                txt_destino.setText(destino);
                final Item obj_item = dados.get(position);
                obj_item.destino = destino;
                dados.get(position).destino = destino;
            }

        }catch (Exception e)
        {
            e.printStackTrace();
           // return null;
            Toast.makeText(context,"Em desenvolvimento!",Toast.LENGTH_LONG).show();
        }


    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void concluir();
    }
}
