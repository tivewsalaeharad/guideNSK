package com.hand.guidensk.constant;

public class S {
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
