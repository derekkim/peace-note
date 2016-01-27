package com.haranghaon.peacenote.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.haranghaon.peacenote.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlanningFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlanningFragment newInstance(int sectionNumber) {
        PlanningFragment fragment = new PlanningFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private String[] planDate;
    private String[] weekendPlanContents;
    private String[] weekdayPlanContents;
    private ListView mListView;

    public PlanningFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plan, container, false);

        planDate = getSundayDates(getYear());
        weekdayPlanContents = getResources().getStringArray(R.array.weekday_plan_contents);
        weekendPlanContents = getResources().getStringArray(R.array.weekEnd_plan_contents);

        //init text view for year
        mListView = (ListView)rootView.findViewById(R.id.planListView);
        mListView.setAdapter(new PlanAdapter(getActivity(), planDate, weekendPlanContents, weekdayPlanContents));
        mListView.setSelection(getWeekOfYear() - 1);
        return rootView;
    }

    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    private static int getWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private static String[] getSundayDates(int year) {
        ArrayList<String> sundays = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        for (int mm = 1; mm <= 12; mm++) {
            cal.set(year, mm - 1, 1);
            int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            for (int i = 1; i < maxDate + 1; i++) {
                cal.clear();
                cal.set(year, mm - 1, i);
                switch (cal.get(cal.DAY_OF_WEEK)) {
                    case java.util.Calendar.SUNDAY:
                        sundays.add(format.format(cal.getTime()));
                }
                cal.clear();
            }
        }
        return sundays.toArray(new String[sundays.size()]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((MainActivity)getActivity()).getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            inflater.inflate(R.menu.main, menu);
            return;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class PlanAdapter extends BaseAdapter {
        Context context;
        private String[] date;
        private String[] contents1;
        private String[] contents2;

        public PlanAdapter(Context context, String[] date, String[] contents1, String[] contents2) {
            this.context = context;

            this.date = date;
            this.contents1 = contents1;
            this.contents2 = contents2;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.plan_list_item, null);
            }

            TextView tv1 = (TextView)v.findViewById(R.id.planItemDate);
            TextView tv2 = (TextView)v.findViewById(R.id.weekendPlanItemContents);
            TextView tv3 = (TextView)v.findViewById(R.id.weekdayPlanItemContents);

            tv1.setText(date[position]);
            tv2.setText(contents1[position]);
            tv3.setText(contents2[position]);

            return v;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return date.length;
        }

        @Override
        public Object getItem(int position) {
            if (position >= date.length)
                return contents1[position];
            return date[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }
}
