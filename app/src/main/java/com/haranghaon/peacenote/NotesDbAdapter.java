package com.haranghaon.peacenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NotesDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "_name";
    public static final String KEY_GROUP = "_group";
    public static final String KEY_ADDRESS = "_address";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_FAM1 = "family1";
    public static final String KEY_FAM2 = "family2";
    public static final String KEY_FAM3 = "family3";
    public static final String KEY_FAM4 = "family4";
    public static final String KEY_FAM5 = "family5";
    private static final String TAG = "NotesDbAdapter";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     *
     * Database creation sql statement
     */

    private static final String DATABASE_CREATE = "create table contacts (_id integer primary key autoincrement, "
            + "_name text not null, _group text not null, _address text not null, phone text not null, family1 text not null, family2 text not null, family3 text not null, family4 text not null, family5 text not null);";

    private static final String DATABASE_NAME = "data.db";
    private static final String DATABASE_TABLE = "contacts";
    private static final int DATABASE_VERSION = 2;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    public NotesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public NotesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String name, String group, String address, String phone, String family1,
                           String family2, String family3, String family4, String family5) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_GROUP, group);
        initialValues.put(KEY_ADDRESS, address);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_FAM1, family1);
        initialValues.put(KEY_FAM2, family2);
        initialValues.put(KEY_FAM3, family3);
        initialValues.put(KEY_FAM4, family4);
        initialValues.put(KEY_FAM5, family5);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteNote(long rowId) {
        Log.i("Delete called", "value__" + rowId);
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllNote() {
        return mDb.delete(DATABASE_TABLE, null, null) > 0;
    }

    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                        KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 }, null,
                null, null, null, KEY_NAME);
    }

    public Cursor fetchAllNotes(int sortType) {
        Cursor c;
        switch (sortType) {
            case PersonCursorAdapter.SORT_BY_NAME:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        null, null, null, null, KEY_NAME);
                break;
            case PersonCursorAdapter.SORT_BY_GROUP:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        null, null, null, null, KEY_GROUP);
                break;
            default:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        null, null, null, null, KEY_NAME);
                break;
        }
        return c;
    }

    public Cursor fetchNote(long rowId) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE,
                new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP, KEY_ADDRESS, KEY_PHONE, KEY_FAM1,
                        KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 }, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public Cursor fetchNote(String name) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE,
                new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP, KEY_ADDRESS, KEY_PHONE, KEY_FAM1,
                        KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 }, "_name = ?",
                new String[] { name}, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchNoteWithPhoneNumber(String phoneNumber) throws SQLException {

        Cursor mCursor = mDb.query(true, DATABASE_TABLE,
                new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP, KEY_ADDRESS, KEY_PHONE, KEY_FAM1,
                        KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 }, "phone = ?",
                new String[] { phoneNumber}, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchFilterdNotes(int mSort, String key) {
        Cursor c;
        switch (mSort) {
            case PersonCursorAdapter.SORT_BY_NAME:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        "_name LIKE ? OR _group LIKE ? OR phone LIKE ?",
                        new String[] { "%"+key+"%", "%"+key+"%", "%"+key+"%" }, null, null, KEY_NAME);
                break;
            case PersonCursorAdapter.SORT_BY_GROUP:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        "_name LIKE ? OR _group LIKE ? OR phone LIKE ?",
                        new String[] { "%"+key+"%", "%"+key+"%", "%"+key+"%" }, null, null, KEY_GROUP);
                break;
            default:
                c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME, KEY_GROUP,
                                KEY_ADDRESS, KEY_PHONE, KEY_FAM1, KEY_FAM2, KEY_FAM3, KEY_FAM4, KEY_FAM5 },
                        "_name LIKE ? OR _group LIKE ? OR phone LIKE ?",
                        new String[] { "%"+key+"%", "%"+key+"%", "%"+key+"%" }, null, null, KEY_NAME);
                break;
        }
        return c;
    }
    //    public boolean updateNote(long rowId, String title, String body) {
    //        ContentValues args = new ContentValues();
    //                args.put(KEY_TITLE, title);
    //                args.put(KEY_BODY, body);
    //        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    //    }

}