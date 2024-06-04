package com.example.taskscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Register.db";
    private static final String TABLE_NAME = "register_user";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "FIRST_NAME";
    private static final String COL_3 = "LAST_NAME";
    private static final String COL_4 = "EMAIL";
    private static final String COL_5 = "PHONE";
    private static final String COL_6 = "COUNTRY";
    private static final String COL_7="PASSWORD";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRST_NAME TEXT, LAST_NAME TEXT, EMAIL TEXT, PHONE TEXT, COUNTRY TEXT,PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, user.getFirstName());
        contentValues.put(COL_3, user.getLastName());
        contentValues.put(COL_4, user.getEmail());
        contentValues.put(COL_5, user.getPhone());
        contentValues.put(COL_6, user.getCountry());
        contentValues.put(COL_7, user.getPassword());
        long res = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return res;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM register_user WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getNamesByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + COL_2 + ", " + COL_3 + " FROM " + TABLE_NAME + " WHERE " + COL_4 + "=?", new String[]{email});
        return res;
    }
}

