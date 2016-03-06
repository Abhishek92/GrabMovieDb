package com.android.grabmoviedb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.android.grabmoviedb.fragments.FavouriteListFragment;
import com.android.grabmoviedb.fragments.MovieListFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static int FAVOURITE_TAB_POS = 1;
    public static String TAB_POS = "tabPos";
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolBar);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mToolbar.setTitle(R.string.home_title);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(getString(R.string.now_playing_tab_txt), new MovieListFragment(), 1);
        pagerAdapter.addTab(getString(R.string.favourite_tab_text), new FavouriteListFragment(), 2);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setFavouriteTab();
    }

    private void setFavouriteTab() {
        if (getIntent().getIntExtra(TAB_POS, 0) == FAVOURITE_TAB_POS)
            mTabLayout.getTabAt(FAVOURITE_TAB_POS).select();
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private final HashMap<Integer, Fragment> mFragments;
        private final ArrayList<Integer> mTabNums;
        private final ArrayList<CharSequence> mTabTitles;

        @SuppressLint("UseSparseArrays")
        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new HashMap<Integer, Fragment>(4);
            mTabNums = new ArrayList<Integer>(4);
            mTabTitles = new ArrayList<CharSequence>(4);
        }

        public void addTab(String tabTitle, Fragment newFragment, int tabId) {
            mTabTitles.add(tabTitle);
            mFragments.put(tabId, newFragment);
            mTabNums.add(tabId);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mFragments.get(mTabNums.get(position));
            } else if (position == 1) {
                return mFragments.get(mTabNums.get(position));
            }

            return new Fragment();
        }
    }

}
