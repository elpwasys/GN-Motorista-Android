package br.com.wasys.gn.motorista.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.services.DetalhesAgendamentoTransporteService;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalheActivity extends AppCompatActivity {

    private Long id;

    public static final String KEY_ID = DetalheActivity.class.getName() + ".mId";

    @Bind(R.id.mapa_imagem_view) ImageView mMapaImageView;
    @Bind(R.id.txt_motorista) TextView txt_motorista;
    @Bind(R.id.txt_status_confirmado) TextView txt_status_confirmado;
    @Bind(R.id.txt_meia_diaria) TextView txt_meia_diaria;
    @Bind(R.id.txt_codigo)  TextView txt_codigo;
    @Bind(R.id.txt_partida) TextView txt_partida;
    @Bind(R.id.txt_chegada) TextView txt_chegada;
    @Bind(R.id.txt_duracao) TextView txt_duracao;
    @Bind(R.id.txt_data_agendamento) TextView txt_data_agendamento;
    @Bind(R.id.txt_observacoes) TextView txt_observacoes;
    @Bind(R.id.txt_distancia) TextView txt_distancia;
    @Bind(R.id.txt_valor_final) TextView txt_valor;
    @Bind(R.id.txt_tipo_corrida) TextView txt_tipo_corrida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(KEY_ID)) {
                id = extras.getLong(KEY_ID);
            }
        }
        if (id != null) {
            initializeValues();
        }
    }

    public void initializeValues()
    {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DetalhesAgendamentoTransporteService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DetalhesAgendamentoTransporteService service = retrofit.create(DetalhesAgendamentoTransporteService.class);

        Call<ResponseBody> response = service.detalhes(id);

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject response_json = new JSONObject(response.body().string());
                    JSONObject result = response_json.getJSONObject("result");
                    JSONObject solicitacao = result.getJSONObject("solicitacao");
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
                        if (result.has("snapshot")) {
                            String snapshot = result.getString("snapshot");
                            byte[] bytes = Base64.decode(snapshot, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mMapaImageView.setImageBitmap(bitmap);
                        }
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
}
