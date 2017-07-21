// Generated code from Butter Knife. Do not modify!
package br.com.wasys.gn.motorista.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class TutorialActivity$$ViewBinder<T extends br.com.wasys.gn.motorista.activities.TutorialActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624154, "field 'mViewPager'");
    target.mViewPager = finder.castView(view, 2131624154, "field 'mViewPager'");
    view = finder.findRequiredView(source, 2131624155, "field 'titleIndicator'");
    target.titleIndicator = finder.castView(view, 2131624155, "field 'titleIndicator'");
    view = finder.findRequiredView(source, 2131624156, "field 'btn_ja_entendi' and method 'btnJaEntendi'");
    target.btn_ja_entendi = finder.castView(view, 2131624156, "field 'btn_ja_entendi'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.btnJaEntendi(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mViewPager = null;
    target.titleIndicator = null;
    target.btn_ja_entendi = null;
  }
}
