package com.haranghaon.peacenote;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class Person {

    private static final String TAG = Person.class.getSimpleName();
    private String name;
    private String group;
    private String address;
    private String phone;
    private String family1;
    private String family2;
    private String family3;
    private String family4;
    private String family5;

    public Person(String mName, String mGroup, String mAddress, String mPhone, String mFamily1,
                  String mFamily2, String mFamily3, String mFamily4, String mFamily5) {
        this.name = mName;
        this.group = mGroup;
        this.address = mAddress;
        this.phone = mPhone;
        this.family1 = mFamily1;
        this.family2 = mFamily2;
        this.family3 = mFamily3;
        this.family4 = mFamily4;
        this.family5 = mFamily5;
    }

    public static Person newPerson(Cursor cursor, Context context) {
        Log.d(TAG, cursor.getString(1) + ", " + cursor.getString(2) + ", " + cursor.getString(3) + ", " + cursor.getString(4) + ", " + cursor.getString(5) + ", " + cursor.getString(6) + ", " + cursor.getString(7) + ", " + cursor.getString(8) + ", " + cursor.getString(9));
        return new Person(cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9));

    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getFamily1() {
        return family1;
    }

    public String getFamily2() {
        return family2;
    }

    public String getFamily3() {
        return family3;
    }

    public String getFamily4() {
        return family4;
    }

    public String getFamily5() {
        return family5;
    }

    public String toString() {
        return getName() + ", " + getGroup() + ", " + getAddress() + ", " + getPhone() + ", " + getFamily1() + ", " + getFamily2() + ", " + getFamily3() + ", " + getFamily4() + ", " + getFamily5();
    }
}
