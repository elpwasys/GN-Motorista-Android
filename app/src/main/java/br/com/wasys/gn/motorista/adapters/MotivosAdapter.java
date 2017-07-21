package br.com.wasys.gn.motorista.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.models.Motivo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fernandamoncores on 5/19/16.
 */
public class MotivosAdapter extends BaseAdapter implements View.OnClickListener, View.OnFocusChangeListener {

    private Context context;
    public ArrayList<Motivo> dados = new ArrayList<Motivo>();
    public String[] titulos = {"Houveram pedágios adicionais", "Desvio de rota pelo cliente, com acréscimo de kms", "Tempo maior de disposição do carro e motorista que o previsto", "Outros"};
    public String[] titulos1 = {"Passageiro esqueceu objeto no carro", "Tive um acidente ou problema com o carro", "Outro"};
    public String[] titulos2 = {"Outro"};

    public List<Motivo> selecionados = new ArrayList<>();

    public MotivosAdapter(Context c, int position) {

        createOptions(position);
        this.context = c;
    }

    public void createOptions(int position) {
        if (position == 0) {
            for (int i = 0; i <= 3; i++) {
                Motivo obj_solicitacao = new Motivo();
                obj_solicitacao.setId(String.valueOf(i + 1));
                obj_solicitacao.setNome(titulos[i]);
                dados.add(obj_solicitacao);
            }
        } else if (position == 1) {
            for (int i = 0; i <= 2; i++) {
                Motivo obj_solicitacao = new Motivo();
                obj_solicitacao.setId(String.valueOf(i + 1));
                obj_solicitacao.setNome(titulos1[i]);
                dados.add(obj_solicitacao);
            }
        } else {

            Motivo obj_solicitacao = new Motivo();
            obj_solicitacao.setId(String.valueOf(1));
            obj_solicitacao.setNome(titulos2[0]);
            dados.add(obj_solicitacao);
        }


    }

    @Override
    public int getCount() {
        return dados.size();
    }

    @Override
    public Motivo getItem(int position) {
        return dados.get(position);
    }

    @Override
    public long getItemId(int position) {
        Motivo obj_solicitacao = dados.get(position);
        return (long) Long.parseLong(obj_solicitacao.getId());
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        Motivo motivo = dados.get(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_da_solicitacao, null);
            holder.checkbox = (CheckBox) view.findViewById(R.id.chk);
            holder.motivoTextView = (TextView) view.findViewById(R.id.lbl_titulo);
            holder.observacaoEditText = (EditText) view.findViewById(R.id.observacao_edit_text);
            holder.checkbox.setOnClickListener(this);
            holder.observacaoEditText.setOnFocusChangeListener(this);
            view.setTag(holder);
        }
        holder.position = position;
        holder.checkbox.setTag(holder);
        holder.observacaoEditText.setTag(holder);
        holder.motivoTextView.setText(motivo.getNome());
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            ViewHolder holder = (ViewHolder) checkBox.getTag();
            int visibility = View.GONE;
            if (checkBox.isChecked()) {
                Motivo motivo = dados.get(holder.position);
                selecionados.add(motivo);
                visibility = View.VISIBLE;
            } else {
                selecionados.remove(holder.position);
            }
            holder.observacaoEditText.setVisibility(visibility);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                ViewHolder holder = (ViewHolder) editText.getTag();
                Motivo motivo = dados.get(holder.position);
                Editable editable = editText.getText();
                String text = String.valueOf(editable);
                if (TextUtils.isEmpty(text)) {
                    motivo.setObservacao(null);
                } else {
                    motivo.setObservacao(text);
                }
            }
        }
    }

    static class ViewHolder {
        public int position;
        public CheckBox checkbox;
        public TextView motivoTextView;
        public EditText observacaoEditText;
    }
}