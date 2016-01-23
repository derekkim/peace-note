package com.haranghaon.peacenote;

import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PersonCursorAdapter extends CursorAdapter {

    public static final int SORT_BY_NAME = 0;
    public static final int SORT_BY_GROUP = 1;
    private Context context;
    private Cursor cursor;
    private String search = "";

    public PersonCursorAdapter(Context context, Cursor c) {
        super(context, c);
        this.context = context;
        this.cursor = c;
    }

    public PersonCursorAdapter(Context context, Cursor c, String search) {
        super(context, c);
        this.context = context;
        this.cursor = c;
        this.search = search;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_list_item, null);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        View v = view;
        if (v != null) {
            TextView nt = (TextView)v.findViewById(R.id.contactItemName);
            TextView pt = (TextView)v.findViewById(R.id.contactItemPhone);
            TextView gt = (TextView)v.findViewById(R.id.contactItemGroup);
            String nameValue = cursor.getString(1);
            String phoneValue = cursor.getString(4);
            String groupValue = cursor.getString(2);

            setHighLightedText(nameValue, nt);
            setHighLightedText(phoneValue, pt);
            setHighLightedText(groupValue, gt);
        }
        return;
    }

    private void setHighLightedText(String nameValue, TextView textView) {
        String filter = search;
        String itemValue = nameValue;

        int startPos = itemValue.toLowerCase(Locale.US).indexOf(filter.toLowerCase(Locale.US));
        int endPos = startPos + filter.length();

        if (textView == null)
            return;
        if (startPos != -1) // This should always be true, just a sanity check
        {
            Spannable spannable = new SpannableString(itemValue);
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1,
                    context.getResources().getColorStateList(R.color.holo_blue_custom), null);

            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable);
        }
        else
            textView.setText(itemValue);
    }

//    public static String highlight(String search, String originalText) {
//        // ignore case and accents
//        // the same thing should have been done for the search text
//        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD)
//                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
//
//        int start = normalizedText.indexOf(search);
//        if (start < 0) {
//            // not found, nothing to to
//            return originalText;
//        } else {
//            // highlight each appearance in the original text
//            // while searching in normalized text
//            Spannable highlighted = new SpannableString(originalText);
//            while (start >= 0) {
//                int spanStart = Math.min(start, originalText.length());
//                int spanEnd = Math.min(start + search.length(), originalText.length());
//
//                highlighted.setSpan(new BackgroundColorSpan(R.color.holo_blue_custom), spanStart,
//                        spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                start = normalizedText.indexOf(search, spanEnd);
//            }
//
//            return highlighted.toString();
//        }
//    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}