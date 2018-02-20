package com.hand.guidensk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hand.guidensk.constant.Key;

public class FavouritesUtils {

    private static long set;

    public static void xor(int index) {
        set = set ^ (0x01L << index - 1);
    }

    public static boolean selected(int index) {
        return (set & (0x01L << index - 1)) != 0;
    }

    public static void get(Activity activity) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        set = preferences.getLong(Key.FAVOURITES, 0);
    }

    public static void put(Activity activity) {
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(Key.FAVOURITES, set);
        ed.apply();
    }

}
