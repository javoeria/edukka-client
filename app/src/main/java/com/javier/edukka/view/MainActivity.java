package com.javier.edukka.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.javier.edukka.R;
import com.javier.edukka.fragment.CardContentFragment;
import com.javier.edukka.fragment.ListContentFragment;
import com.javier.edukka.fragment.TileContentFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private int[] tabIcons = {
            R.drawable.ic_games_white_36dp,
            R.drawable.ic_group_white_36dp,
            R.drawable.ic_schedule_white_36dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setTitle(UserSingleton.getInstance().getUserModel().getUsername());

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);

        tabs.getTabAt(0).setCustomView(R.layout.customlab);
        tabs.getTabAt(1).setCustomView(R.layout.customlab);
        tabs.getTabAt(2).setCustomView(R.layout.customlab);

        tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorAccent),PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(1).getIcon().setColorFilter(Color.LTGRAY,PorterDuff.Mode.SRC_IN);
        tabs.getTabAt(2).getIcon().setColorFilter(Color.LTGRAY,PorterDuff.Mode.SRC_IN);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tab.getIcon().setColorFilter(Color.WHITE,PorterDuff.Mode.SRC_IN);
                tab.getIcon().setColorFilter(getResources().getColor(R.color.colorAccent),PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.LTGRAY,PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new TileContentFragment(), "Subjects");
        adapter.addFragment(new ListContentFragment(), "Class");
        adapter.addFragment(new CardContentFragment(), "Activity");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

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
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
