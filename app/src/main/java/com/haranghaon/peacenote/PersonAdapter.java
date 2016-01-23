package com.haranghaon.peacenote;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class PersonAdapter extends ArrayAdapter<Person> {

    private ArrayList<Person> items;

    public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.contact_list_item, null);
        }
        Person p = items.get(position);
        if (p != null) {
            TextView nt = (TextView)v.findViewById(R.id.contactItemName);
            TextView pt = (TextView)v.findViewById(R.id.contactItemPhone);
            TextView gt = (TextView)v.findViewById(R.id.contactItemGroup);
            if (nt != null) {
                nt.setText(p.getName());
            }
            if (pt != null) {
                pt.setText(p.getPhone());
            }
            if (gt != null) {
                gt.setText(p.getGroup());
            }
        }
        return v;
    }


}