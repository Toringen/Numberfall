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
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by Vyacheslav on 11.05.2019.
 */

public class StarController {
    private static SharedPreferences sPrefLevels;
    private static int[][] list_star_count;
    public static int count_stars, count_levels;

    public static void Initialize(SharedPreferences sPrefLevels_) {
        int modes  = 1;
        int levels = LevelController.GetLevelpack(0).length;
        sPrefLevels = sPrefLevels_;
        list_star_count = new int[modes][levels];
/*
        SharedPreferences.Editor ed = sPrefLevels.edit();
        for (int k = 0; k < modes; k++)
            for (int i = 35; i < 100; i++)
            {
                ed.putBoolean("M" + k + "H" + i, false);
                ed.putBoolean("M" + k + "L" + i, false);
            }
        ed.commit();
*/
        for (int k = 0; k < modes; k++)
            for (int i = 0; i < levels; i++)
            {
                     if (sPrefLevels.getBoolean("M" + k + "H" + i, false)) {
                         list_star_count[k][i] = 2;
                         count_stars += 2;
                         count_levels++;
                     }
                else if (sPrefLevels.getBoolean("M" + k + "L" + i, false)) {
                         list_star_count[k][i] = 1;
                         count_stars++;
                         count_levels++;
                     }
            }
            MessageController m = new MessageController();
            AchieveController.AddStar(m, count_stars);
            AchieveController.AddLevel(m, count_levels);
    }

    public static int GetCountStars(int mode, int level_id) {
        return list_star_count[mode][level_id];
    }

    public static void SetCountStars(int mode, int level_id, int value) {
        list_star_count[mode][level_id] = value;
    }

    public static String EndLevel(int id_mode, int level_id, boolean is_hardmode_level) {
        int sc = GetCountStars(id_mode, level_id);
        //if (sc == 2 || (sc == 1 && !is_hardmode_level)) return "";

        String rez = "";
        if (sc == 0) {
            SharedPreferences.Editor ed = sPrefLevels.edit();
            ed.putBoolean("M" + id_mode + "L" + level_id, true);
            ed.commit();

            rez = "L" + level_id + "_S" + count_stars;
            SetCountStars(id_mode, level_id, 1);
            count_stars++;
            count_levels++;
        }
        if (sc < 2 && is_hardmode_level) {
            SharedPreferences.Editor ed = sPrefLevels.edit();
            ed.putBoolean("M" + id_mode + "H" + level_id, true);
            ed.commit();

            if (rez == "") rez  = "S" + count_stars + "_S" + count_stars;
            else           rez += "_S" + count_stars;
            SetCountStars(id_mode, level_id, 2);
            count_stars++;
        }
        return rez;
    }

    //region Colors and max levels
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

    public static int GetMaxLevel() {
        return count_stars < 50 ?
                7 * ( 1 + min(count_stars / 5, 4)) :
                42 + 7 * (-4 + count_stars / 10);
    }

    public static int GetCountNeededStars(int id_level) {
        return id_level < 42 ?
                      5 * ( id_level       / 7) - count_stars :
                50 + 10 * ((id_level - 42) / 7) - count_stars;
    }
    //endregion
}
