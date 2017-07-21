package br.com.wasys.gn.motorista.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;
import br.com.wasys.gn.motorista.adapters.TutorialPageAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends AppCompatActivity {

    @Bind(R.id.pager) ViewPager mViewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator titleIndicator;
    @Bind(R.id.btn_ja_entendi)
    Button btn_ja_entendi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleActionBar();

        TutorialPageAdapter tutorialPageAdapter= new TutorialPageAdapter(this);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        btn_ja_entendi.setVisibility(View.INVISIBLE);
        mViewPager.setAdapter(tutorialPageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int page) {
                if (page == 3) {
                    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.height = 2045;
                    mViewPager.setLayoutParams(params);*/
                    btn_ja_entendi.setVisibility(View.VISIBLE);
                }
                else {
                    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mViewPager.setLayoutParams(params);*/
                    btn_ja_entendi.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        titleIndicator.setViewPager(mViewPager);
    }



    public void setTitleActionBar()
    {
        android.support.v7.app.ActionBar app = getSupportActionBar();
        app.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        app.setCustomView(R.layout.action_bar);
        app.setDisplayHomeAsUpEnabled(true);
        ((TextView)app.getCustomView().findViewById(R.id.actionbar_title)).setText("Tutorial");
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

    @OnClick(R.id.btn_ja_entendi)
    public void btnJaEntendi(View v)
    {
        switch(v.getId())
        {
            case R.id.btn_ja_entendi:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}
