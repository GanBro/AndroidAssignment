package com.ganbro.shopmaster.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.ganbro.shopmaster.fragments.FavoritesFragment; // 导入正确的包

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FavoritesFragment.newInstance(position == 0 ? "product" : "video");
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
