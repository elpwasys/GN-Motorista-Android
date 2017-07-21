package br.com.wasys.gn.motorista.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.adapters.CarrosAdapter;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.models.Carro;
import br.com.wasys.gn.motorista.services.CarrosService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefinirCarroActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    CarrosAdapter adapter;
    ListView list;
    ArrayList<Carro> dados = new ArrayList<Carro>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_carro);
        setupActionBar();

        loadData();

        list = (ListView) findViewById(R.id.list_view_carros);
        adapter = new CarrosAdapter(this);
//        adapter.dados = Helper.getCarros(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    public void loadData()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CarrosService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CarrosService service = retrofit.create(CarrosService.class);
        Call<ResponseBody> response = service.get_carros(Long.parseLong(Helper.current_user(this).getColaborador()));
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE Carro:"+response_json.toString());
                    if(response_json.getString("status").equals("SUCCESS"))
                    {
                       JSONArray carros = response_json.getJSONObject("result").getJSONArray("carros");
                        for(int i=0;i<carros.length();i++)
                        {
                            JSONObject carro_json = carros.getJSONObject(i);
                            Carro obj_carro = new Carro(carro_json.getString("id"),carro_json.getString("modelo"),carro_json.getString("placa"));
                            dados.add(obj_carro);
                        }

                        adapter.dados = dados;
                        adapter.notifyDataSetChanged();
                    }else
                    {

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

    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Escolher o carro atual");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //adapter.notifyDataSetChanged();
        Carro obj_selected = adapter.getItem(position);
        Helper.set_current_carro_id(this, obj_selected.getId());
        adapter.notifyDataSetChanged();
        this.finish();
    }
}
