package br.com.wasys.gn.motorista.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import br.com.wasys.gn.motorista.R;

import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserAgreementActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.actionbar_title) TextView actionbar_title;
    @Bind(R.id.activity_termos_de_uso_txt_titulo) TextView activity_termos_de_uso_txt_titulo_action_bar;
    @Bind(R.id.activity_termos_de_uso_switch_termos)
    SwitchCompat activity_termos_de_uso_switch_termos;
    @Bind(R.id.activity_termos_de_uso_btn_agree)
    Button activity_termos_de_uso_btn_agree;
    private boolean from_termo = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        Bundle b = getIntent().getExtras();
        from_termo = b.getBoolean("from_termo",true);
        setupActionBar();
        ButterKnife.bind(this);
        intializeView();
    }

    public void intializeView() {
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Bold.otf");
        activity_termos_de_uso_txt_titulo_action_bar.setTypeface(myCustomFont);
        actionbar_title.setText(R.string.activity_user_agreement_termos_de_uso);
        actionbar_title.setTypeface(myCustomFont);
        activity_termos_de_uso_switch_termos.setChecked(false);
        activity_termos_de_uso_switch_termos.setOnCheckedChangeListener(this);
        activity_termos_de_uso_btn_agree.setClickable(false);
        activity_termos_de_uso_btn_agree.setEnabled(false);
        if (from_termo)
        {
            activity_termos_de_uso_switch_termos.setVisibility(View.GONE);
            activity_termos_de_uso_btn_agree.setVisibility(View.GONE);
        }
    }

    public void setupActionBar() {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        if(from_termo)
            app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Termos de Uso");
    }

    @OnClick(R.id.activity_termos_de_uso_btn_agree) protected void btnAgree() {
        Intent intent = new Intent(this, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.activity_termos_de_uso_switch_termos:
                if (buttonView.isChecked())
                {
                    activity_termos_de_uso_btn_agree.setClickable(true);
                    activity_termos_de_uso_btn_agree.setEnabled(true);
                }else{
                    Toast.makeText(this, "Você deve aceitar os termos...", Toast.LENGTH_LONG);
                    activity_termos_de_uso_btn_agree.setClickable(false);
                    activity_termos_de_uso_btn_agree.setEnabled(false);
                }
                break;
        }
    }
}