package com.haranghaon.peacenote.view;

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
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haranghaon.peacenote.R;
import com.haranghaon.peacenote.common.CTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class HistoryFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private String[] historyDate;
    private String[] historyContents;
    private ListView mListView;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        historyDate = getResources().getStringArray(R.array.history_date);
        historyContents = getResources().getStringArray(R.array.history_contents);

        //init text view for year
        TextView mYearTextView = (TextView)rootView.findViewById(R.id.historyTitleYear);
        mYearTextView.setText(getResources().getStringArray(R.array.history_date)[0].substring(0, 4) + "년");
        mListView = (ListView)rootView.findViewById(R.id.historyListView);
        mListView.setAdapter(new HistoryAdapter(getActivity(), historyDate, historyContents));
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                String[] array = getResources().getStringArray(R.array.history_date);
                View mParentView = (View)view.getParent();
                TextView tv = (TextView)mParentView.findViewById(R.id.historyTitleYear);
                int lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                if (0 <= lastVisibleIndex && lastVisibleIndex < array.length)
                    tv.setText(array[lastVisibleIndex].substring(0, 4) + "년");

            }
        });

        return rootView;
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

    class HistoryAdapter extends BaseAdapter {
        Context context;
        private String[] date;
        private String[] contents;

        public HistoryAdapter(Context context, String[] date, String[] contents) {
            this.context = context;

            this.date = date;
            this.contents = contents;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.history_list_item, null);
            }

            TextView tv1 = (TextView)v.findViewById(R.id.historyItemDate);
            CTextView tv2 = (CTextView)v.findViewById(R.id.historyItemContents);

            tv1.setText(date[position]);
            tv2.setText(contents[position]);

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
                return contents[position];
            return date[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
    }
}
