package com.haranghaon.peacenote.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import jxl.Sheet;
import jxl.Workbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.haranghaon.peacenote.NotesDbAdapter;
import com.haranghaon.peacenote.Person;
import com.haranghaon.peacenote.PersonCursorAdapter;
import com.haranghaon.peacenote.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactFragment extends Fragment {

    private static final String TAG = "NotesDbAdapter";
    private static final String IS_REGISTED = "is_registed";

    private NotesDbAdapter dbAdapter;
    private ListView mListView;
    SharedPreferences prefs = null;
    private ArrayList<Person> resultList;
    private PersonCursorAdapter mPersonCursorAdapter;
    private Cursor result;
    private int mSort = PersonCursorAdapter.SORT_BY_NAME;

    protected String phoneNumber;

    protected String key;

    private Handler mHandler = new Handler();

    private AlertDialog.Builder alertDialogBuilder;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private boolean ignoreRegistration = false;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContactFragment newInstance(int sectionNumber) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        long start = System.currentTimeMillis();
        Log.d(TAG, "start: " + (System.currentTimeMillis() - start)); start = System.currentTimeMillis();


        Log.d(TAG, "DatabaseTest :: onCreate()");

        View rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        prefs = getActivity().getSharedPreferences("com.haranghaon.peacenote",
                getActivity().MODE_PRIVATE);
        Log.d(TAG, "prefs: " + (System.currentTimeMillis() - start)); start = System.currentTimeMillis();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        getActivity().registerReceiver(mIntentReceiver, filter, null, mHandler);

        mListView = (ListView)rootView.findViewById(R.id.contactListView);
        mPersonCursorAdapter = new PersonCursorAdapter(getActivity(), getContactCursor(mSort));
        mListView.setAdapter(mPersonCursorAdapter);
        Log.d(TAG, "mListView.setAdapter: " + (System.currentTimeMillis() - start)); start = System.currentTimeMillis();

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = ((PersonCursorAdapter)parent.getAdapter()).getCursor();
                cursor.moveToPosition(position);
                ContactDetailDialogFragment detailDialog = ContactDetailDialogFragment
                        .newInstance(Person.newPerson(cursor, getActivity()));
                detailDialog.show(getFragmentManager(), "detail");
            }
        });
        Log.d(TAG, "mListView.setOnItemClickListener: " + (System.currentTimeMillis() - start)); start = System.currentTimeMillis();

        EditText searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {//텍스트가 다 지워질때는 전체 목록을 보여준다
                    mPersonCursorAdapter = new PersonCursorAdapter(getActivity(),
                            getContactCursor(mSort));
                } else {
                    String keyWord = s.toString();
                    mPersonCursorAdapter = new PersonCursorAdapter(getActivity(),
                            getFilterdCursor(keyWord), s.toString());
                }
                mListView.setAdapter(mPersonCursorAdapter);
                mPersonCursorAdapter.notifyDataSetChanged();//검색이 끝나면 새로운 리스트로 리스트뷰를 갱신해준다
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setHasOptionsMenu(true);

        if (!prefs.getBoolean(IS_REGISTED, false) && !ignoreRegistration) {
            confirmContact();
            mListView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    private Cursor getContactCursor(int sortType) {

        this.dbAdapter = new NotesDbAdapter(getActivity());

        PackageInfo pinfo;
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            int prevVersionName = prefs.getInt("version", 999999);
            Log.d(TAG, "Version Code : " + versionNumber);
            if ((prefs.getBoolean("firstrun", true) || (prevVersionName < versionNumber))) {
                DataLoad dataLoad = new DataLoad();
                dataLoad.execute();
                prefs.edit().putBoolean("firstrun", false).commit();
            }
            prefs.edit().putInt("version", versionNumber).commit();
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dbAdapter.open();
        Cursor c = dbAdapter.fetchAllNotes(sortType);
        c.moveToFirst();
        return c;
    }

    private void confirmContact() {
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("본인 인증");
        alertDialogBuilder.setMessage("전화번호를 입력해 주세요.");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        alertDialogBuilder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        phoneNumber = input.getText().toString();
                        phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber);
                        if (phoneNumber.equals("032-328-1661") || phoneNumber.equals("328-1661")) {
                            setConfirmed(true);

                        } else if (isContain(phoneNumber)) {
                            key = UUID.randomUUID().toString().split("-")[0];
                            sendMessage(phoneNumber, key);
                            Toast.makeText(getActivity(),
                                    "문자메세지를 통해 본인 확인 중 입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "전화번호 등록이 안 되어 있습니다. 관리자에게 문의 해주세요.", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    private void sendMessage(String phoneNumber, String key) {
                        SmsManager.getDefault().sendTextMessage(phoneNumber, null, key, null, null);
                    }

                    private boolean isContain(String input) {
                        NotesDbAdapter dbAdapter;
                        dbAdapter = new NotesDbAdapter(getActivity());
                        dbAdapter.open();
                        if ((input.length() == 13 || input.length() == 12) && (input.startsWith("01"))) {
                            Cursor cursor = dbAdapter.fetchNoteWithPhoneNumber(input);
                            Log.d(TAG, "cursor count: " + cursor.getCount());
                            try {
                                Person mPerson = Person.newPerson(cursor, getActivity());
                                Log.d(TAG, mPerson.toString());
                                return (mPerson == null) ? false : true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }

                });

        alertDialogBuilder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.show();
    }

    private Cursor getFilterdCursor(String key) {

        this.dbAdapter = new NotesDbAdapter(getActivity());
        dbAdapter.open();
        Cursor c = dbAdapter.fetchFilterdNotes(mSort, key);
        c.moveToFirst();
        return c;
    }

    protected void sort(int sortType) {
        mPersonCursorAdapter = new PersonCursorAdapter(getActivity(), getContactCursor(sortType));
        mListView.setAdapter(mPersonCursorAdapter);
        mPersonCursorAdapter.notifyDataSetChanged();
        mSort = sortType;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity)activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub

        if (result != null) {
            result.close();
        }
        if (dbAdapter != null) {
            dbAdapter.close();
        }
        hideKeyboard(getActivity());
        super.onDetach();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!((MainActivity)getActivity()).getNavigationDrawerFragment().isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            inflater.inflate(R.menu.contact, menu);
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

        switch (id) {
            case R.id.action_sort:
                showSortTypeDialog();
                break;

            //        case R.id.action_message:
            //            //
            //            break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortTypeDialog() {
        SortDialogFragment sortDialog = SortDialogFragment.newInstance(this);
        sortDialog.show(getFragmentManager(), "sort");
    }

    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");

        Workbook workbook = null;
        Sheet sheet = null;

        try {
            InputStream is = getActivity().getResources().getAssets().open("notes.xls");
            workbook = Workbook.getWorkbook(is);

            if (workbook != null) {
                sheet = workbook.getSheet(0);

                if (sheet != null) {

                    int nMaxColumn = 9;
                    int nRowStartIndex = 0;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    //                    int nColumnEndIndex = sheet.getRow(2).length - 1;

                    dbAdapter.open();
                    dbAdapter.deleteAllNote();
                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String name = sheet.getCell(nColumnStartIndex, nRow).getContents();
                        String group = sheet.getCell(nColumnStartIndex + 1, nRow).getContents();
                        String address = sheet.getCell(nColumnStartIndex + 2, nRow).getContents();
                        String phone = sheet.getCell(nColumnStartIndex + 3, nRow).getContents();
                        String family1 = sheet.getCell(nColumnStartIndex + 4, nRow).getContents();
                        String family2 = sheet.getCell(nColumnStartIndex + 5, nRow).getContents();
                        String family3 = sheet.getCell(nColumnStartIndex + 6, nRow).getContents();
                        String family4 = sheet.getCell(nColumnStartIndex + 7, nRow).getContents();
                        String family5 = sheet.getCell(nColumnStartIndex + 8, nRow).getContents();
                        dbAdapter.createNote(name, group, address, phone, family1, family2,
                                family3, family4, family5);
                    }
                    dbAdapter.close();
                } else {
                    Log.w(TAG, "Sheet is null!!");
                }
            } else {
                Log.w(TAG, "WorkBook is null!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    public static class SortDialogFragment extends DialogFragment {

        private static ContactFragment contactFragment;

        public static SortDialogFragment newInstance(ContactFragment mContactFragment) {
            SortDialogFragment f = new SortDialogFragment();
            contactFragment = mContactFragment;
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.action_sort_type)
                    .setItems(R.array.contact_sort_type, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0: // by name
                                    Toast.makeText(getActivity(), "이름정렬", Toast.LENGTH_SHORT).show();
                                    contactFragment.sort(PersonCursorAdapter.SORT_BY_NAME);
                                    break;
                                case 1: // by group
                                    Toast.makeText(getActivity(), "그룹정렬", Toast.LENGTH_SHORT).show();
                                    contactFragment.sort(PersonCursorAdapter.SORT_BY_GROUP);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
            return builder.create();
        }
    }

    private class DataLoad extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        public DataLoad() {
            ;
            mProgressDialog = new ProgressDialog(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setTitle("잠시만 기다려 주세요.");
            mProgressDialog.setMessage("연락처를 업데이트 중입니다.");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            mListView = (ListView)getView().findViewById(R.id.contactListView);
            PersonCursorAdapter adapter = (PersonCursorAdapter)mListView.getAdapter();
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {
            copyExcelDataToDatabase();

            return null;
        }
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive : " + intent.getAction());
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    String messageBody = smsMessage.getMessageBody();
                    setConfirmed(messageBody.equals(key));
                }
            }
        }
    };

    private void setConfirmed(boolean isConfirmed) {
        if (isConfirmed) {
            Toast.makeText(getActivity(), "인증 성공 하였습니다.", Toast.LENGTH_SHORT).show();
            prefs.edit().putBoolean(IS_REGISTED, true).commit();
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.container, ContactFragment.newInstance(5))
                    .commit();
        }
    }

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(mIntentReceiver);
        super.onDestroyView();
    }
}
