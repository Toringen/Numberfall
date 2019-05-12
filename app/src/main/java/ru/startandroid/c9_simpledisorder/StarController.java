package ru.startandroid.c9_simpledisorder;

/**
 * Created by Vyacheslav on 11.05.2019.
 */

public class StarController {
    public static int GetMaxLevel(int count_stars) {
        return count_stars < 50 ?
                7 * ( 1 + count_stars / 5) :
                42 + 7 * (-4 + count_stars / 10);
    }

    public static int GetCountNeededStars(int id_level, int count_stars) {
        return id_level < 42 ?
                      5 * ( id_level       / 7) - count_stars :
                50 + 10 * ((id_level - 42) / 7) - count_stars;
    }
}
