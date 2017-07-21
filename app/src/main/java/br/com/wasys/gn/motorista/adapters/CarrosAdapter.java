package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.models.Carro;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 4/26/16.
 */
public class CarrosAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Carro> dados = new ArrayList<Carro>();


    public CarrosAdapter(Context c)
    {
        this.context = c;
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Carro getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        Carro obj_carro = dados.get(position);
        return (long) Long.parseLong(obj_carro.getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Numero de carros:"+dados.size());

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_lista_carro, null);
        Carro obj_carro = dados.get(position);

        System.out.println("Nome do Carro:"+obj_carro.getNome());
        System.out.println("Placa do Carro:" + obj_carro.getPlaca());

        LinearLayout l = (LinearLayout) convertView.findViewById(R.id.layout_item);

        String carro_default_id = Helper.get_current_carro_id(this.context);

        if(carro_default_id != null && !carro_default_id.isEmpty() && obj_carro.getId().equals(carro_default_id))
        {
            l.setBackgroundColor(Color.parseColor("#aeb2b7"));
        }

        TextView txt_carro = (TextView) convertView.findViewById(R.id.txt_nome_do_carro);
        txt_carro.setText(obj_carro.getNome());

        TextView txt_placa = (TextView) convertView.findViewById(R.id.txt_placa_do_carro);
        txt_placa.setText(obj_carro.getPlaca());

        return convertView;
    }
}
