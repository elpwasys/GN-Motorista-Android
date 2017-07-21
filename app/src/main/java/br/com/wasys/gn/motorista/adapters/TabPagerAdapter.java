package br.com.wasys.gn.motorista.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.com.wasys.gn.motorista.fragments.ScheduledTransport;
import br.com.wasys.gn.motorista.fragments.SchedulerTransport;


/**
 * Created by fernandamoncores on 3/28/16.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    public TabPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                //Fragement for Android Tab
                return new SchedulerTransport();
            case 1:
                //Fragment for Ios Tab
                return new ScheduledTransport();
        }
        return null;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mNumOfTabs; //No of Tabs
    }

}
