package com.example.taskscheduler.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.taskscheduler.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static final String COL_4 = "DATE";
    private static final String COL_5 = "START_TIME";
    private static final String COL_6 = "END_TIME";
    private static final String COL_7 = "ACTIVITIES";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER, DATE TEXT, START_TIME TEXT, END_TIME TEXT, ACTIVITIES TEXT)");
    }

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDoModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, 0);
        values.put(COL_4, model.getDate());
        values.put(COL_5, model.getStartTime());
        values.put(COL_6, model.getEndTime());
        values.put(COL_7, model.getActivity());
        db.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {
        db = this.getWritableDatabase();

        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                    task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                    task.setDate(cursor.getString(cursor.getColumnIndex(COL_4)));
                    task.setStartTime(cursor.getString(cursor.getColumnIndex(COL_5)));
                    task.setEndTime(cursor.getString(cursor.getColumnIndex(COL_6)));
                    task.setActivity(cursor.getString(cursor.getColumnIndex(COL_7)));
                    modelList.add(task);
                } while (cursor.moveToNext());
            }
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return modelList;
    }
}