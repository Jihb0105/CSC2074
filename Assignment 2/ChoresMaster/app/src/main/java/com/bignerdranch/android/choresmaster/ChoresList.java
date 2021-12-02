package com.bignerdranch.android.choresmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;

import com.bignerdranch.android.choresmaster.database.ChoresBaseHelper;
import com.bignerdranch.android.choresmaster.database.ChoresCursor;
import com.bignerdranch.android.choresmaster.database.ChoresData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChoresList {
    private static ChoresList sChoresList;

    //private List<Chores> cChores;
    private Context c;
    private SQLiteDatabase cData;

    public static ChoresList get(Context context){
        if(sChoresList == null){
            sChoresList = new ChoresList(context);
        }
        return sChoresList;
    }

    private ChoresList(Context context){
        c = context.getApplicationContext();
        cData =  new ChoresBaseHelper(c).getWritableDatabase();

    }

    //Returning chores from Chores.java
    public List<Chores> getChores() {
        List<Chores> chores = new ArrayList<>();
        ChoresCursor c = queryChores(null, null);

        try{
            c.moveToFirst();
            while (!c.isAfterLast()){
                chores.add(c.getChore());
                c.moveToNext();
            }
        }finally{
            c.close();
        }
        return chores;
    }

    //getter to get the choresID
    public Chores getChore (UUID id){
        ChoresCursor c = queryChores(
                ChoresData.ChoresTable.Columns.UUID + " = ?",
                new String[]{ id.toString() }
        );
        try{
            if (c.getCount() == 0){
                return null;
            }
            c.moveToFirst();
            return c.getChore();
        } finally {
            c.close();
        }
    }

    //Updating a chore in the table
    public void updateChores(Chores c){
        String uuidString = c.getId().toString();
        ContentValues values = getContentValues(c);

        cData.update(ChoresData.ChoresTable.TABLE_NAME, values,
                ChoresData.ChoresTable.Columns.UUID + " = ?",
                new String[] {uuidString});
    }

    //Reading from the database
   // private Cursor queryChores(String whereClause, String[] whereArgs){
    private ChoresCursor queryChores(String whereClause, String[] whereArgs){
        Cursor c = cData.query(
                ChoresData.ChoresTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new ChoresCursor(c);
        //return c;
    }

    //Writing to the database
    private static ContentValues getContentValues(Chores c) {
        ContentValues values = new ContentValues();
        values.put(ChoresData.ChoresTable.Columns.UUID, c.getId().toString());
        values.put(ChoresData.ChoresTable.Columns.TITLE, c.getTitle());
        values.put(ChoresData.ChoresTable.Columns.DATE, c.getDate().getTime());
        values.put(ChoresData.ChoresTable.Columns.COMPLETED, c.isCompleted() ? 1 : 0);

        return values;
    }

    public void addChores(Chores c){ //method to add chores
        //Inserting a row
        ContentValues values = getContentValues(c);

        cData.insert(ChoresData.ChoresTable.TABLE_NAME, null, values);
    }
    public void deleteChores(Chores c) { //method to delete chores
        //Delete a row from the database table
        cData.delete(ChoresData.ChoresTable.TABLE_NAME,
                ChoresData.ChoresTable.Columns.UUID + " = ?",
                new String[]{c.getId().toString()});
    }

}
