package ru.startandroid.c9_simpledisorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vyacheslav on 23.05.2019.
 */

public class AchieveController {
    static SharedPreferences sPref;
    static Achieve[] list_achieve;
    static String[] mass_signs;
    static long[] mass_values;
    static Date[] mass_dates;
    static Date dateStartLevel;

    public static void Initialize() {
        mass_signs = new String[10];
        mass_values = new long[5];
        mass_dates = new Date[5];
        list_achieve = new Achieve[] {
			// 1
			    new Achieve(1, "MOVE_177013",    "Worst Ending Ever", R.drawable.sp_xor, "Набрать ровно 177013", ""),
                new Achieve(1, "MOVE_100000_0",  "There and back again", R.drawable.sp_xor, "Дойти до 100000, а потом обратно до 0", "", 2),
                new Achieve(1, "STEPS_2056",     "Profi", R.drawable.sp_xor, "Сделать 2056 действий", "", 2056),
                new Achieve(1, "STEPS_13118",    "Maniac", R.drawable.sp_xor, "Сделать 13118 действий", "", 13118),
                new Achieve(1, "WIN_3", "Chain of victory", R.drawable.sp_xor, "Выиграть 3 раза подряд без рестартов", "", 3),
                new Achieve(1, "WIN_5", "Complete the curcuit", R.drawable.sp_xor, "Выиграть 5 раз подряд без рестартов", "", 5),
                new Achieve(1, "TIME_5S", "Speed-o'-Sound Sonic", R.drawable.sp_xor, "Пройти уровень за 5 секунд", "", 3),
            
			// 2
			    new Achieve(1, "SAME_4", "Solidity", R.drawable.sp_xor, "4 раза подряд остаться с одним и тем же числом", "", 4),
                new Achieve(1, "SAME_8", "Hardness", R.drawable.sp_xor, "8 раз подряд остаться с одним и тем же числом", "", 8),
                new Achieve(1, "SIGNS_DIFF_5",  "Difference", R.drawable.sp_xor, "Использовать 5 разных действий подряд", ""),
                new Achieve(1, "SIGNS_SAME_30", "Stability", R.drawable.sp_xor, "Использовать 5 одинаковых действий подряд" , "", 5),
				new Achieve(1, "SIGNS_SAME_10", "Constancy", R.drawable.sp_xor, "Использовать 10 одинаковых действий подряд", "", 10),
                new Achieve(1, "SIGNS_SAME_15", "Permanence", R.drawable.sp_xor, "Использовать 15 одинаковых действий подряд", "", 15),
                new Achieve(1, "TIME_5L", "Genocide", R.drawable.sp_xor, "Использовать 5 знаков за 2 секунды", ""),
            
			// 3
		        new Achieve(1, "START_300", "This is SPARTA!", R.drawable.sp_xor, "Начать уровень 300 раз", "", 300),
        	    new Achieve(1, "STEPS_LEVEL_50", "Long game", R.drawable.sp_xor, "Использовать 50 чисел за уровень", "", 50),
                new Achieve(1, "STEPS_LEVEL_99", "Very long game", R.drawable.sp_xor, "Использовать 99 чисел за уровень", "", 99),
                new Achieve(1, "STEPS_LEVEL_200", "Long, long gaaaaaaame!", R.drawable.sp_xor, "Использовать 200 чисел за уровень", "", 200),
        		new Achieve(1, "S10",  "Darkness begins", R.drawable.sp_xor, "Открыть усложенный режим", ""),
                new Achieve(1, "L42", "42", R.drawable.sp_xor, "Пройти 42 уровня", "", 42),
                new Achieve(1, "L70", "Triumph", R.drawable.sp_xor, "Пройти все уровни", "", 70),

			// 4
		        new Achieve(1, "FIELD_EMPTY", "Emptyness", R.drawable.sp_xor, "Выиграть, использовав все числа на уровне", ""),
                new Achieve(1, "FIELD_ZERO", "It's all useless!", R.drawable.sp_xor, "Заполнить все поле нулями", ""),
                new Achieve(1, "POLARITY_7", "Renegade", R.drawable.sp_xor, "Сменить знак числа 7 раз за уровень", "", 7),
        	    new Achieve(1, "SLAUGHTERHOUSE9", "Slaughterhouse 9", R.drawable.sp_xor, "Получить девятку на 9 шаге 9 раз", "", 9),
        	    new Achieve(1, "S80",  "Unlocker", R.drawable.sp_xor, "Открыть все уровни", ""),
                new Achieve(1, "S50",  "Starlight", R.drawable.sp_xor, "Набрать 50 звезд", "", 50),
                new Achieve(1, "S140", "Purity", R.drawable.sp_xor, "Набрать 140 звезд", "", 140),

			// 5
		        new Achieve(1, "DIVIDE_ZERO", "Universe is collapsed", R.drawable.sp_xor, "Схлопнуть вселенную", "Разделить на 0"),
                //new Achieve(0, "SIGN_ALL", "", R.drawable.sp_xor, "Использовать все знаки в игре", ""),
        };
    }

    public static void AddStep(MessageController m, int count_steps, String sign, long current, Button[][] buttons) {
        for (int i = mass_values.length - 1; i > 0; i--) mass_values[i] = mass_values[i-1];
        for (int i = mass_signs.length - 1; i > 0; i--) mass_signs[i] = mass_signs[i-1];
        for (int i = mass_dates.length - 1; i > 0; i--) mass_dates[i] = mass_dates[i-1];
        mass_values[0] = current;
        mass_signs[0] = sign;
        mass_dates[0] = Calendar.getInstance().getTime();

        Achieve am10 = GetAchieve("MOVE_100000_0");
        if (am10.GetCounter() == 0 && current >= 100000) am10.SetCounter(1);
        else if (am10.GetCounter() == 1 && current <= 0) AchieveUnlock(m, am10);

        Achieve af0 = GetAchieve("FIELD_ZERO");
        if (!af0.isUnlocked() && CheckAchieveFieldZero(buttons)) AchieveUnlock(m, af0);

        AchieveAddCounter(m, "STEPS_LEVEL_50", 1);
        AchieveAddCounter(m, "STEPS_LEVEL_99", 1);
        AchieveAddCounter(m, "STEPS_LEVEL_200", 1);
        AchieveAddCounter(m, "STEPS_2056", 1);
        AchieveAddCounter(m, "STEPS_13118", 1);

        CheckAchieveUnlock(m, "MOVE_177013", current == 177013);
        CheckAchieveUnlock(m, "TIME_5L", count_steps >= 5 && Calendar.getInstance().getTime().getTime() - mass_dates[4].getTime() <= 2000);
        CheckAchieveUnlock(m, "SAME_4", count_steps >= 4
                && current == mass_values[1]
                && current == mass_values[2]
                && current == mass_values[3]);

		if (count_steps == 9 && current == 9)
			AchieveAddCounter(m, "SLAUGHTERHOUSE9", 1);
		
		if (current * mass_values[1] < 0 || mass_values[1] == 0 && current * mass_values[2] < 0)
			AchieveAddCounter(m, "POLARITY_7", 1);

        if (mass_values[0] == mass_values[1]) {
            AchieveAddCounter(m, "SAME_4", 1);
            AchieveAddCounter(m, "SAME_8", 1);
        } else {
            AchieveClearCounter("SAME_4");
            AchieveClearCounter("SAME_8");
        }

        if (mass_signs[0] == mass_signs[1]) {
            AchieveAddCounter(m, "SIGNS_SAME_10", 1);
            AchieveAddCounter(m, "SIGNS_SAME_15", 1);
            AchieveAddCounter(m, "SIGNS_SAME_30", 1);
        } else {
            AchieveClearCounter("SIGNS_SAME_10");
            AchieveClearCounter("SIGNS_SAME_15");
            AchieveClearCounter("SIGNS_SAME_30");
        }
        CheckAchieveUnlock(m, "SIGNS_DIFF_5",
                mass_signs[0] != mass_signs[1]
                        && mass_signs[0] != mass_signs[2]
                        && mass_signs[0] != mass_signs[3]
                        && mass_signs[0] != mass_signs[4]
                        && mass_signs[1] != mass_signs[2]
                        && mass_signs[1] != mass_signs[3]
                        && mass_signs[1] != mass_signs[4]
                        && mass_signs[2] != mass_signs[3]
                        && mass_signs[2] != mass_signs[4]
                        && mass_signs[3] != mass_signs[4]);
    }

    public static void AddLevel(MessageController m, int count_levels) {
        AchieveSetCounter(m, "L42", count_levels);
        AchieveSetCounter(m, "L70", count_levels);
        //CheckAchieveUnlock(m, "L42", count_levels >= 42);
        //CheckAchieveUnlock(m, "L70", count_levels >= 70);
    }

    public static void AddStar(MessageController m, int count_stars) {
        CheckAchieveUnlock(m, "S10", count_stars >= 10);
        CheckAchieveUnlock(m, "S80", count_stars >= 80);
        AchieveSetCounter(m, "S50", count_stars);
        AchieveSetCounter(m, "S140", count_stars);
    }

    public static void StartLevel(MessageController m) {
        try {
            dateStartLevel = Calendar.getInstance().getTime();
            AchieveAddCounter(m, "START_300", 1);
            AchieveClearCounter("STEPS_LEVEL_50");
            AchieveClearCounter("STEPS_LEVEL_99");
            AchieveClearCounter("STEPS_LEVEL_200");
            AchieveClearCounter("SIGNS_SAME_10");
            AchieveClearCounter("SIGNS_SAME_15");
            AchieveClearCounter("SIGNS_SAME_30");
            AchieveClearCounter("MOVE_100000_0");
            AchieveClearCounter("POLARITY_7");
        } catch (Exception e) {
        }
    }

    public static void RestartLevel(MessageController m) {
        StartLevel(m);
        AchieveClearCounter("WIN_3");
        AchieveClearCounter("WIN_5");
    }

    public static void EndLevel(MessageController m, String sign, long current, Button[][] buttons) {
        Date currentTime = Calendar.getInstance().getTime();
		if (currentTime.getTime() - dateStartLevel.getTime() <= 5000)
			AchieveAddCounter(m, "TIME_5S", 1);
        AchieveAddCounter(m, "WIN_3", 1);
        AchieveAddCounter(m, "WIN_5", 1);

        Achieve af0 = GetAchieve("FIELD_EMPTY");
        if (!af0.isUnlocked() && CheckAchieveFieldEmpty(buttons)) AchieveUnlock(m, af0);
    }

    private static boolean CheckAchieveFieldZero(Button[][] buttons) {
        for (Button[] mass : buttons)
            for (Button btn : mass)
                if (btn.getText() != "0")
                    return false;
        return true;
    }

    private static boolean CheckAchieveFieldEmpty(Button[][] buttons) {
        for (Button[] mass : buttons)
            for (Button btn : mass)
                if (btn.getText() != "")
                    return false;
        return true;
    }


    private static void AchieveUnlock(String code) {
        GetAchieve(code).Unlock();
    }

    private static boolean AchieveUnlock(MessageController m, Achieve a) {
        if (a.Unlock()) {
            m.AddMessage("A:" + a.GetLabelAfter());
            return true;
        };
        return false;
    }

    public static boolean CheckAchieveUnlock(MessageController m, String code, boolean is_unlocked) {
        if (is_unlocked) {
            Achieve a = GetAchieve(code);
            if (a.Unlock()) {
                m.AddMessage("A:" + a.GetLabelAfter());
                return true;
            };
        }
        return false;
    }

    private static void AchieveAddCounter(String code, int counter_add) {
        GetAchieve(code).AddCounter(counter_add);
    }

    private static boolean AchieveAddCounter(MessageController m, String code, int counter_add) {
        Achieve a = GetAchieve(code);
        if (a.AddCounter(counter_add)) {
            m.AddMessage("A:" + a.GetLabelAfter());
            return true;
        };
        return false;
    }

    private static boolean AchieveSetCounter(MessageController m, String code, int counter_add) {
        Achieve a = GetAchieve(code);
        if (a.SetCounter(counter_add)) {
            m.AddMessage("A:" + a.GetLabelAfter());
            return true;
        };
        return false;
    }

    private static void AchieveClearCounter(String code) {
        GetAchieve(code).SetCounter(0);
    }

    //region Basic Reload Erika Things!
    public static void Load(SharedPreferences sPrefAchieve) {
        sPref = sPrefAchieve;
        String list_unlocked = sPref.getString("list_unlocked", "");
        if (list_unlocked != "")
            for (String code : list_unlocked.split(";"))
                AchieveUnlock(code);

        String list_counters = sPref.getString("list_counters", "");
        if (list_counters != "")
            for (String s : list_counters.split(";")) {
                int index = s.indexOf(":");
                String code = s.substring(0, index);
                int value = Integer.parseInt(s.substring(index + 1));
                AchieveAddCounter(code, value);
            }
    }

    public static void Save() {
        String list_unlocked = "", list_counters = "";
        for (Achieve a : list_achieve) {
            if (a.isUnlocked())
                list_unlocked += ";" + a.GetCode();
            else if (a.GetCounter() > 0)
                list_counters += ";" + a.GetCode() + ":" + a.GetCounterMax();
        }
        if (list_unlocked != "") list_unlocked = list_unlocked.substring(1);
        if (list_counters != "") list_counters = list_counters.substring(1);

        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("list_unlocked", list_unlocked);
        ed.putString("list_counters", list_counters);
        ed.commit();
    }

    public static Achieve GetAchieve(String code) {
        for (Achieve a : list_achieve)
            if (a.IsItYou(code))
                return a;
        return null;
    }

    public static Achieve GetAchieveOnID(int id) {
        return list_achieve[id];
    }

    public static boolean[] GetListUnlocked() {
        boolean[] rez = new boolean[list_achieve.length];
        for (int i = 0; i < list_achieve.length; i++)
            rez[i] = list_achieve[i].isUnlocked();
        return rez;
    }
    //endregion
}
