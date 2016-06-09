package net.sharksystem.sbc.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import net.sharksystem.sbc.fragments.BroadcastsFragment;
import net.sharksystem.sbc.fragments.RadarFragment;

import java.util.ArrayList;

/**
 * Created by j4rvis on 12.05.16.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[] { "Radar", "Broadcasts" };
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public SampleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        switch (position){
            case 0:
                f = new RadarFragment();
                break;
            case 1:
                f = new BroadcastsFragment();
                break;
            default:
                f = new RadarFragment();
                break;
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}