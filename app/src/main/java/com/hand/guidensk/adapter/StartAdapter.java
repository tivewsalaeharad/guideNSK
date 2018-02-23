package com.hand.guidensk.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.guidensk.R;
import com.hand.guidensk.constant.Images;
import com.hand.guidensk.constant.S;
import com.hand.guidensk.db.DB;
import com.hand.guidensk.utils.ShowPlaceUtils;

import java.util.ArrayList;

public class StartAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;

    public StartAdapter(Activity screen, int index) {
        activity = screen;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        names = new ArrayList<>();
        ids = new ArrayList<>();
        Cursor cursor = DB.db.rawQuery(S.SQL_CATEGORY, new String[]{index + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                names.add(cursor.getString(1));
                ids.add(cursor.getInt(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = inflater.inflate(R.layout.item_start, parent, false);
        (view.findViewById(R.id.background)).setBackgroundColor((isOdd(position)? 0xFFB0B0B0 : 0xFF404040));
        ((TextView) view.findViewById(R.id.place_name)).setText(names.get(position));
        ((ImageView) view.findViewById(R.id.place_image)).setImageResource(Images.ARRAY[ids.get(position) - 1]);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPlaceUtils.showPlace(activity, ids.get(position));
            }
        });
        return view;
    }

    private boolean isOdd( int val ) { return (val & 0x01) != 0; }
}