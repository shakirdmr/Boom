package com.alisoftwares.boom.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alisoftwares.boom.Fragments.find;
import com.alisoftwares.boom.Fragments.polls;

import com.alisoftwares.boom.Fragments.replies;
import com.alisoftwares.boom.Fragments.settings;

public class ViewpagerFragmentSetter_MainActivty extends FragmentPagerAdapter {

    public ViewpagerFragmentSetter_MainActivty(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if(position==0)
            return new polls();

        /*if(position==1)
            return new replies();

        if(position==2)
            return new find();*/

        if(position==1)
            return new settings();


        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }
}
