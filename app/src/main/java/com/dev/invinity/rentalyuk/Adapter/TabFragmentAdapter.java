package com.dev.invinity.rentalyuk.Adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.dev.invinity.rentalyuk.Fragment.TabPemilikBarangFragment;
import com.dev.invinity.rentalyuk.Fragment.TabStatusPenyewaFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter   {

////////////////////////    Start Deklarasi Variable    //////////////////////////
    private Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabFragmentAdapter(android.support.v4.app.FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    //Isi dari Tab
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

////////////////////////     End Deklarasi Variable     //////////////////////////

}
