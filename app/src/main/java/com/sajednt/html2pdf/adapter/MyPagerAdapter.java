package com.sajednt.html2pdf.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.sajednt.html2pdf.fragment.fragment_factor;
import com.sajednt.html2pdf.fragment.fragment_list;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment fragment = new fragment_list();
                return fragment;
            case 1:
                Fragment fragment1 = new fragment_list();
                return fragment1;

            case 2:

                Fragment fragment2 = new fragment_factor();
                return fragment2;

        }

        return  null;

    }

    @Override
    public int getCount() {
        return 3;
    }
}
