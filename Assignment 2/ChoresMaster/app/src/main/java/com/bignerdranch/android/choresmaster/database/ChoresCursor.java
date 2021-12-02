package com.bignerdranch.android.choresmaster.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.choresmaster.Chores;

import java.util.Date;
import java.util.UUID;

public class ChoresCursor extends CursorWrapper {
    public ChoresCursor (Cursor c) {
        super(c);
    }

    public Chores getChore() {
        //to get relevant column data
        String uuidString = getString(getColumnIndex(ChoresData.ChoresTable.Columns.UUID));
        String title = getString(getColumnIndex(ChoresData.ChoresTable.Columns.TITLE));
        long date = getLong(getColumnIndex(ChoresData.ChoresTable.Columns.DATE));
        int isCompleted = getInt(getColumnIndex(ChoresData.ChoresTable.Columns.COMPLETED));


        Chores chores = new Chores(UUID.fromString(uuidString));
        chores.setTitle(title);
        chores.setDate(new Date(date));
        chores.setCompleted(isCompleted != 0);

        return chores;

    }
}
