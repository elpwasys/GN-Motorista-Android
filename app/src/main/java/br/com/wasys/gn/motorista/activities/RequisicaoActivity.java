package br.com.wasys.gn.motorista.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.adapters.ItemRequisicaoAdapter;
import br.com.wasys.gn.motorista.models.Requisicao;

import butterknife.ButterKnife;

public class RequisicaoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView list;
    ItemRequisicaoAdapter adapter;
    String solicitacao_id;

    private static final int REQUEST_ITEM_REQUISICAO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisicao);
        ButterKnife.bind(this);
        setTitleActionBar();

        Bundle extras = getIntent().getExtras();

        solicitacao_id = extras.getString("solicitacao_id");
        list = (ListView) findViewById(R.id.list_view_opcoes);
        adapter = new ItemRequisicaoAdapter(this);
//        adapter.dados = Helper.getCarros(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Solicitações");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Requisicao obj_selected = adapter.getItem(position);
        Intent i = new Intent(this, ItemSolicitacaoActivity.class);
        i.putExtra("tipo_solicitacao",id);
        i.putExtra("solicitacao_id",solicitacao_id);
        i.putExtra("position",position);

        startActivityForResult(i, REQUEST_ITEM_REQUISICAO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ITEM_REQUISICAO) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
