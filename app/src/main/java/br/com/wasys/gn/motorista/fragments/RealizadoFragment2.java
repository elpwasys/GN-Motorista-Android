package br.com.wasys.gn.motorista.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.wasys.gn.motorista.Permission;
import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.adapters.GooglePlacesAutocompleteAdapter;
import br.com.wasys.gn.motorista.dialog.ConfirmDialog;
import br.com.wasys.gn.motorista.models.Endereco;
import br.com.wasys.gn.motorista.models.Marcacao;
import br.com.wasys.gn.motorista.models.Realizado;
import br.com.wasys.gn.motorista.models.Solicitacao;
import br.com.wasys.gn.motorista.models.Trecho;
import br.com.wasys.gn.motorista.processor.RealizadoProcessor;
import br.com.wasys.gn.motorista.repository.MarcacaoRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.com.wasys.library.fragment.AppFragment;
import br.com.wasys.library.http.AsyncTask;
import br.com.wasys.library.utils.DateUtils;
import br.com.wasys.library.utils.FieldUtils;
import br.com.wasys.library.widget.MoneyEditText;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealizadoFragment2 extends AppFragment
        implements OnClickListener, ConfirmDialog.OnConfirmListener, OnRequestPermissionsResultCallback, LocationListener {

    private boolean mAberto;
    private Solicitacao mSolicitacao;
    private List<Realizado> mRealizados;

    private LinearLayout mItensLayout;
    private LinearLayout mAtualLayout;
    private LinearLayout mAnteriorLayout;

    private Button mConcluirButton;
    private Button mMaisTrechoButton;
    private ImageView mMyOrigemImagemView;
    private ImageView mMyDestinoImagemView;
    private MoneyEditText mPedagioEditText;
    private MoneyEditText mEstacionamentoEditText;
    private AutoCompleteTextView mOrigemTextView;
    private AutoCompleteTextView mDestinoTextView;

    private String mProvider;
    private Criteria mCriteria;
    private Location mLocation;
    private LocationManager mLocationManager;

    // REQUEST
    private static final int REQUEST_CHECK_SETTINGS = 1;

    // PERMISSION
    private static final int PERMISSION_STOP_LOCATION = 1;
    private static final int PERMISSION_FIRST_LOCATION = 2;
    private static final int PERMISSION_UPDATE_LOCATION = 3;
    private static final int PERMISSION_CURRENT_LOCATION = 4;

    private static final List<Integer> PERMISSION_REQUEST_CODES = Arrays.asList(new Integer[] {
            PERMISSION_STOP_LOCATION,
            PERMISSION_FIRST_LOCATION,
            PERMISSION_UPDATE_LOCATION,
            PERMISSION_CURRENT_LOCATION
    });

    private static final int MIN_ACCURACY = 5;
    private static final int MIN_DISTANCE = 50;
    private static final int MIN_DURATION = (1000 * 60) / 10;

    // PARAMETER
    public static final String KEY_ABERTO = RealizadoFragment2.class.getSimpleName() + ".mAberto";
    public static final String KEY_ORIGEM = RealizadoFragment2.class.getSimpleName() + ".mOrigem";
    public static final String KEY_DESTINO = RealizadoFragment2.class.getSimpleName() + ".mDestino";
    public static final String KEY_PEDAGIO = RealizadoFragment2.class.getSimpleName() + ".mPedagio";
    public static final String KEY_LOCATION = RealizadoFragment2.class.getSimpleName() + ".mLocation";
    public static final String KEY_SOLICITACAO = RealizadoFragment2.class.getSimpleName() + ".mSolicitacao";
    public static final String KEY_ESTACIONAMENTO = RealizadoFragment2.class.getSimpleName() + ".mEstacionamento";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_realizado, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Context context = getContext();

        mItensLayout = (LinearLayout) view.findViewById(R.id.itens_layout);
        mAtualLayout = (LinearLayout) view.findViewById(R.id.atual_layout);
        mConcluirButton = (Button) view.findViewById(R.id.concluir_button);
        mMaisTrechoButton = (Button) view.findViewById(R.id.mais_trecho_button);
        mAnteriorLayout = (LinearLayout) view.findViewById(R.id.anterior_layout);
        mOrigemTextView = (AutoCompleteTextView) view.findViewById(R.id.origen_text_view);
        mDestinoTextView = (AutoCompleteTextView) view.findViewById(R.id.destino_text_view);
        mPedagioEditText = (MoneyEditText) view.findViewById(R.id.pedagio_edit_text);
        mMyOrigemImagemView = (ImageView) view.findViewById(R.id.my_origem);
        mMyDestinoImagemView = (ImageView) view.findViewById(R.id.my_destino);
        mEstacionamentoEditText = (MoneyEditText) view.findViewById(R.id.estacionamento_edit_text);

        mOrigemTextView.setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.autocomplete_list_item));
        mMyOrigemImagemView.setOnClickListener(this);

        mDestinoTextView.setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.autocomplete_list_item));
        mMyDestinoImagemView.setOnClickListener(this);

        mConcluirButton.setOnClickListener(this);
        mMaisTrechoButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            mSolicitacao = (Solicitacao) savedInstanceState.getSerializable(KEY_SOLICITACAO);
            if (savedInstanceState.containsKey(KEY_ABERTO)) {
                mAberto = savedInstanceState.getBoolean(KEY_ABERTO);
            }
            if (savedInstanceState.containsKey(KEY_ORIGEM)) {
                String origem = savedInstanceState.getString(KEY_ORIGEM);
                FieldUtils.setText(mOrigemTextView, origem);
            }
            if (savedInstanceState.containsKey(KEY_DESTINO)) {
                String destino = savedInstanceState.getString(KEY_DESTINO);
                FieldUtils.setText(mDestinoTextView, destino);
            }
            if (savedInstanceState.containsKey(KEY_PEDAGIO)) {
                double pedagio = savedInstanceState.getDouble(KEY_PEDAGIO);
                FieldUtils.setText(mPedagioEditText, pedagio);
            }
            if (savedInstanceState.containsKey(KEY_ESTACIONAMENTO)) {
                double estacionamento = savedInstanceState.getDouble(KEY_ESTACIONAMENTO);
                FieldUtils.setText(mEstacionamentoEditText, estacionamento);
            }
            if (savedInstanceState.containsKey(KEY_LOCATION)) {
                mLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
        }

        mCriteria = new Criteria();
        mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        mCriteria.setPowerRequirement(Criteria.POWER_LOW);
        mCriteria.setAltitudeRequired(true);
        mCriteria.setBearingRequired(true);
        mCriteria.setSpeedRequired(true);
        mCriteria.setCostAllowed(true);

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        mProvider = mLocationManager.getBestProvider(mCriteria, true);
        Toast.makeText(getContext(), "Provider (" + mProvider + ")", Toast.LENGTH_SHORT).show();
    }

    /**
     * Android lifecycle
     ************************************************/
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setFirstLocation();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(getTag(), "Saving instance state...");
        outState.putBoolean(KEY_ABERTO, mAberto);
        outState.putSerializable(KEY_SOLICITACAO, mSolicitacao);
        String origem = FieldUtils.getValue(mOrigemTextView);
        if (origem != null) {
            outState.putString(KEY_ORIGEM, origem);
        }
        String destino = FieldUtils.getValue(mDestinoTextView);
        if (destino != null) {
            outState.putString(KEY_DESTINO, destino);
        }
        Double pedagio = FieldUtils.getValue(Double.class, mPedagioEditText);
        if (pedagio != null) {
            outState.putDouble(KEY_PEDAGIO, pedagio);
        }
        Double estacionamento = FieldUtils.getValue(Double.class, mEstacionamentoEditText);
        if (estacionamento != null) {
            outState.putDouble(KEY_PEDAGIO, estacionamento);
        }
        if (mLocation != null) {
            outState.putParcelable(KEY_LOCATION, mLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                // ???
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODES.contains(requestCode)) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                switch (requestCode) {
                    case PERMISSION_STOP_LOCATION: {
                        stopLocationUpdates();
                        break;
                    }
                    case PERMISSION_FIRST_LOCATION: {
                        setFirstLocation();
                        break;
                    }
                    case PERMISSION_CURRENT_LOCATION: {
                        // Do nothing
                        break;
                    }
                    case PERMISSION_UPDATE_LOCATION: {
                        startLocationUpdates();
                        break;
                    }
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     *
     * LocationListener
     ************************************************/
    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location)) {
            marcar(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     *
     * View.OnClickListener
     ************************************************/
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.excluir_button: {
                new ConfirmDialog.Builder()
                        .listener(this)
                        .title(R.string.trecho)
                        .message(R.string.deseja_excluir_trecho)
                        .show(getContext());
                break;
            }
            case R.id.concluir_button: {
                concluir();
                break;
            }
            case R.id.mais_trecho_button: {
                mAberto = true;
                mMaisTrechoButton.setVisibility(View.GONE);
                mAtualLayout.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.my_origem: {
                setCurrentLocation(R.id.origen_text_view, R.id.my_origem);
                mDestinoTextView.requestFocus();
                break;
            }
            case R.id.my_destino: {
                setCurrentLocation(R.id.destino_text_view, R.id.my_destino);
                mPedagioEditText.requestFocus();
                break;
            }
        }
    }

    /**
     *
     * ConfirmDialog.OnConfirmListener
     ************************************************/
    @Override
    public void onConfirm(boolean answer) {
        if (answer) {
            if (CollectionUtils.isNotEmpty(mRealizados)) {
                int index = mRealizados.size() - 1;
                excluir(index);
            }
        }
    }

    public int size() {
        return CollectionUtils.size(mRealizados);
    }

    public void stop() {
        stopLocationUpdates();
    }

    private void setFirstLocation() {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_FIRST_LOCATION);
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(mProvider);
        if (location != null) {
            marcar(location);
        }
    }

    private void setCurrentLocation(@IdRes int field, @IdRes int image) {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_CURRENT_LOCATION);
            return;
        }
        View view = getView();
        View imageView = view.findViewById(image);
        imageView.setVisibility(View.INVISIBLE);
        Location location = mLocationManager.getLastKnownLocation(mProvider);
        if (location != null) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (CollectionUtils.isNotEmpty(addresses)) {
                    Address address = addresses.get(0);
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(field);
                    autoCompleteTextView.setText(address.getAddressLine(0));
                }
            } catch (IOException e) {
                Toast.makeText(getContext(), getString(R.string.msg_falha_localizacao_atual), Toast.LENGTH_SHORT).show();
            }
        }
        imageView.setVisibility(View.VISIBLE);
    }

    private void stopLocationUpdates() {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_STOP_LOCATION);
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    private void startLocationUpdates() {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_UPDATE_LOCATION);
            return;
        }
        mLocationManager.requestLocationUpdates(mProvider, MIN_DURATION, 0, this);
    }

    private void concluir() {
        closeVirtualKeyboard();
        StringBuilder errors = new StringBuilder();
        // Origem
        String origem = FieldUtils.getValue(mOrigemTextView);
        if (StringUtils.isBlank(origem)) {
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.origem)));
        }
        // Destino
        String destino = FieldUtils.getValue(mDestinoTextView);
        if (StringUtils.isBlank(destino)) {
            if (StringUtils.isNotBlank(errors)) {
                errors.append("\n");
            }
            errors.append(getString(R.string.msg_campo_obrigatorio, getString(R.string.destino)));
        }
        final Context context = getContext();
        if (StringUtils.isNotBlank(errors)) {
            Toast.makeText(context, errors, Toast.LENGTH_SHORT).show();
        } else {
            // Adiciona o realizado
            final Realizado realizado = new Realizado();
            realizado.data = new Date();
            realizado.inicio = new Endereco(origem);
            realizado.termino = new Endereco(destino);
            realizado.pedagio = FieldUtils.getValue(Double.class, mPedagioEditText);
            realizado.estacionamento = FieldUtils.getValue(Double.class, mEstacionamentoEditText);
            concluir(realizado);
        }
    }

    public void popular(Solicitacao solicitacao) {
        mSolicitacao = solicitacao;
        mRealizados = solicitacao.realizados;
        // SOMENTE SE A ORIGEM NAO FOI INFORMADA
        String origem = FieldUtils.getValue(mOrigemTextView);
        if (StringUtils.isBlank(origem)) {
            List<Trecho> trechos = solicitacao.trechos;
            if (CollectionUtils.isNotEmpty(trechos)) {
                Trecho trecho = trechos.get(0);
                FieldUtils.setText(mOrigemTextView, trecho.inicio.completo);
            }
        }
        popular();
    }

    private void popular() {
        mItensLayout.removeAllViews();
        if (CollectionUtils.isEmpty(mRealizados)) {
            mAberto = true;
            mAnteriorLayout.setVisibility(View.GONE);
        } else {
            for (Realizado realizado : mRealizados) {
                add(realizado);
            }
            toogleExcluirButton();
            mAnteriorLayout.setVisibility(View.VISIBLE);
        }
        if (mAberto) {
            mAtualLayout.setVisibility(View.VISIBLE);
            mMaisTrechoButton.setVisibility(View.GONE);
        } else {
            mAtualLayout.setVisibility(View.GONE);
            mMaisTrechoButton.setVisibility(View.VISIBLE);
        }
    }

    private void add(Realizado realizado) {
        String pattern = "HH'h'mm";
        Context context = getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int count = mItensLayout.getChildCount();
        View view = inflater.inflate(R.layout.item_fragment_realizado, null);
        TextView horaTextView = (TextView) view.findViewById(R.id.hora_text_view);
        TextView enderecoTextView = (TextView) view.findViewById(R.id.endereco_text_view);
        if (count == 0) {
            FieldUtils.setText(horaTextView, DateUtils.format(realizado.data, pattern));
            FieldUtils.setText(enderecoTextView, realizado.inicio.completo);
            mItensLayout.addView(view);
            view = inflater.inflate(R.layout.item_fragment_realizado, null);
            horaTextView = (TextView) view.findViewById(R.id.hora_text_view);
            enderecoTextView = (TextView) view.findViewById(R.id.endereco_text_view);
        }
        if (realizado.distancia != null) {
            TextView distanciaTextView = ButterKnife.findById(view, R.id.distancia_text_view);
            int distancia = (int) (realizado.distancia / 1000);
            FieldUtils.setText(distanciaTextView, getString(R.string.distancia_km, distancia));
        }
        FieldUtils.setText(horaTextView, DateUtils.format(realizado.data, pattern));
        FieldUtils.setText(enderecoTextView, realizado.termino.completo);
        LinearLayout separatorLayout = ButterKnife.findById(view, R.id.separator_layout);
        separatorLayout.setVisibility(View.VISIBLE);
        mItensLayout.addView(view);
        if (mRealizados == null) {
            mRealizados = new LinkedList<>();
            mRealizados.add(realizado);
        }
        if (!mAberto) {
            FieldUtils.setText(mOrigemTextView, realizado.termino.completo);
            FieldUtils.setText(mDestinoTextView, null);
            FieldUtils.setText(mPedagioEditText, null);
            FieldUtils.setText(mEstacionamentoEditText, null);
        }
        if (mAnteriorLayout.getVisibility() == View.GONE) {
            mAnteriorLayout.setVisibility(View.VISIBLE);
        }
        if (mRealizados == null) {
            mRealizados = new ArrayList<>();
        }
        if (!mRealizados.contains(realizado)) {
            mRealizados.add(realizado);
        }
        mDestinoTextView.requestFocus();
    }

    private void toogleExcluirButton() {
        int count = mItensLayout.getChildCount();
        if (count > 0) {
            int last = count - 1;
            for (int i = 0; i < last; i++) {
                View view = mItensLayout.getChildAt(i);
                View button = view.findViewById(R.id.excluir_button);
                button.setOnClickListener(null);
                button.setVisibility(View.GONE);
            }
            View view = mItensLayout.getChildAt(last);
            View button = view.findViewById(R.id.excluir_button);
            button.setOnClickListener(this);
            button.setVisibility(View.VISIBLE);
        }
    }

    private void excluir(final int index) {
        if (CollectionUtils.isNotEmpty(mRealizados)) {
            stopLocationUpdates();
            startTask(true, true, new AsyncTask.Task() {
                @Override
                public void update() {
                    mRealizados.remove(index);
                    popular();
                    startLocationUpdates();
                    Toast.makeText(getContext(), getString(R.string.msg_trecho_excluido_sucesso), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void perform() throws Throwable {
                    Context context = getContext();
                    Realizado realizado = mRealizados.get(index);
                    RealizadoProcessor processor = new RealizadoProcessor(context);
                    processor.excluir(realizado.id);
                }
            });
        }
    }

    /**
     * Insere uma marcacao para a solicitacao
     * @param location
     */
    private void marcar(Location location) {
        if (mSolicitacao != null) {
            //Toast.makeText(getContext(), "Location (" + location.getLatitude() + ", " + location.getLongitude() + ")", Toast.LENGTH_SHORT).show();
            mLocation = location;
            startTask(new AsyncTask.Task() {
                @Override
                public void update() {

                }
                @Override
                public void perform() throws Throwable {
                    Context context = getContext();
                    Marcacao marcacao = new Marcacao();
                    marcacao.data = new Date();
                    marcacao.latitude = mLocation.getLatitude();
                    marcacao.longitude = mLocation.getLongitude();
                    marcacao.solicitacao = new Solicitacao(mSolicitacao.id);
                    MarcacaoRepository marcacaoRepository = new MarcacaoRepository(context);
                    marcacaoRepository.salvar(marcacao);
                }
            });
        }
    }

    public boolean isBetterLocation(Location location) {
        float accuracy = location.getAccuracy();
        Context context = getContext();
        //Toast.makeText(context, "Precisao (" + accuracy + ")", Toast.LENGTH_LONG).show();
        if (accuracy <= MIN_ACCURACY) {
            if (mLocation == null) {
                return true;
            } else {
                float distance = mLocation.distanceTo(location);
                if (distance >= MIN_DISTANCE) {
                    //Toast.makeText(context, "Precisao (" + accuracy + ") \n Distancia (" + distance + ")", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Salva um trecho realizado e sincroniza com o servidor
     * @param realizado
     */
    private void concluir(final Realizado realizado) {
        realizado.solicitacao = new Solicitacao(mSolicitacao.id);
        if (mSolicitacao != null) {
            stopLocationUpdates();
            startTask(true, true, new AsyncTask.Task() {
                private boolean error;
                @Override
                public void update() {
                    if (!error) {
                        mAberto = false;
                        add(realizado);
                        toogleExcluirButton();
                        mAtualLayout.setVisibility(View.GONE);
                        mMaisTrechoButton.setVisibility(View.VISIBLE);
                    }
                    startLocationUpdates();
                    Toast.makeText(getContext(), getString(R.string.msg_trecho_salvo_sucesso), Toast.LENGTH_SHORT).show();
                }
                @Override
                public void perform() throws Throwable {
                    try {
                        Context context = getContext();
                        RealizadoProcessor processor = new RealizadoProcessor(context);
                        processor.salvar(realizado);
                    } catch (Throwable throwable) {
                        error = true;
                        throw throwable;
                    }
                }
            });
        }
    }
}
