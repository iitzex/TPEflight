package com.atc.qn.tpeflight;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AirlinesFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.airlines, container, false);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TerminalAdapter mTerminalAdapter = new TerminalAdapter(getChildFragmentManager());
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mTerminalAdapter);

        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                QNLog.d(String.valueOf(tab.getText()));
//
//                TerminalFragment termFrag = (TerminalFragment)mTerminalAdapter.getItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.name_airlines));
    }

    private class TerminalAdapter extends FragmentPagerAdapter
    {
        int NumberOfPages = 2;
        String TERMINAL1 = "第一航廈1樓";
        String TERMINAL2 = "第二航廈3樓";

        public TerminalAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QNLog.d("terminal " + String.valueOf(position));
            if (position == 0) {
                return TerminalFragment.newInstance(TERMINAL1);
            }else
                return TerminalFragment.newInstance(TERMINAL2);
        }

        public int getCount() {
            return NumberOfPages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return TERMINAL1;
                case 1:
                    return TERMINAL2;
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            super.destroyItem(container, position, object);
        }
    }
}
