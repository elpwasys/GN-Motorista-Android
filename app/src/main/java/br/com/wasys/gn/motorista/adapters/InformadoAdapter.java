package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.motorista.models.Realizado;

import java.util.List;

import br.com.wasys.library.adapter.ListAdapter;

/**
 * Created by pascke on 26/08/16.
 */
public class InformadoAdapter extends ListAdapter<Realizado> {

    public InformadoAdapter(Context context, List<Realizado> rows) {
        super(context, rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    static class ViewHolder {

        public Button localButton;
        public Button pedagioButton;
        public Button estacionamentoButton;
        public TextView distanciaTextView;
        public AutoCompleteTextView origemAutoCompleteTextView;
        public AutoCompleteTextView destinoAutoCompleteTextView;
    }
}
