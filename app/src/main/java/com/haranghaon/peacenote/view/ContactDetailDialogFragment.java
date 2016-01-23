package com.haranghaon.peacenote.view;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haranghaon.peacenote.NotesDbAdapter;
import com.haranghaon.peacenote.Person;
import com.haranghaon.peacenote.PersonCursorAdapter;
import com.haranghaon.peacenote.R;

public class ContactDetailDialogFragment extends DialogFragment implements OnClickListener {
    Person person;
    private String name;
    private String group;
    private String address;
    private String phone;
    private String family1;
    private String family2;
    private String family3;
    private String family4;
    private String family5;

    public static ContactDetailDialogFragment newInstance(Person mPerson) {
        ContactDetailDialogFragment f = new ContactDetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", mPerson.getName());
        args.putString("group", mPerson.getGroup());
        args.putString("address", mPerson.getAddress());
        args.putString("phone", mPerson.getPhone());
        if (mPerson.getFamily1() != null) {
            args.putString("family1", mPerson.getFamily1());
        }
        if (mPerson.getFamily2() != null) {
            args.putString("family2", mPerson.getFamily2());
        }
        if (mPerson.getFamily3() != null) {
            args.putString("family3", mPerson.getFamily3());
        }
        if (mPerson.getFamily4() != null) {
            args.putString("family4", mPerson.getFamily4());
        }
        if (mPerson.getFamily5() != null) {
            args.putString("family5", mPerson.getFamily5());
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        name = getArguments().getString("name");
        group = getArguments().getString("group");
        address = getArguments().getString("address");
        phone = getArguments().getString("phone");
        family1 = getArguments().getString("family1");
        family2 = getArguments().getString("family2");
        family3 = getArguments().getString("family3");
        family4 = getArguments().getString("family4");
        family5 = getArguments().getString("family5");

        View view = inflater.inflate(R.layout.contact_detail, container);
        TextView nameTextView = (TextView)view.findViewById(R.id.contactDetailName);
        TextView groupTextView = (TextView)view.findViewById(R.id.contactDetailGroup);
        TextView phoneTextView = (TextView)view.findViewById(R.id.contactDetailPhone);
        TextView addressTextView = (TextView)view.findViewById(R.id.contactDetailAddress);

        addressTextView.setSelected(true);

        nameTextView.setText(name);
        groupTextView.setText(group);
        addressTextView.setText(address);
        phoneTextView.setText(phone);

        if (family1 != null) {
            TextView familyTextView1 = (TextView)view.findViewById(R.id.contactDetailFamily1);
            familyTextView1.setText(family1);
            familyTextView1.setOnClickListener(this);
            if (family1.equals("")) {
                familyTextView1.setVisibility(View.GONE);
            }
        }
        if (family2 != null) {
            TextView familyTextView2 = (TextView)view.findViewById(R.id.contactDetailFamily2);
            familyTextView2.setText(family2);
            familyTextView2.setOnClickListener(this);
            if (family2.equals("")) {
                familyTextView2.setVisibility(View.GONE);
            }
        }
        if (family3 != null) {
            TextView familyTextView3 = (TextView)view.findViewById(R.id.contactDetailFamily3);
            familyTextView3.setText(family3);
            familyTextView3.setOnClickListener(this);
            if (family3.equals("")) {
                familyTextView3.setVisibility(View.GONE);
            }
        }
        if (family4 != null) {
            TextView familyTextView4 = (TextView)view.findViewById(R.id.contactDetailFamily4);
            familyTextView4.setText(family4);
            familyTextView4.setOnClickListener(this);
            if (family4.equals("")) {
                familyTextView4.setVisibility(View.GONE);
            }
        }
        if (family5 != null) {
            TextView familyTextView5 = (TextView)view.findViewById(R.id.contactDetailFamily5);
            familyTextView5.setText(family5);
            familyTextView5.setOnClickListener(this);
            if (family5.equals("")) {
                familyTextView5.setVisibility(View.GONE);
            }
        }
        getDialog().setTitle(name);

        Button smsBtn = (Button)view.findViewById(R.id.detailSmsBtn);
        smsBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                String smsBody = "";
                sendIntent.putExtra("sms_body", smsBody); // 보낼 문자
                sendIntent.putExtra("address", phone); // 받는사람 번호
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });
        Button callBtn = (Button)view.findViewById(R.id.detailCallBtn);
        callBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_CALL);
                sendIntent.setData(Uri.parse("tel:" + phone));
                startActivity(sendIntent);

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v instanceof TextView) {
            NotesDbAdapter dbAdapter;
            dbAdapter = new NotesDbAdapter(getActivity());
            dbAdapter.open();
            String name = ((TextView)v).getText().toString();
            Cursor cursor = dbAdapter.fetchNote(name);
            try {
                Person mPerson = Person.newPerson(cursor, getActivity());
                ContactDetailDialogFragment detailDialog = ContactDetailDialogFragment
                        .newInstance(mPerson );
                detailDialog.show(getFragmentManager(), "detail");
                this.dismiss();
            } catch (Exception e) {
                Log.e("ContactDetailDialogFragment", e.toString());
                Toast.makeText(getActivity(), name + "님은 등록 되지 않았습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
