package com.dev.invinity.rentalyuk.Fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.invinity.rentalyuk.Adapter.TabFragmentAdapter;
import com.dev.invinity.rentalyuk.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiFragment extends Fragment {

////////////////////////     Start Deklarasi Variable     /////////////////////////

    private ViewPager viewPager;
    private TabFragmentAdapter tabFragmentAdapter;
    private TabLayout tabLayout;

////////////////////////     End Deklarasi Variable     //////////////////////////

    public TransaksiFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaksi, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.vp_konten);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {

        tabFragmentAdapter = new TabFragmentAdapter(getChildFragmentManager());
        tabFragmentAdapter.addFragment(new TabStatusPenyewaFragment(), "Status Rental");
        tabFragmentAdapter.addFragment(new TabPemilikBarangFragment(), getString(R.string.pemilik_barang));
        tabFragmentAdapter.addFragment(new TabBarangkuFragment(), "Barangku");
        viewPager.setAdapter(tabFragmentAdapter);

    }


}
