package ru.startandroid.c9_simpledisorder;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Vyacheslav on 04.05.2019.
 */

public class SignController {
    static Sign[] list_signs;

    public static void Initialize()
    {
        list_signs = new Sign[] {
                new Sign("+", 0.5, R.drawable.sa_plus, R.raw.up2),
                new Sign("-", 0.5, R.drawable.sa_minus, R.raw.up1),
                new Sign("*", 0.1, R.drawable.sa_multiply, R.raw.up3),
                new Sign("/", 0.1, R.drawable.sa_division, R.raw.up0),
                new Sign("G", 0.15, R.drawable.s_gemini, R.raw.action3),
                new Sign("C", 0.15, R.drawable.s_cancer, R.raw.action3),
                new Sign("L", 0.15, R.drawable.s_libra, R.raw.action1),
                new Sign("P", 0.15, R.drawable.sa_square, R.raw.action2),
                new Sign("+2*", 0.4, R.drawable.sb_plus2, R.raw.up2),
                new Sign("+10*", 0.4, R.drawable.sb_plus10, R.raw.action2),
                new Sign("-3*", 0.4, R.drawable.sb_minus3, R.raw.up0),
                new Sign("&", 0.5, R.drawable.sp_and, R.raw.up2),
                new Sign("^", 0.5, R.drawable.sp_or, R.raw.up1),
                new Sign("⊕", 0.4, R.drawable.sp_xor, R.raw.up3),
                new Sign("<<", 0.1, R.drawable.sp_left, R.raw.action2),
                new Sign(">>", 0.1, R.drawable.sp_right, R.raw.action2),
                new Sign("+1", 0.1, R.drawable.sp_inc, R.raw.action1),
                new Sign("-1", 0.1, R.drawable.sp_dec, R.raw.action1)
        };
    }

    public static int GetSignIndex(String sign) {
        int index = 0;
        while (index < list_signs.length && !list_signs[index].sign.equals(sign)) index++;
        return (index >= list_signs.length) ? 0 : index;

        //int index = 0;
        //while (!list_signs[index].sign.equals(sign)) {
        //    index++;
        //    if (index >= list_signs.length) return list_signs.length - 1;
        //}
        //return index;
    }

    public static Sign GetSign(String sign) {
        int index = GetSignIndex(sign);
        return (index < 0 || index >= list_signs.length) ? null : list_signs[index];
    }
/*
    public static void FillArrayChances(String sign, double[] array) {
        int index = GetSignIndex(sign);
        if (index == -1) return;
        array[index] = list_signs[index].default_chance;
    }
*/
    public static MediaPlayer[] GetListPlayers(Context context) {
        MediaPlayer[] players = new MediaPlayer[list_signs.length];
        for (int i = 0; i < players.length; i++)
            players[i] = MediaPlayer.create(context, list_signs[i].id_sound);
        return players;
    }
}
