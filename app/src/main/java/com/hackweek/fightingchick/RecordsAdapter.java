package com.hackweek.fightingchick;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RecordsAdapter extends FragmentStateAdapter {
    public RecordsAdapter(Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment =null;
        switch (position){
            case 0:
                fragment = new MyChickFragment();
                break;
            case 1:
                fragment = new StatisticsFragment();
                break;
            case 2:
                fragment = new GloriesConfessionsFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}