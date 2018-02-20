package com.hand.guidensk.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.hand.guidensk.R;
import com.hand.guidensk.adapter.FavouritesAdapter;
import com.hand.guidensk.utils.FavouritesUtils;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {
    ListView listView;
    FavouritesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        listView = findViewById(R.id.list_view);
        drawFavourites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawFavourites();
    }

    private void drawFavourites() {
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 1; i <= 40; i++) if (FavouritesUtils.selected(i)) items.add(i);
        adapter = new FavouritesAdapter(this, items);
        listView.setAdapter(adapter);
    }
}
