// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.motorista.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends br.com.wasys.gn.motorista.activities.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624138, "field 'login'");
    target.login = finder.castView(view, 2131624138, "field 'login'");
    view = finder.findRequiredView(source, 2131624139, "field 'senha'");
    target.senha = finder.castView(view, 2131624139, "field 'senha'");
    view = finder.findRequiredView(source, 2131624128, "field 'message'");
    target.message = finder.castView(view, 2131624128, "field 'message'");
    view = finder.findRequiredView(source, 2131624141, "method 'btnEsqueciMinhaSenha'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnEsqueciMinhaSenha();
        }
      });
    view = finder.findRequiredView(source, 2131624140, "method 'btnEntrar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnEntrar();
        }
      });
  }

  @Override public void unbind(T target) {
    target.login = null;
    target.senha = null;
    target.message = null;
  }
}
