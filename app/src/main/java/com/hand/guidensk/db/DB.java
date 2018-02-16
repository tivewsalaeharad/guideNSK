package com.hand.guidensk.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

public class DB {

    public static DatabaseHelper helper;
    public static SQLiteDatabase db;

    public static void init(Context context){
        helper = new DatabaseHelper(context);

        try {
            helper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = helper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }
}
