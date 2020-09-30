package com.example.runtime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendsViewPagerAdapter extends FragmentStateAdapter {

    public FriendsViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new FriendsTabFragment();
        } else {
            return new FriendsRequestsTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
