// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.motorista.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserAgreementActivity$$ViewBinder<T extends br.com.wasys.gn.motorista.activities.UserAgreementActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624063, "field 'actionbar_title'");
    target.actionbar_title = finder.castView(view, 2131624063, "field 'actionbar_title'");
    view = finder.findRequiredView(source, 2131624159, "field 'activity_termos_de_uso_txt_titulo_action_bar'");
    target.activity_termos_de_uso_txt_titulo_action_bar = finder.castView(view, 2131624159, "field 'activity_termos_de_uso_txt_titulo_action_bar'");
    view = finder.findRequiredView(source, 2131624164, "field 'activity_termos_de_uso_switch_termos'");
    target.activity_termos_de_uso_switch_termos = finder.castView(view, 2131624164, "field 'activity_termos_de_uso_switch_termos'");
    view = finder.findRequiredView(source, 2131624165, "field 'activity_termos_de_uso_btn_agree' and method 'btnAgree'");
    target.activity_termos_de_uso_btn_agree = finder.castView(view, 2131624165, "field 'activity_termos_de_uso_btn_agree'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnAgree();
        }
      });
  }

  @Override public void unbind(T target) {
    target.actionbar_title = null;
    target.activity_termos_de_uso_txt_titulo_action_bar = null;
    target.activity_termos_de_uso_switch_termos = null;
    target.activity_termos_de_uso_btn_agree = null;
  }
}
