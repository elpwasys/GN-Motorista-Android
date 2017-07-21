package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.models.Requisicao;

import java.util.ArrayList;

/**
 * Created by fernandamoncores on 5/19/16.
 */
public class ItemRequisicaoAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<Requisicao> dados = new ArrayList<Requisicao>();
    public String[] titulos = {"Preciso solicitar revisão do transporte", "Informar algo que ocorreu ao longo do transporte",
    "Tive um problema técnico com o aplicativo", "Outras solicitações ou informações sobre este transporte"};




    public ItemRequisicaoAdapter(Context c)
    {

        createOptions();
        this.context = c;
    }

    public void createOptions()
    {
        for(int i=0;i<=3;i++)
        {
            Requisicao obj_solicitacao = new Requisicao();
            obj_solicitacao.id = i + 1l;
            obj_solicitacao.nome = titulos[i];
            dados.add(obj_solicitacao);
        }
    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Requisicao getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        Requisicao obj_solicitacao = dados.get(position);
        return obj_solicitacao.id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("Numero de carros:"+dados.size());

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.item_lista_solicitacao, null);
        Requisicao obj_solicitacao = dados.get(position);

        TextView lbl_titulo = (TextView) convertView.findViewById(R.id.lbl_titulo);
        lbl_titulo.setText(obj_solicitacao.nome);

        return convertView;
    }

}
