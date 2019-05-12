package ru.startandroid.c9_simpledisorder;

/**
 * Created by Vyacheslav on 04.05.2019.
 */

public class Sign {
    String sign;
    double default_chance;
    int id_image, id_sound;

    public Sign(String sign, double default_chance, int id_image, int id_sound) {
        this.sign = sign;
        this.default_chance = default_chance;
        this.id_image = id_image;
        this.id_sound = id_sound;
    }
}
