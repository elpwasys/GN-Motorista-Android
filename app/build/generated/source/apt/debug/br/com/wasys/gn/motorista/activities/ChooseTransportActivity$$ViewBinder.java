// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.motorista.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChooseTransportActivity$$ViewBinder<T extends br.com.wasys.gn.motorista.activities.ChooseTransportActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624117, "field 'btn_para_mim' and method 'btn_para_mim'");
    target.btn_para_mim = finder.castView(view, 2131624117, "field 'btn_para_mim'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btn_para_mim(p0);
        }
      });
    view = finder.findRequiredView(source, 2131624118, "field 'btn_another_user' and method 'btn_another_user'");
    target.btn_another_user = finder.castView(view, 2131624118, "field 'btn_another_user'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btn_another_user(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btn_para_mim = null;
    target.btn_another_user = null;
  }
}
