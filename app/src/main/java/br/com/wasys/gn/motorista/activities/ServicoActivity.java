package br.com.wasys.gn.motorista.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.Permission;
import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.dialog.CpfDialog;
import br.com.wasys.gn.motorista.dialog.KmDialog;
import br.com.wasys.gn.motorista.endpoint.Endpoint;
import br.com.wasys.gn.motorista.endpoint.SolicitacaoEndpoint;
import br.com.wasys.gn.motorista.fragments.RealizadoFragment2;
import br.com.wasys.gn.motorista.models.Solicitacao;
import br.com.wasys.gn.motorista.processor.SolicitacaoProcessor;
import br.com.wasys.gn.motorista.result.SolicitacaoResult;

import org.apache.commons.lang3.StringUtils;

import br.com.wasys.library.activity.AppActivity;
import br.com.wasys.library.http.AsyncTask;
import br.com.wasys.library.http.Callback;
import br.com.wasys.library.http.Error;
import br.com.wasys.library.utils.BitmapUtils;
import br.com.wasys.library.utils.DateUtils;
import br.com.wasys.library.utils.FieldUtils;
import retrofit2.Call;

public class ServicoActivity extends AppActivity implements CpfDialog.OnConfirmListener, KmDialog.OnConfirmListener {

    private Double mKm;
    private String mCpf;
    private Long mSolicitacaoId;
    private Solicitacao mSolicitacao;

    private TextView mHoraTextView;
    private ImageView mMapImagemView;
    private TextView mDistanciaTextView;
    private RealizadoFragment2 mRealizadoFragment;

    private static final int PERMISSION_PHONE = 1;

    public static final String KEY_SOLICITACAO = ServicoActivity.class.getSimpleName() + ".mSolicitacao";
    public static final String KEY_SOLICITACAO_ID = ServicoActivity.class.getSimpleName() + ".mSolicitacaoId";

    public static Intent newIntent(Context context, Long id) {
        Intent intent = new Intent(context, ServicoActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(KEY_SOLICITACAO_ID, id);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servico);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMapImagemView = (ImageView) findViewById(R.id.image_map_view);
        mHoraTextView = (TextView) findViewById(R.id.hora_text_view);
        mDistanciaTextView = (TextView) findViewById(R.id.distancia_text_view);
        mRealizadoFragment = (RealizadoFragment2) getSupportFragmentManager().findFragmentById(R.id.realizado_fragment);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(KEY_SOLICITACAO_ID)) {
                mSolicitacaoId = extras.getLong(KEY_SOLICITACAO_ID);
            }
        }
    }

    /**
     * Android lifecycle
     ************************************************/
    @Override
    protected void onResume() {
        super.onResume();
        if (mSolicitacao != null) {
            popular();
        }
        else if (mSolicitacaoId != null) {
            carregar();
        }
    }

    @Override
    public void onBackPressed() {
        startMainActivity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(KEY_SOLICITACAO_ID, mSolicitacaoId);
        if (mSolicitacao != null) {
            outState.putSerializable(KEY_SOLICITACAO, mSolicitacao);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSolicitacaoId = savedInstanceState.getLong(KEY_SOLICITACAO_ID);
        if (savedInstanceState.containsKey(KEY_SOLICITACAO)) {
            mSolicitacao = (Solicitacao) savedInstanceState.getSerializable(KEY_SOLICITACAO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_PHONE) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                switch (requestCode) {
                    case PERMISSION_PHONE: {
                        carregar();
                        break;
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickFinalizar(View view) {
        if (mSolicitacaoId != null) {
            int size = mRealizadoFragment.size();
            if (size < 1) {
                Toast.makeText(this, getString(R.string.msg_informe_trechos), Toast.LENGTH_SHORT).show();
            }
            else {
                initDialogs();
            }
        }
    }

    private void popular() {
        if (mSolicitacao == null) {
            finish();
        }
        else {
            Bitmap bitmap = null;
            String snapshot = mSolicitacao.snapshot;
            if (StringUtils.isNotBlank(snapshot)) {
                bitmap = BitmapUtils.toBitmap(snapshot);
            }
            if (bitmap != null) {
                mMapImagemView.setImageBitmap(bitmap);
            }
            else {
                Context context = getBaseContext();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_map);
                mMapImagemView.setImageDrawable(drawable);
            }
            FieldUtils.setText(mDistanciaTextView, mSolicitacao.distanciaTotal);
            FieldUtils.setText(mHoraTextView, DateUtils.format(mSolicitacao.dataInicial, DateUtils.DateType.DATE_BR.getPattern()));
            mRealizadoFragment.popular(mSolicitacao);
        }
    }

    private void carregar() {
        if (!checkedSelfPermission(Permission.PHONE)) {
            ActivityCompat.requestPermissions(this, Permission.PHONE, PERMISSION_PHONE);
            return;
        }
        showProgress();
        if (mSolicitacaoId != null) {
            final Context context = getBaseContext();
            SolicitacaoEndpoint endpoint = Endpoint.create(SolicitacaoEndpoint.class);
            Call<SolicitacaoResult> call = endpoint.obter(mSolicitacaoId);
            call.enqueue(new Callback<SolicitacaoResult>() {
                @Override
                public void onSuccess(SolicitacaoResult result) {
                    mSolicitacao = result.solicitacao;
                    popular();
                    hideProgress();
                }
                @Override
                public void onError(Error error) {
                    hideProgress();
                    finish();
                    Toast.makeText(context, error.getFormattedErrorMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void startMainActivity() {
        Bundle extras = new Bundle();
        extras.putInt(MainActivity.KEY_CURRENT_TAB, MainActivity.TAB_TRANSPORTES_AGENDADOS);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void startAvaliacaoActivity() {
        Bundle extras = new Bundle();
        extras.putLong("id", mSolicitacaoId);
        Intent intent = new Intent(this, AvaliacaoPassageiroActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }

    private void initDialogs() {
        KmDialog dialog = new KmDialog(this);
        dialog.setConfirmListener(this);
        dialog.show();
    }

    @Override
    public void onKmConfirm(Double km) {
        mKm = km;
        CpfDialog dialog = new CpfDialog(this);
        dialog.setConfirmListener(this);
        dialog.show();
    }

    @Override
    public void onCpfConfirm(String cpf) {
        mCpf = cpf;
        finalizar();
    }

    private void finalizar() {
        mRealizadoFragment.stop();
        final Context context = getBaseContext();
        startTask(true, true, new AsyncTask.Task() {
            private boolean error;
            @Override
            public void update() {
                String message = getString(R.string.msg_solicitacao_finalizada_sucesso);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                startAvaliacaoActivity();
            }
            @Override
            public void perform() throws Throwable {
                SolicitacaoProcessor processor = new SolicitacaoProcessor(context);
                processor.finalizar(mSolicitacaoId, mKm, mCpf);
            }
        });
    }
}