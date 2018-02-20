package com.hand.guidensk.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import com.hand.guidensk.constant.Key;
import com.hand.guidensk.constant.S;
import com.hand.guidensk.db.DB;
import com.hand.guidensk.screen.PlaceActivity;

public class ShowPlaceUtils {

    public static void showPlace(Activity activity, int index) {
        Cursor cursor = DB.db.rawQuery(S.SQL_ID, new String[] {index + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Intent intent = new Intent(activity, PlaceActivity.class);
            intent.putExtra(Key.TAG, index);
            intent.putExtra(Key.TITLE, cursor.getString(1));
            intent.putExtra(Key.PRE_DESCRIPTION, cursor.getString(2));
            intent.putExtra(Key.DESCRIPTION, cursor.getString(3));
            intent.putExtra(Key.ADDRESS, cursor.getString(4));
            intent.putExtra(Key.PHONE, cursor.getString(5));
            intent.putExtra(Key.WEBSITE, cursor.getString(6));
            intent.putExtra(Key.OPENED, cursor.getString(7));
            intent.putExtra(Key.CLOSED, cursor.getString(8));
            intent.putExtra(Key.BREAK_START, cursor.getString(9));
            intent.putExtra(Key.BREAK_END, cursor.getString(10));
            intent.putExtra(Key.LATITUDE, cursor.getDouble(11));
            intent.putExtra(Key.LONGITUDE, cursor.getDouble(12));
            activity.startActivity(intent);
        }
        cursor.close();
    }
}
