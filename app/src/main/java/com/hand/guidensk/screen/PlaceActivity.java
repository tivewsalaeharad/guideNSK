package com.hand.guidensk.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.guidensk.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String KEY_TAG = "Tag";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_PRE_DESCRIPTION = "PreDescription";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_ADDRESS = "Address";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_WEBSITE = "Website";
    private static final String KEY_OPENED = "Opened";
    private static final String KEY_CLOSED = "Closed";
    private static final String KEY_BREAK_START = "Break start";
    private static final String KEY_BREAK_END = "Break end";
    private static final String KEY_FAVOURITES = "Favourites";

    private int[] images = {
            R.drawable.image_dinner_cardamon,
            R.drawable.image_dinner_travellers,
            R.drawable.image_dinner_westeast,
            R.drawable.image_dinner_coffeecup,
            R.drawable.image_dinner_om,
            R.drawable.image_dinner_shafran,
            R.drawable.image_dinner_lpshbr,
            R.drawable.image_hotel_guestcourt,
            R.drawable.image_hotel_hfotel,
            R.drawable.image_hotel_domina,
            R.drawable.image_hotel_centeravanta,
            R.drawable.image_hotel_marins,
            R.drawable.image_hotel_globus,
            R.drawable.image_hotel_n,
            R.drawable.image_hotel_radisson,
            R.drawable.image_hotel_riverpark,
            R.drawable.image_cinema_victory,
            R.drawable.image_cinema_7thsky,
            R.drawable.image_cinema_luxor,
            R.drawable.image_shop_aura,
            R.drawable.image_shop_versaille,
            R.drawable.image_shop_mega,
            R.drawable.image_shop_galery,
            R.drawable.image_shop_royalpark,
            R.drawable.image_theatre_operaballey,
            R.drawable.image_theatre_globus,
            R.drawable.image_theatre_katz,
            R.drawable.image_museum_region,
            R.drawable.image_museum_rerikh,
            R.drawable.image_museum_art,
            R.drawable.image_interest_transfiguration,
            R.drawable.image_interest_ascencion,
            R.drawable.image_interest_alexandrnevskiy,
            R.drawable.image_interest_library,
            R.drawable.image_interest_glory,
            R.drawable.image_interest_hundredflats,
            R.drawable.image_entertainment_zoo,
            R.drawable.image_entertainment_spartak,
            R.drawable.image_entertainment_circus,
            R.drawable.image_entertainment_centralpark
    };

    private static final String WHOLE_DAY = "Круглосуточно\n";
    private static final String WORKING_HOURS_TEMPLATE = "Ежедневно с %s по %s\n";
    private static final String BREAK_HOURS_TEMPLATE = "Перерыв с %s по %s\n";
    private static final String NOW_OPENED = "Сейчас открыто";
    private static final String NOW_CLOSED = "Сейчас закрыто";

    TextView title;
    TextView preDescription;
    ImageView image;
    TextView phone;
    TextView email;
    TextView address;
    TextView hours;
    TextView description;
    TextView favourites;

    private static SimpleDateFormat hhmm;
    private Date today;
    private int tag;
    private SharedPreferences preferences;
    private long favouriteSet;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(email.getText().toString())));
                break;
            case R.id.phone:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString())));
                break;
            case R.id.favourites:
                xorFavourites();
                setFavourites();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        hhmm = new SimpleDateFormat("HH:mm", Locale.getDefault());
        hhmm.setTimeZone(TimeZone.getTimeZone("GMT+07"));
        today = new Date();
        preferences = getPreferences(MODE_PRIVATE);
        favouriteSet = preferences.getLong(KEY_FAVOURITES, 0);
        title = findViewById(R.id.title);
        preDescription = findViewById(R.id.pre_description);
        image = findViewById(R.id.image);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        hours = findViewById(R.id.hours);
        description = findViewById(R.id.description);
        favourites = findViewById(R.id.favourites);
        Intent intent = getIntent();
        tag = intent.getIntExtra(KEY_TAG, -1);
        if (tag != -1) image.setImageResource(images[tag - 1]);
        putText(title, intent, KEY_TITLE);
        putText(preDescription, intent, KEY_PRE_DESCRIPTION);
        putText(phone, intent, KEY_PHONE);
        putText(email, intent, KEY_WEBSITE);
        putText(address, intent, KEY_ADDRESS);
        putHours(intent);
        putText(description, intent, KEY_DESCRIPTION);
        setFavourites();
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(KEY_FAVOURITES, favouriteSet);
        ed.apply();
        super.onStop();
    }

    private void putText(TextView textView, Intent intent, String key) {
        String text = intent.getStringExtra(key);
        if ((text != null) && !text.equals("")) textView.setText(text);
        else textView.setVisibility(View.GONE);
    }

    private void putHours(Intent intent) {
        String s = "";
        String keyOpened = intent.getStringExtra(KEY_OPENED);
        String keyClosed = intent.getStringExtra(KEY_CLOSED);
        boolean wholeDay = keyOpened.equals("0:00") && keyClosed.equals("24:00");
        String keyBreakStart = intent.getStringExtra(KEY_BREAK_START);
        String keyBreakEnd = intent.getStringExtra(KEY_BREAK_END);
        boolean opened = wholeDay || inTime(keyOpened, keyClosed) && !inTime(keyBreakStart, keyBreakEnd);
        if ((keyOpened != null) && (keyClosed != null) && !keyOpened.equals("") && !keyClosed.equals(""))
            s = s.concat(wholeDay ? WHOLE_DAY : String.format(WORKING_HOURS_TEMPLATE, keyOpened, keyClosed));
        if ((keyBreakStart != null) && (keyBreakEnd != null) && !keyBreakStart.equals("") && !keyBreakEnd.equals(""))
            s = s.concat(String.format(BREAK_HOURS_TEMPLATE, keyBreakStart, keyBreakEnd));
        if (!s.equals("")) {
            s = s.concat(opened ? NOW_OPENED : NOW_CLOSED);
            hours.setTextColor(opened ? 0xFF008000 : 0xFF800000);
            hours.setText(s);
        } else hours.setVisibility(View.GONE);
    }

    private boolean inTime(String strStart, String strEnd) {
        try {
            Date start = mouldTime(hhmm.parse(strStart));
            Date end = mouldTime(hhmm.parse(strEnd));
            return start.before(end) ? today.after(start) && today.before(end) : today.after(start) || today.before(end);
        } catch (Exception e) {
            return false;
        }
    }

    private Date mouldTime(Date date) {
        Date newDate = new Date();
        newDate.setHours(date.getHours());
        newDate.setMinutes(date.getMinutes());
        return newDate;
    }

    private void xorFavourites() {
        favouriteSet = favouriteSet ^ (0x01 << tag - 1);
    }

    private void setFavourites() {
        if ((favouriteSet & (0x01 << tag - 1)) != 0) {
            favourites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_favorites_added, 0, 0, 0);
            favourites.setText("Удалить из избранного");
        } else {
            favourites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_favorites_toadd, 0, 0, 0);
            favourites.setText("Добавить в избранное");
        }
    }

}
