package br.com.wasys.gn.motorista.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.wasys.gn.motorista.R;

import butterknife.Bind;
import butterknife.OnClick;

public class ChooseTransportActivity extends AppCompatActivity {

    @Bind(R.id.btn_para_mim)
    Button btn_para_mim;

    @Bind(R.id.btn_another_user)
    Button btn_another_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_transport);
    }

    @OnClick(R.id.btn_para_mim)
    public void btn_para_mim(View view) {

    }

    @OnClick(R.id.btn_another_user)
    public void btn_another_user(View view) {

    }

}
