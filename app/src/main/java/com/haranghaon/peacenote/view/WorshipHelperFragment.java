package com.haranghaon.peacenote.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
public class WorshipHelperFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WorshipHelperFragment newInstance(int sectionNumber) {
        WorshipHelperFragment fragment = new WorshipHelperFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private String[] worshipHelperWeeks;
    private String[] worshipHelperPray1;
    private String[] worshipHelperPray2;
    private String[] worshipHelperPraySong;
    private String[] worshipHelperGift1;
    private String[] worshipHelperGift2;
    private String[] worshipHelperHelper;
    private String[] worshipHelperGuide1;
    private String[] worshipHelperGuide2;
    private String[] worshipHelperWed;
    private ListView mListView;

    public WorshipHelperFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_helper, container, false);

        worshipHelperWeeks = getResources().getStringArray(R.array.worship_helper_weeks);
        worshipHelperPray1 = getResources().getStringArray(R.array.worship_helper_pray_1);
        worshipHelperPray2 = getResources().getStringArray(R.array.worship_helper_pray_2);
        worshipHelperPraySong = getResources().getStringArray(R.array.worship_helper_pray_song);
        worshipHelperGift1 = getResources().getStringArray(R.array.worship_helper_gift_2);
        worshipHelperGift2 = getResources().getStringArray(R.array.worship_helper_gift_1);
        worshipHelperHelper = getResources().getStringArray(R.array.worship_helper_helper);
        worshipHelperGuide1 = getResources().getStringArray(R.array.worship_helper_guide_1);
        worshipHelperGuide2 = getResources().getStringArray(R.array.worship_helper_guide_2);
        worshipHelperWed = getResources().getStringArray(R.array.worship_helper_wed);

        //init text view for year
        TextView mDateTextView = (TextView)rootView.findViewById(R.id.helpTitleDate);
        mDateTextView.setText(worshipHelperWeeks[0]);
        mListView = (ListView)rootView.findViewById(R.id.helpListView);
        mListView.setAdapter(new WorshipHelperAdapter(getActivity(), worshipHelperWeeks,
                worshipHelperPray1, worshipHelperPray2, worshipHelperPraySong, worshipHelperGift1,
                worshipHelperGift2, worshipHelperHelper,
                worshipHelperGuide1, worshipHelperGuide2, worshipHelperWed));
        mListView.setSelection(getWeekOfYear() - 1);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                String[] array = worshipHelperWeeks;
                View mParentView = (View)view.getParent();
                TextView tv = (TextView)mParentView.findViewById(R.id.helpTitleDate);
                if (0 < firstVisibleItem && firstVisibleItem < array.length)
                    tv.setText(array[firstVisibleItem]);
            }
        });
        return rootView;
    }

    private static int getWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context activity) {
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

    class WorshipHelperAdapter extends BaseAdapter {
        Context context;
        private String[] weeks;
        private String[] contents1;
        private String[] contents2;
        private String[] contents3;
        private String[] contents4;
        private String[] contents5;
        private String[] contents6;
        private String[] contents7;
        private String[] contents8;
        private String[] contents9;

        public WorshipHelperAdapter(Context context, String[] weeks, String[] contents1,
                                    String[] contents2, String[] contents3, String[] contents4, String[] contents5,
                                    String[] contents6, String[] contents7, String[] contents8, String[] contents9) {
            this.context = context;

            this.weeks = weeks;
            this.contents1 = contents1;
            this.contents2 = contents2;
            this.contents3 = contents3;
            this.contents4 = contents4;
            this.contents5 = contents5;
            this.contents6 = contents6;
            this.contents7 = contents7;
            this.contents8 = contents8;
            this.contents9 = contents9;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.worship_list_item, null);
            }

            TextView tv0 = (TextView)v.findViewById(R.id.worshipItemWeeks);
            TextView tv1 = (TextView)v.findViewById(R.id.worshipItemContents1);
            TextView tv4 = (TextView)v.findViewById(R.id.worshipItemContents4);
            TextView tv6 = (TextView)v.findViewById(R.id.worshipItemContents6);

            tv0.setText(weeks[position]);
            tv1.setText("1부 예배: " + contents1[position] + "\n2부 예배: " + contents2[position]
                    + "\n찬양 예배: " + contents3[position] + "\n수요성서 연구: " + contents9[position]);
            tv4.setText("1부 예배: " + contents4[position] + "\n2부 예배: " + contents5[position]);
            tv6.setText("안내담당: " + contents6[position] + "\n1부 예배: " + contents7[position]
//                    + "\n2부 예배: " + contents8[position]
            );
            tv1.setEllipsize(TruncateAt.MARQUEE);
            tv4.setEllipsize(TruncateAt.MARQUEE);
            tv6.setEllipsize(TruncateAt.MARQUEE);
//            tv1.setSingleLine(true);
//            tv4.setSingleLine(true);
//            tv6.setSingleLine(true);
            tv1.setSelected(true);
            tv4.setSelected(true);
            tv6.setSelected(true);
            return v;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return weeks.length;
        }

        @Override
        public Object getItem(int position) {
            if (position >= weeks.length)
                return contents1[position];
            return weeks[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }
}
