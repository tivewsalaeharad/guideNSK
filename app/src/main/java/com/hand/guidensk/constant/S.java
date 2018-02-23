package com.hand.guidensk.constant;

public class S {
    public static final String GUIDE_NSK = "Путеводитель по Новосибирску";
    public static final String WELCOME_MAIN = "Главный экран";
    public static final String WELCOME_MAIN_TEXT = "Здесь редставлены достопримечательности и организации города, разбитые по категориям";
    public static final String WELCOME_CATEGORY = "Категории";
    public static final String WELCOME_CATEGORY_TEXT = "Все категории делятся на подкатегории";
    public static final String WELCOME_PLACES = "Достопримечательности";
    public static final String WELCOME_PLACES_TEXT = "Внутри каждой вкладки находится список организаций или достопримечательностей города";
    public static final String WELCOME_PLACE = "Информация";
    public static final String WELCOME_PLACE_TEXT = "При выборе пункта с объектом, вызывается информационное окно с подробной информацией";
    public static final String WELCOME_PLACE_TEXT2 = "Можно не выходя из окна можно позвонить в организацию или перейти на её сайт, добавить в избрвнное";
    public static final String WELCOME_FAVOURITES = "Избранное";
    public static final String WELCOME_FAVOURITES_TEXT = "В Избранном можно перейти в информационное окно выбранных объектов, удалить объект из избранного";
    public static final String WELCOME_ROUTE = "Маршрут";
    public static final String WELCOME_ROUTE_TEXT = "Можно проложить маршрут до объекта";

    public static final String SQL_ALL = "SELECT * FROM sites";
    public static final String SQL_CATEGORY = "SELECT * FROM sites WHERE category=?";
    public static final String SQL_ID = "SELECT * FROM sites WHERE _id=?";

    public static final String WHOLE_DAY = "Круглосуточно\n";
    public static final String WORKING_HOURS_TEMPLATE = "Ежедневно с %s по %s\n";
    public static final String BREAK_HOURS_TEMPLATE = "Перерыв с %s по %s\n";
    public static final String NOW_OPENED = "Сейчас открыто";
    public static final String NOW_CLOSED = "Сейчас закрыто";
    public static final String IN_FAVOURITES = "В избранном";
    public static final String ADD_TO_FAVOURITES = "Добавить в избранное";
    public static final String DISTANCE = "Расстояние: ";
    public static final String DURATION = "Время в пути: ";
}
