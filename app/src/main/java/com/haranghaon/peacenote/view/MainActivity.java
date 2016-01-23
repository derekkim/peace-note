package com.haranghaon.peacenote.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.widget.Toast;

import com.haranghaon.peacenote.R;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer, mDrawerLayout);
        //        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Log.d(TAG, "[onNavigationDrawerItemSelected] position: " + position);
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, GreetingFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HistoryFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlanningFragment.newInstance(position + 1))
                        .commit();
                break;
            case 3:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, WorshipHelperFragment.newInstance(position + 1))
                        .commit();
                break;
            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ContactFragment.newInstance(position + 1))
                        .commit();
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
        }
    }

    public void onSectionAttached(int number) {
        Log.d(TAG, "[onSectionAttached] position: " + number);
        number--;
        switch (number) {
        case 0:
            mTitle = getString(R.string.title_section1);
            break;
        case 1:
            mTitle = getString(R.string.title_section2);
            break;
        case 2:
            mTitle = getString(R.string.title_section3);
            break;
        case 3:
            mTitle = getString(R.string.title_section5);
            break;
        case 4:
            mTitle = getString(R.string.title_section8);
            break;
        }
        Log.d(TAG, "[onSectionAttached] mTitle: " + mTitle);
        this.getActionBar().setTitle(mTitle);
    }

    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    //        @Override
    //        public boolean onCreateOptionsMenu(Menu menu) {
    //            if (!mNavigationDrawerFragment.isDrawerOpen()) {
    //                // Only show items in the action bar relevant to this screen
    //                // if the drawer is not showing. Otherwise, let the drawer
    //                // decide what to show in the action bar.
    //                getMenuInflater().inflate(R.menu.global, menu);
    //                restoreActionBar();
    //                return true;
    //            }
    //            return super.onCreateOptionsMenu(menu);
    //        }
    //    
    //        @Override
    //        public boolean onOptionsItemSelected(MenuItem item) {
    //            // Handle action bar item clicks here. The action bar will
    //            // automatically handle clicks on the Home/Up button, so long
    //            // as you specify a parent activity in AndroidManifest.xml.
    //            int id = item.getItemId();
    //            if (id == R.id.action_settings) {
    //                return true;
    //            }
    //            return super.onOptionsItemSelected(item);
    //        }
}
