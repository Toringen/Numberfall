package ru.startandroid.c9_simpledisorder;

import android.content.SharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vyacheslav on 11.05.2019.
 */

public class StarController {
    private static int[][] list_star_count;

    public static void Initialize(SharedPreferences sPrefLevels) {
        int count_modes  = 1;
        int count_levels = LevelController.GetLevelpack(0).length;
        list_star_count = new int[count_levels][count_modes];
        for (int k = 0; k < count_modes; k++)
            for (int i = 0; i < count_levels; i++)
            {
                     if (sPrefLevels.getBoolean("M" + k + "H" + i, false)) list_star_count[k][i] = 2;
                else if (sPrefLevels.getBoolean("M" + k + "L" + i, false)) list_star_count[k][i] = 1;
            }
    }

    public static int GetCountStars(int mode, int level_id) {
        return list_star_count[mode][level_id];
    }

    public static int GetMenuButtonColor(int mode, int level_id, boolean is_hardmode) {
        return GetMenuButtonColor(GetCountStars(mode, level_id), is_hardmode);
    }

    private static int GetMenuButtonColor(int count_stars, boolean is_hardmode) {
        switch (count_stars)
        {
            case 0: return is_hardmode ? R.color.menu_buttonblack_star0 : R.color.menu_buttonwhite_star0;
            case 1: return is_hardmode ? R.color.menu_buttonblack_star1 : R.color.menu_buttonwhite_star1;
            case 2: return is_hardmode ? R.color.menu_buttonblack_star2 : R.color.menu_buttonwhite_star2;
            default: return 0;
        }
    }




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
