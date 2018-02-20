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
import com.hand.guidensk.utils.FavouritesUtils;
import com.hand.guidensk.utils.ShowPlaceUtils;


import java.util.ArrayList;

public class FavouritesAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Integer> objects;

    public FavouritesAdapter(Activity screen, ArrayList<Integer> items) {
        activity = screen;
        objects = items;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_favourites, parent, false);
        }
        final int index = objects.get(position);
        Cursor cursor = DB.db.rawQuery(S.SQL_ID, new String[] {index + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            ((TextView) view.findViewById(R.id.title)).setText(cursor.getString(1));
            ((ImageView) view.findViewById(R.id.image)).setImageResource(Images.ARRAY[index - 1]);
            (view.findViewById(R.id.remove)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouritesUtils.xor(index);
                    objects.remove(position);
                    notifyDataSetChanged();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPlaceUtils.showPlace(activity, index);
                }
            });
        }
        cursor.close();
        return view;
    }
}
