package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.services.TransportesConfirmadosData;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ListViewMenuAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<TransportesConfirmadosData> records;

    public ListViewMenuAdapter(Context c)
    {
        this.context = c;
    }

    public ListViewMenuAdapter(Context c,ArrayList<TransportesConfirmadosData> records)
    {
        this.context = c;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public TransportesConfirmadosData getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(records.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_menu, null);
        TransportesConfirmadosData obj = records.get(position);
        TextView horario_agendamento = (TextView) convertView.findViewById(R.id.txt_hour_scheduled);
        horario_agendamento.setText(obj.getDataInicial());

        TextView txt_tipo_viagem = (TextView) convertView.findViewById(R.id.txt_tipo_viagem);
        txt_tipo_viagem.setText(obj.getTipo());

        TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
        txt_price.setText("R$ "+obj.getValorTotal());

        TextView txt_distance = (TextView) convertView.findViewById(R.id.txt_distance);
        txt_distance.setText(obj.getDistancia());

        TextView txt_origem = (TextView) convertView.findViewById(R.id.txt_origin);
        txt_origem.setText(obj.getOrigem());

        TextView txt_destiny = (TextView) convertView.findViewById(R.id.txt_destiny);
        txt_destiny.setText(obj.getDestino());

        return convertView;
    }
}
