package br.com.wasys.gn.motorista.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.services.LoginData;
import br.com.wasys.gn.motorista.services.LoginService;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "erro" ;
    public static final String PREFS_NAME = "AOP_PREFS";
    @Bind(R.id.activity_login_txtEmail)
    EditText login;
    @Bind(R.id.activity_login_txtSenha) EditText senha;
    @Bind(R.id.txt_invalid_message)
    TextView message;
    private Context ctx;
    SharedPreferences settings;

    TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            message.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.ctx = this;
        ButterKnife.bind(this);
        login.addTextChangedListener(inputTextWatcher);
        senha.addTextChangedListener(inputTextWatcher);
        if(!Helper.firstLogin(this))
        {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    public void showMessageInvalidLogin()
    {
        message.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.activity_login_btn_esqueci_minha_senha) protected  void btnEsqueciMinhaSenha()
    {

        Intent intent = new Intent(this, EsqueciMinhaSenhaActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.activity_login_btn_entrar) protected void btnEntrar() {
        //Intent intent = new Intent(this, UserAgreementActivity.class);
        //startActivity(intent);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LoginService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoginService service = retrofit.create(LoginService.class);
        Call<ResponseBody> response = service.autentica(new LoginData(login.getText().toString(), senha.getText().toString(), true));

        HttpUrl url = response.request().url();
        System.out.println(url);


        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONObject response_json = new JSONObject(response.body().string());
                    System.out.println("JSON RESPONSE:"+response_json.toString());
                    if(response_json.getString("status").equals("SUCCESS"))
                    {
                        message.setVisibility(View.GONE);
                        if(Helper.firstLogin(ctx)) {
                            Helper.saveFirstLogin(ctx, response_json.toString());
                            Intent intent = new Intent(ctx, UserAgreementActivity.class);
                            Bundle extras = new Bundle();
                            extras.putBoolean("from_termo",false);
                            intent.putExtras(extras);
                            startActivity(intent);
                        }else
                        {
                            Intent intent = new Intent(ctx, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }else
                    {
                        showMessageInvalidLogin();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showMessageInvalidLogin();
            }

        });
    }
}
