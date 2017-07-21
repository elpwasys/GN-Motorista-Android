// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.motorista.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AvaliacaoPassageiroActivity$$ViewBinder<T extends br.com.wasys.gn.motorista.activities.AvaliacaoPassageiroActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624072, "field 'txt_motorista'");
    target.txt_motorista = finder.castView(view, 2131624072, "field 'txt_motorista'");
    view = finder.findRequiredView(source, 2131624081, "field 'txt_status_confirmado'");
    target.txt_status_confirmado = finder.castView(view, 2131624081, "field 'txt_status_confirmado'");
    view = finder.findRequiredView(source, 2131624083, "field 'txt_meia_diaria'");
    target.txt_meia_diaria = finder.castView(view, 2131624083, "field 'txt_meia_diaria'");
    view = finder.findRequiredView(source, 2131624088, "field 'txt_codigo'");
    target.txt_codigo = finder.castView(view, 2131624088, "field 'txt_codigo'");
    view = finder.findRequiredView(source, 2131624091, "field 'txt_partida'");
    target.txt_partida = finder.castView(view, 2131624091, "field 'txt_partida'");
    view = finder.findRequiredView(source, 2131624094, "field 'txt_chegada'");
    target.txt_chegada = finder.castView(view, 2131624094, "field 'txt_chegada'");
    view = finder.findRequiredView(source, 2131624097, "field 'txt_duracao'");
    target.txt_duracao = finder.castView(view, 2131624097, "field 'txt_duracao'");
    view = finder.findRequiredView(source, 2131624066, "field 'txt_data_agendamento'");
    target.txt_data_agendamento = finder.castView(view, 2131624066, "field 'txt_data_agendamento'");
    view = finder.findRequiredView(source, 2131624100, "field 'txt_observacoes'");
    target.txt_observacoes = finder.castView(view, 2131624100, "field 'txt_observacoes'");
    view = finder.findRequiredView(source, 2131624101, "field 'btn_aceitar'");
    target.btn_aceitar = finder.castView(view, 2131624101, "field 'btn_aceitar'");
    view = finder.findRequiredView(source, 2131624102, "field 'btn_solicitacao'");
    target.btn_solicitacao = finder.castView(view, 2131624102, "field 'btn_solicitacao'");
    view = finder.findRequiredView(source, 2131624085, "field 'txt_distancia'");
    target.txt_distancia = finder.castView(view, 2131624085, "field 'txt_distancia'");
    view = finder.findRequiredView(source, 2131624075, "field 'txt_valor'");
    target.txt_valor = finder.castView(view, 2131624075, "field 'txt_valor'");
  }

  @Override public void unbind(T target) {
    target.txt_motorista = null;
    target.txt_status_confirmado = null;
    target.txt_meia_diaria = null;
    target.txt_codigo = null;
    target.txt_partida = null;
    target.txt_chegada = null;
    target.txt_duracao = null;
    target.txt_data_agendamento = null;
    target.txt_observacoes = null;
    target.btn_aceitar = null;
    target.btn_solicitacao = null;
    target.txt_distancia = null;
    target.txt_valor = null;
  }
}
