package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.services.TransportesConfirmadosData;

import java.util.ArrayList;

import br.com.wasys.library.utils.BitmapUtils;

/**
 * Created by fernandamoncores on 3/28/16.
 */
public class ScheduledTransportAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<TransportesConfirmadosData> records;


    public ScheduledTransportAdapter(Context c,ArrayList<TransportesConfirmadosData> records)
    {
        this.context = c;
        this.records = records;
    }

    public ScheduledTransportAdapter(Context c)
    {
        this.context = c;
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
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(context);
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_scheduled_transport_menu, null);
            holder.mapImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.horarioTextView = (TextView) convertView.findViewById(R.id.txt_hour_scheduled);
            holder.distanciaTextView = (TextView) convertView.findViewById(R.id.txt_pernoite);
            holder.tipoViagemTextView = (TextView) convertView.findViewById(R.id.txt_car);
            convertView.setTag(holder);
        }
        TransportesConfirmadosData obj = records.get(position);
        holder.horarioTextView.setText(obj.getDataInicial());
        holder.distanciaTextView.setText(obj.getDistancia() + " Km");
        holder.tipoViagemTextView.setText(obj.getTipo());
        // Imagem
        String snapshot = obj.getSnapshot();
        if (!TextUtils.isEmpty(snapshot)) {
            Bitmap bitmap = BitmapUtils.toBitmap(snapshot);
            holder.mapImageView.setImageBitmap(bitmap);
        }
        else {
            holder.mapImageView.setImageResource(R.drawable.ic_map);
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView mapImageView;
        public TextView horarioTextView;
        public TextView distanciaTextView;
        public TextView tipoViagemTextView;
    }
}
