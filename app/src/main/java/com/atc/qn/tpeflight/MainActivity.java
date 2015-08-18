package com.atc.qn.tpeflight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class MainActivity extends AppCompatActivity implements DrawerCallback {
    private DrawerFragment mDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mDrawerFragment = (DrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

    }

    @Override
    public void onDrawerItemSelected(int position) {
        // update the main_menu content by replacing fragments

        FragmentManager fragMgr = getSupportFragmentManager();
        FlightFragment departureFrag = new FlightFragment();
        Bundle depArgs = new Bundle();
        depArgs.putString("FlightAction", "D");
        departureFrag.setArguments(depArgs);

        FlightFragment landingFrag = new FlightFragment();
        Bundle arrArgs = new Bundle();
        arrArgs.putString("FlightAction", "A");
        landingFrag.setArguments(arrArgs);

        AirlinesFragment infoFrag = new AirlinesFragment();

        if (position == 0) {
            fragMgr.beginTransaction()
                    .replace(R.id.container, departureFrag)
                    .commit();
        }else if (position == 1) {
            fragMgr.beginTransaction()
                    .replace(R.id.container, landingFrag)
                    .commit();
        }else{
            fragMgr.beginTransaction()
                    .replace(R.id.container, infoFrag)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerFragment.isDrawerOpen())
            mDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
