package com.bignerdranch.android.choresmaster.database;

public class ChoresData {
    //Creating Database Table
    public static final class ChoresTable{
        public static final String TABLE_NAME = "chores"; //Table Name

        //Setting up the database table columns
        public static final class Columns {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String COMPLETED = "completed";

        }
    }
}
