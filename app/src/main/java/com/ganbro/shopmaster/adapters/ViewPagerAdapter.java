package com.ganbro.shopmaster.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return com.ganbro.shopmaster.activities.FavoritesFragment.newInstance(position == 0 ? "product" : "video");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
