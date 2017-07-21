package br.com.wasys.gn.motorista.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.adapters.MotivosAdapter;
import br.com.wasys.gn.motorista.models.Motivo;
import br.com.wasys.gn.motorista.services.SolicatacaoService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemSolicitacaoActivity extends AppCompatActivity {

    ListView list;
    MotivosAdapter adapter;
    String solicitacao_id;
    String tipo_solicitacao;
    int position;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_solicitacao);
        ButterKnife.bind(this);
        setTitleActionBar();

        Bundle extras = getIntent().getExtras();
        this.mContext = this;
        solicitacao_id = extras.getString("solicitacao_id");
        position = extras.getInt("position",-1);
        System.out.println("Posição:"+position);
        tipo_solicitacao = String.valueOf(extras.getLong("tipo_solicitacao"));
        System.out.println("Tipo Solicitação:"+tipo_solicitacao);
        list = (ListView) findViewById(R.id.list_view_opcoes);
        adapter = new MotivosAdapter(this,position);
//        adapter.dados = Helper.getCarros(this);
        list.setAdapter(adapter);
       // list.setOnItemClickListener(this);
    }

    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Solicitações");
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

    @OnClick(R.id.btn_enviar)
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_enviar:
                System.out.println("Enviar");
                System.out.println("Numero de selecionados: "+adapter.selecionados.size());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(SolicatacaoService.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                SolicatacaoService service = retrofit.create(SolicatacaoService.class);
                JSONObject dados = new JSONObject();

                try {
                    dados.put("solicitacao_id",solicitacao_id);
                    dados.put("tipo_solicitacao",tipo_solicitacao);
                    JSONArray motivos = new JSONArray();

                    if (!adapter.selecionados.isEmpty()) {
                        for (Motivo motivo : adapter.selecionados) {
                            motivos.put(motivo);
                        }
                    }

                    dados.put("motivos", motivos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<ResponseBody> response = service.criarSolicitacao(dados);
                response.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {

                            JSONObject response_json = new JSONObject(response.body().string());
                            System.out.println("JSON RESPONSE:" + response_json.toString());
                            if (response_json.getString("status").equals("SUCCESS")) {
                                Toast.makeText(mContext,"Solicitação enviada com sucesso!",Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK);
                                ItemSolicitacaoActivity.this.finish();
                            } else {
                                Toast.makeText(mContext,"Solicitação não pode ser enviada. Contate o administrador da plataforma!",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(mContext,"Solicitação não pode ser enviada. Contate o administrador da plataforma!",Toast.LENGTH_LONG).show();
                    }

                });
                break;
        }
    }


}
