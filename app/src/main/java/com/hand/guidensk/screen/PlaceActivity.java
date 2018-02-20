package com.hand.guidensk.screen;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.guidensk.R;
import com.hand.guidensk.constant.Images;
import com.hand.guidensk.constant.Key;
import com.hand.guidensk.constant.S;
import com.hand.guidensk.utils.FavouritesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PlaceActivity extends AppCompatActivity implements View.OnClickListener {

    TextView title;
    TextView preDescription;
    ImageView image;
    TextView phone;
    TextView email;
    TextView address;
    TextView hours;
    TextView description;
    TextView favourites;
    double latitude;
    double longitude;

    private static SimpleDateFormat hhmm;
    private Date today;
    private int tag;

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
                FavouritesUtils.xor(tag);
                setFavourites();
                break;
            case R.id.route:
                Intent intent = new Intent(this, RouteActivity.class);
                //Освободить после отладки
                //intent.putExtra(Key.USER_LATITUDE, MainActivity.latitude);
                //intent.putExtra(Key.USER_LONGITUDE, MainActivity.longitude);
                intent.putExtra(Key.PLACE_LATITUDE, latitude);
                intent.putExtra(Key.PLACE_LONGITUDE, longitude);
                startActivity(intent);
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
        tag = intent.getIntExtra(Key.TAG, -1);
        if (tag != -1) image.setImageResource(Images.ARRAY[tag - 1]);
        putText(title, intent, Key.TITLE);
        putText(preDescription, intent, Key.PRE_DESCRIPTION);
        putText(phone, intent, Key.PHONE);
        putText(email, intent, Key.WEBSITE);
        putText(address, intent, Key.ADDRESS);
        putHours(intent);
        putText(description, intent, Key.DESCRIPTION);
        setFavourites();
        latitude = intent.getDoubleExtra(Key.LATITUDE, 55.0302577);
        longitude = intent.getDoubleExtra(Key.LONGITUDE, 82.9233965);

    }

    private void putText(TextView textView, Intent intent, String key) {
        String text = intent.getStringExtra(key);
        if ((text != null) && !text.equals("")) textView.setText(text);
        else textView.setVisibility(View.GONE);
    }

    private void putHours(Intent intent) {
        String s = "";
        String keyOpened = intent.getStringExtra(Key.OPENED);
        String keyClosed = intent.getStringExtra(Key.CLOSED);
        if (notExist(keyOpened, keyClosed)) return;
        boolean wholeDay = keyOpened.equals("0:00") && keyClosed.equals("24:00");
        String keyBreakStart = intent.getStringExtra(Key.BREAK_START);
        String keyBreakEnd = intent.getStringExtra(Key.BREAK_END);
        boolean opened = wholeDay || inTime(keyOpened, keyClosed) && !inTime(keyBreakStart, keyBreakEnd);
        s = s.concat(wholeDay ? S.WHOLE_DAY : String.format(S.WORKING_HOURS_TEMPLATE, keyOpened, keyClosed));
        if (!notExist(keyBreakStart, keyBreakEnd))
            s = s.concat(String.format(S.BREAK_HOURS_TEMPLATE, keyBreakStart, keyBreakEnd));
        s = s.concat(opened ? S.NOW_OPENED : S.NOW_CLOSED);
        hours.setTextColor(opened ? 0xFF008000 : 0xFF800000);
        hours.setText(s);
        hours.setVisibility(View.VISIBLE);
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

    private boolean notExist(String a, String b) {
        return (a == null) || (b == null) || a.equals("") || b.equals("");
    }

    private Date mouldTime(Date date) {
        Date newDate = new Date();
        newDate.setHours(date.getHours());
        newDate.setMinutes(date.getMinutes());
        return newDate;
    }

    private void setFavourites() {
        if (FavouritesUtils.selected(tag)) {
            favourites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_favorites_added, 0, 0, 0);
            favourites.setText("Удалить из избранного");
        } else {
            favourites.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_favorites_toadd, 0, 0, 0);
            favourites.setText("Добавить в избранное");
        }
    }

}
