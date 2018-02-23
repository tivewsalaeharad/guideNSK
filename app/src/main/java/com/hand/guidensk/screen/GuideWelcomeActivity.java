package com.hand.guidensk.screen;

import com.hand.guidensk.R;
import com.hand.guidensk.constant.S;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.TitlePage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class GuideWelcomeActivity extends WelcomeActivity {

    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultBackgroundColor(R.color.colorPrimaryDark)
                .page(new TitlePage(R.drawable.screen_start, S.GUIDE_NSK))
                .page(new BasicPage(R.drawable.screen_main, S.WELCOME_MAIN, S.WELCOME_MAIN_TEXT))
                .page(new BasicPage(R.drawable.screen_to_see, S.WELCOME_CATEGORY, S.WELCOME_CATEGORY_TEXT))
                .page(new BasicPage(R.drawable.screen_places, S.WELCOME_PLACES, S.WELCOME_PLACES_TEXT))
                .page(new BasicPage(R.drawable.screen_place, S.WELCOME_PLACE, S.WELCOME_PLACE_TEXT))
                .page(new BasicPage(R.drawable.screen_place2, S.WELCOME_PLACE, S.WELCOME_PLACE_TEXT2))
                .page(new BasicPage(R.drawable.screen_favourites, S.WELCOME_FAVOURITES, S.WELCOME_FAVOURITES_TEXT))
                .page(new BasicPage(R.drawable.screen_route, S.WELCOME_ROUTE, S.WELCOME_ROUTE_TEXT))
                .swipeToDismiss(true)
                .build();
    }
}
