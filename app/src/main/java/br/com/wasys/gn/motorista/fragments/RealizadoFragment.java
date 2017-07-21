package br.com.wasys.gn.motorista.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
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
public class RealizadoFragment extends AppFragment
        implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener, ConfirmDialog.OnConfirmListener,
        LocationListener, ResultCallback<LocationSettingsResult>, OnRequestPermissionsResultCallback {

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

    private Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    // REQUEST
    private static final int REQUEST_CHECK_SETTINGS = 1;

    // PERMISSION
    private static final int PERMISSION_MARCAR = 1;
    private static final int PERMISSION_MY_LOCATION = 2;
    private static final int PERMISSION_MARK_LOCATION = 3;
    private static final int PERMISSION_UPDATE_LOCATION = 4;

    // PARAMETER
    public static final String KEY_ABERTO = RealizadoFragment.class.getSimpleName() + ".mAberto";
    public static final String KEY_ORIGEM = RealizadoFragment.class.getSimpleName() + ".mOrigem";
    public static final String KEY_DESTINO = RealizadoFragment.class.getSimpleName() + ".mDestino";
    public static final String KEY_PEDAGIO = RealizadoFragment.class.getSimpleName() + ".mPedagio";
    public static final String KEY_LOCATION = RealizadoFragment.class.getSimpleName() + ".mLocation";
    public static final String KEY_SOLICITACAO = RealizadoFragment.class.getSimpleName() + ".mSolicitacao";
    public static final String KEY_ESTACIONAMENTO = RealizadoFragment.class.getSimpleName() + ".mEstacionamento";

    private LocationManager mLocationManager;

    private static final int DISTANCE = 50;
    private static final int INTERVAL_MINUTES = 1000 * 30 * 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_realizado, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

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

        mOrigemTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.autocomplete_list_item));
        mMyOrigemImagemView.setOnClickListener(this);

        mDestinoTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getContext(), R.layout.autocomplete_list_item));
        mMyDestinoImagemView.setOnClickListener(this);

        mConcluirButton.setOnClickListener(this);
        mMaisTrechoButton.setOnClickListener(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(INTERVAL_MINUTES);
            mLocationRequest.setFastestInterval(INTERVAL_MINUTES / 2);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(this);

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
    }

    /**
     * Android lifecycle
     ************************************************/
    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
        if (requestCode == PERMISSION_MARCAR || requestCode == PERMISSION_MY_LOCATION || requestCode == PERMISSION_UPDATE_LOCATION) {
            boolean granted = grantedResults(grantResults);
            if (granted) {
                switch (requestCode) {
                    case PERMISSION_MARCAR: {
                        // Do nothing
                        break;
                    }
                    case PERMISSION_MY_LOCATION: {
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
     * GoogleApiClient.ConnectionCallbacks
     ************************************************/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     *
     * GoogleApiClient.OnConnectionFailedListener
     ************************************************/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     *
     * LocationListener
     ************************************************/
    @Override
    public void onLocationChanged(Location location) {
        marcar(location);
    }

    /**
     *
     * ResulCallback
     ************************************************/
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();
        LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // All location settings are satisfied. The client can initialize location requests here.
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    Activity activity = getActivity();
                    status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore the error.
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                break;
        }
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
                setMyLocation(R.id.origen_text_view, R.id.my_origem);
                mDestinoTextView.requestFocus();
                break;
            }
            case R.id.my_destino: {
                setMyLocation(R.id.destino_text_view, R.id.my_destino);
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

    private void setMyLocation(@IdRes int field, @IdRes int image) {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_MY_LOCATION);
            return;
        }
        View view = getView();
        View imageView = view.findViewById(image);
        imageView.setEnabled(false);
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
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
        imageView.setEnabled(true);
    }

    private void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void startLocationUpdates() {
        if (!checkedSelfPermission(Permission.LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), Permission.LOCATION, PERMISSION_UPDATE_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
            if (isBetterLocation(location, mLocation)) {
                Toast.makeText(getContext(), "Location (" + location.getLatitude() + ", " + location.getLongitude() + ")", Toast.LENGTH_SHORT).show();
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

    protected boolean isBetterLocation(Location location, Location currentLocation) {
        if (currentLocation == null) {
            return true;
        }
        float distance = currentLocation.distanceTo(location);
        return distance >= DISTANCE;
    }
}
