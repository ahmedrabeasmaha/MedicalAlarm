package com.example.medicinealarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class myDbAdapter extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Medicine_Table.db";


    public myDbAdapter(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        try {
            db.execSQL(
                    "create table medicine " +
                            "(id integer not null primary key AUTOINCREMENT, medicine_name text, image blob, same_time boolean)"
            );
            db.execSQL(
                    "create table medicine_time " +
                            "(id integer primary key AUTOINCREMENT, alarm_hour text, alarm_minute text, day text, medicine_num integer not null, foreign key (medicine_num) REFERENCES medicine(id) on delete cascade)"
            );
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        onCreate(db);
    }

    public boolean insertMedicine(String medicine_name, byte[] image, boolean same_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("medicine_name", medicine_name);
        contentValues.put("image", image);
        contentValues.put("same_time", same_time);
        db.insert("medicine", null, contentValues);
        return true;
    }

    public boolean insertMedicineTime(String alarm_hour, String alarm_minute, String day) {
        SQLiteDatabase curr = this.getReadableDatabase();
        Cursor res = curr.rawQuery("select id from medicine", null);
        res.moveToLast();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_hour", alarm_hour);
        contentValues.put("alarm_minute", alarm_minute);
        contentValues.put("day", day);
        contentValues.put("medicine_num", res.getString(0));
        db.insert("medicine_time", null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from medicine", null);
        return res;
    }

    public boolean updateData(Integer id, String name, String phone, String email, String street, String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }


}