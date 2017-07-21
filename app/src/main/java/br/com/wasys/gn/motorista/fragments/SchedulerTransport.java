package br.com.wasys.gn.motorista.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.activities.ConfirmTransportActivity;
import br.com.wasys.gn.motorista.activities.DefinirCarroActivity;
import br.com.wasys.gn.motorista.adapters.ListViewMenuAdapter;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.models.Carro;
import br.com.wasys.gn.motorista.services.TransportesConfirmadosData;
import br.com.wasys.gn.motorista.services.TransportesConfirmadosService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class SchedulerTransport extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<TransportesConfirmadosData> records;
    ListViewMenuAdapter adapter;
    Context c;
    ListView list;
    public static int SUCCESS = 123;
    View footerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View android = inflater.inflate(R.layout.fragment_scheduler_transport, container, false);

        setHasOptionsMenu(true);

        records = new ArrayList<TransportesConfirmadosData>();
        footerView =  ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        this.c = getActivity();
        adapter = new ListViewMenuAdapter(getContext(), records);
        list = (ListView) android.findViewById(R.id.list_view_menu);
        list.setAdapter(adapter);
        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        list.setOnItemClickListener(this);
       // setListViewHeightBasedOnItems(list);
        definirFooter();
        return android;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
    }

    public void updateCarName()
    {
//        list.foo
//        View footerView =  ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
//        Carro current_carro = Helper.getCurrentCarro(getActivity());
//        TextView txt_carro = (TextView) footerView.findViewById(R.id.txt_carro);
//        txt_carro.setText(current_carro.getNome() + "-" + current_carro.getPlaca());
    }

    public void definirFooter()
    {

        footerView =  ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        TextView txt_motorista = (TextView) footerView.findViewById(R.id.txt_motorista);
        txt_motorista.setText(Helper.current_user(getActivity()).getNome());
        TextView txt_carro = (TextView) footerView.findViewById(R.id.txt_carro);
        if(Helper.get_current_carro_id(getActivity()) != null) {
            Carro current_carro = Helper.getCurrentCarro(getActivity());
            txt_carro.setText(current_carro.getNome() + "-" + current_carro.getPlaca());
        }else
        {
            txt_carro.setText("Sem carro definido");
        }

        Button btn_change_car = (Button) footerView.findViewById(R.id.btn_change_car);
        btn_change_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, DefinirCarroActivity.class);
                startActivityForResult(i, SUCCESS);
            }
        });

        list.addFooterView(footerView);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SUCCESS) {

            list.removeFooterView(footerView);
            definirFooter();
            //list.invalidate();
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public void loadData()
    {

        try {
            System.out.println("Chamando LoadData");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TransportesConfirmadosService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TransportesConfirmadosService service = retrofit.create(TransportesConfirmadosService.class);

            Call<ResponseBody> response = service.get_pendentes_by_motorista(Long.parseLong(Helper.current_user(getActivity()).getId()));

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject response_json = new JSONObject(response.body().string());
                        if (response_json.getString("status").equals("SUCCESS")) {
                            JSONArray results = response_json.getJSONObject("result").getJSONArray("pendentes");
                            System.out.println("Size"+results.length());
                            System.out.println("REsults:"+results);
                            records = new ArrayList<TransportesConfirmadosData>(results.length());
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                JSONObject itens = result.getJSONArray("itens").getJSONObject(0);
                                System.out.println(result.toString());
                                String origem = itens.getString("cidadeOrigem");
                                String destino = itens.getString("cidadeDestino");
                               // Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                               // System.out.println("Tamanho:"+geocoder.getFromLocationName(origem,10).size());
//                                String cidade_origem = geocoder.getFromLocationName(origem,1).get(0).getLocality();
//                                String cidade_destino = geocoder.getFromLocationName(destino,1).get(0).getLocality();
                                TransportesConfirmadosData obj_transporte = new TransportesConfirmadosData(result.getString("id"), result.getString("dataInicial"), result.getString("tipo"), "", result.getString("distanciaTotal"),result.getString("valorMotorista"),origem,destino,"");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent i = new Intent(getContext(), ConfirmTransportActivity.class);
        Bundle b = new Bundle();
        b.putLong("id",id);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scheduler_transport, menu);
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
