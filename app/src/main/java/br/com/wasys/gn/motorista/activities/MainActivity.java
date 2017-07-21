package br.com.wasys.gn.motorista.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import br.com.wasys.gn.motorista.R;

import br.com.wasys.gn.motorista.fragments.ScheduledTransport;
import br.com.wasys.gn.motorista.fragments.SchedulerTransport;
import br.com.wasys.gn.motorista.helpers.Helper;
import br.com.wasys.gn.motorista.models.Usuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int mCurrentTab;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public static final int TAB_TRANSPORTES_AGENDADOS = 1;
    public static final String KEY_CURRENT_TAB = MainActivity.class.getName() + ".mCurrentTab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCurrentTab = intent.getIntExtra(KEY_CURRENT_TAB, 0);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.user_name);
        TextView menu_title = (TextView)hView.findViewById(R.id.menu_title);
        Usuario obj = Helper.current_user(this);
        nav_user.setText(obj.getNome());
        menu_title.setText(obj.getEmail());
        setupTabs();
    }

    public void setupTabs() {

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(mCurrentTab);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int i = mViewPager.getCurrentItem();
            if (i == 0) {
                drawer.openDrawer(GravityCompat.START);
            }
            else {
                i--;
                mViewPager.setCurrentItem(i);
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Intent i;
        int id = item.getItemId();

        if (id == R.id.nav_termos_de_uso) {
            Bundle extras = new Bundle();
            extras.putBoolean("from_termo",true);
            Intent intent = new Intent(this, UserAgreementActivity.class);
            intent.putExtras(extras);
            startActivity(intent);


        } else if (id == R.id.nav_ajuda) {
            i = new Intent(this, TutorialActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_sobre) {

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_sobre);
            dialog.setTitle("Title...");
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();

        }else if(id == R.id.nav_sair)
        {


            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_sair);
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_cancel);
            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();


        } else if(id == R.id.nav_definir_carro)
        {
            System.out.println(Helper.getStringLogin(this));
            Intent intent = new Intent(this, DefinirCarroActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logout()
    {
        SharedPreferences preferences = getSharedPreferences("AOP_PREFS_MOTORISTA", 0);
        preferences.edit().remove("login").commit();
        preferences.edit().remove("current_carro_id").commit();
        SharedPreferences clear_preferences = getPreferences(0);
        SharedPreferences.Editor editor = clear_preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SchedulerTransport();
                case 1:
                    return new ScheduledTransport();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab1);
                case 1:
                    return getString(R.string.tab2);
            }
            return null;
        }
    }
}
