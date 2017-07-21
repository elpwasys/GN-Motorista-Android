package br.com.wasys.gn.motorista.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.helpers.Mask;
import br.com.wasys.gn.motorista.services.EsqueciMinhaSenhaData;
import br.com.wasys.gn.motorista.services.EsqueciMinhaSenhaService;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EsqueciMinhaSenhaActivity extends AppCompatActivity {

    Context mContext;
    @Bind(R.id.activity_esqueci_minha_senha_cpf)
    EditText cpf;
    @Bind(R.id.activity_esqueci_minha_senha_data_de_nascimento) EditText data_de_nascimento;
    @Bind(R.id.activity_esqueci_minha_senha_senha) EditText senha;
    @Bind(R.id.activity_esqueci_minha_senha_confirmacao_senha) EditText confirmacao_senha;
    @Bind(R.id.txt_invalid_message)
    TextView txt_invalid_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_minha_senha);
        setupActionBar();
        this.mContext = this;
        ButterKnife.bind(this);
        cpf.addTextChangedListener(Mask.insert(Mask.CPF_MASK, cpf));
    }


    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Cadastrar / Esqueci minha senha");
    }

    public void showInvalidMessage(String message)
    {
        txt_invalid_message.setText(message);
        txt_invalid_message.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.activity_esqueci_minha_senha_btn_cadastrar) protected void btnCadastrar() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EsqueciMinhaSenhaService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EsqueciMinhaSenhaService service = retrofit.create(EsqueciMinhaSenhaService.class);

        Call<ResponseBody> response = service.esqueciMinhaSenha(new EsqueciMinhaSenhaData(cpf.getText().toString(),senha.getText().toString(), data_de_nascimento.getText().toString(),confirmacao_senha.getText().toString()));

        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject response_json = new JSONObject(response.body().string());
                    if (response_json.getString("status").equals("SUCCESS")) {

                        final Dialog dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_cadastrar_senha);

                        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ((Activity) mContext).finish();
                            }
                        });
                        dialog.show();


                    } else {
                        showInvalidMessage(response_json.getJSONArray("messages").join(","));
                    }

                } catch (Exception e) {
                    showInvalidMessage("Houve um erro de processamento.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showInvalidMessage("Dados inválidos");
            }

        });


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
}
