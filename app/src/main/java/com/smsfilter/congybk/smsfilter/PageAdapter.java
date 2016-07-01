package com.smsfilter.congybk.smsfilter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by congybk on 24/06/2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter {


    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position){
            case 0:
                frag = new BlockPhoneFragment();
                break;
            case 1:
                frag = new BlockSMSFragment();
                break;
            case 2:
                frag = new BlockCallFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch (position){
            case 0:
                title = "Phones";
                break;
            case 1:
                title = "SMS";
                break;
            case 2:
                title = "CALL";
                break;
        }
        return title;
    }
}
