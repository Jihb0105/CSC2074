package com.bignerdranch.android.choresmaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChoresBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "choresData.db";

    public ChoresBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    //Creating the Table in the database
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + ChoresData.ChoresTable.TABLE_NAME + "(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ChoresData.ChoresTable.Columns.UUID + " INTEGER, " +
                ChoresData.ChoresTable.Columns.TITLE + " TEXT, " +
                ChoresData.ChoresTable.Columns.DATE + " INTEGER, " +
                ChoresData.ChoresTable.Columns.COMPLETED + " INTEGER" +
                ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){

    }
}
