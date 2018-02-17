package com.hand.guidensk.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hand.guidensk.R;

public class PlaceActivity extends AppCompatActivity {

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
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_LONGITUDE = "Longitude";

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

    private static final String workingHoursTemplate = "Ежедневно с %s по %s";
    private static final String breakHoursTemplate = "Перерыв с %s по %s";

    TextView title;
    TextView preDescription;
    ImageView image;
    TextView phone;
    TextView email;
    TextView address;
    TextView workingHours;
    TextView breakHours;
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        title = findViewById(R.id.title);
        preDescription = findViewById(R.id.pre_description);
        image = findViewById(R.id.image);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        workingHours = findViewById(R.id.working_hours);
        breakHours = findViewById(R.id.break_hours);
        description = findViewById(R.id.description);
        Intent intent = getIntent();
        int tag = intent.getIntExtra(KEY_TAG, -1);
        if(tag != -1) image.setImageResource(images[tag - 1]);
        putText(title, intent, KEY_TITLE);
        putText(preDescription, intent, KEY_PRE_DESCRIPTION);
        putText(phone, intent, KEY_PHONE);
        putText(email, intent, KEY_WEBSITE);
        putText(address, intent, KEY_ADDRESS);
        putDualText(workingHours, intent, workingHoursTemplate, KEY_OPENED, KEY_CLOSED);
        putDualText(breakHours, intent, breakHoursTemplate, KEY_BREAK_START, KEY_BREAK_END);
        putText(description, intent, KEY_DESCRIPTION);

        double latitude = intent.getDoubleExtra(KEY_LATITUDE, 0);
        double longitude = intent.getDoubleExtra(KEY_LONGITUDE, 0);
    }

    private void putText(TextView textView, Intent intent, String key) {
        String text = intent.getStringExtra(key);
        if ((text != null) && !text.equals("")) textView.setText(text);
    }

    private void putDualText(TextView textView, Intent intent, String template, String key1, String key2) {
        String a = intent.getStringExtra(key1);
        String b = intent.getStringExtra(key2);
        if ((a != null) && (b != null) && !a.equals("")&& !b.equals(""))
            textView.setText(String.format(template, a, b));
    }
}
