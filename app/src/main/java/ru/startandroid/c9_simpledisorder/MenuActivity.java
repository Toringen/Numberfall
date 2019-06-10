package ru.startandroid.c9_simpledisorder;

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

public class MenuActivity extends AppCompatActivity {

    SharedPreferences sPrefLevels, sPrefProgress;
    ConstraintLayout gridMain;
    LinearLayout grid, gridAchieveInfo;
    GridLayout gridLevelInfo;
    Button btnStart, btnPageLeft, btnPageRight;
    ImageButton btnHardmode, btnAchieve;
    TextView tvTitle, tvError, tvLevelDifficulty, tvLevelCurrent, tvLevelResult, tvLevelProgress, tvLevelSigns, 
            tvAchieveTitle, tvAchieveCaption;
    ImageView imagePanel, imageAchieveStarLeft, imageAchieveStarRight;
    Button[] list_buttons;
    ImageView[] images_star, images_sign;
    boolean[] list_achieve_unlocked;
    boolean is_hardmode_level, is_achieve;
    int id_mode, id_page, id_button_choosen, state_godmode, max_level;
    String[] list_difficulties = new String[] { "EASY", "NORMAL", "HARD", "LUNATIC",
            "EXTRA", "PRINCE", "SORCERER", "THE DOUBLE", "REVOLUTIONARY", "KING" };

    Random rand = new Random();
    int BUTTON_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        try {
            SignController.Initialize();
            AchieveController.Initialize();
            AchieveController.Load( getSharedPreferences("Achieve", MODE_PRIVATE));
            sPrefLevels = getSharedPreferences(getString(R.string.PREF_FILE_LEVELS), MODE_PRIVATE);
            sPrefProgress = getSharedPreferences(getString(R.string.PREF_FILE_PROGRESS), MODE_PRIVATE);
            gridMain = findViewById(R.id.menuGridMain);
            grid = findViewById(R.id.menuGrid);
            gridLevelInfo = findViewById(R.id.menuLayoutLevelInfo);
            gridAchieveInfo = findViewById(R.id.menuLayoutAchieveInfo);
            tvTitle = findViewById(R.id.menuTextViewTitle);
            tvError = findViewById(R.id.menuTextViewError);
            tvLevelDifficulty = findViewById(R.id.menuTextViewDiff);
            tvLevelCurrent = findViewById(R.id.menuTextViewCurrent);
            tvLevelResult = findViewById(R.id.menuTextViewResult);
            tvLevelProgress = findViewById(R.id.menuTextViewProgress);
            tvLevelSigns = findViewById(R.id.menuTextViewSigns);
            tvAchieveTitle = findViewById(R.id.menuAchieveTextViewTitle);
            tvAchieveCaption = findViewById(R.id.menuAchieveTextViewCaption);
            btnHardmode = findViewById(R.id.menuButtonHardmode);
            btnAchieve = findViewById(R.id.menuButtonAchieve);
            btnStart = findViewById(R.id.menuButtonStart);
            btnPageLeft = findViewById(R.id.menuButtonPageLeft);
            btnPageRight = findViewById(R.id.menuButtonPageRight);
            imagePanel = findViewById(R.id.menuImagePanel);
            imageAchieveStarLeft = findViewById(R.id.menuAchieveImageStarLeft);
            imageAchieveStarRight = findViewById(R.id.menuAchieveImageStarRight);
            id_button_choosen = -1;
            id_mode = 0;
            id_page = 0;
            state_godmode = 0;

            // Размер экрана
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            BUTTON_HEIGHT = (size.x - 40) / 7;

            // Images
			int[] id_signs = new int[] { R.id.menuImageViewSign1, R.id.menuImageViewSign2, 
				R.id.menuImageViewSign3, R.id.menuImageViewSign4, R.id.menuImageViewSign5 };
			images_sign = new ImageView[id_signs.length];
            images_star = new ImageView[] { findViewById(R.id.menuImageViewStar1), findViewById(R.id.menuImageViewStar2) };
            for (int i = 0; i < images_sign.length; i++)
				images_sign[i] = findViewById(id_signs[i]);

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
					int level_id = list_buttons.length * id_page + id_button_choosen;
                    startActivity(new Intent(MenuActivity.this, MainActivity.class)
                            .putExtra("level_id", level_id).putExtra("mode", id_mode).putExtra("hardmode", is_hardmode_level));
                }
            });

            btnPageLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetPage(0, true);
                }
            });

            btnPageRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetPage(1, true);
                }
            });

            btnHardmode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetHardmode(!is_hardmode_level);
                }
            });

            btnAchieve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAchieve(!is_achieve);
                }
            });

            // Создание кнопок
            int k = 0;
            list_buttons = new Button[35];
            for (int i = 0; i < 5; i++) {
                LinearLayout layout = GetLayoutParamsRow();
                for (int j = 0; j < 7; j++) {
                    Button btn = new Button(this);
                    btn.setTag(k);
                    btn.setTextSize(16);
                    btn.setBackgroundColor(Color.WHITE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Button btn = (Button) v;
								int old_id_button_choosen = id_button_choosen;
                                id_button_choosen = (int)btn.getTag();
                                if (old_id_button_choosen != -1)
									UpdateButtonColor(old_id_button_choosen);
                                UpdateButtonColor(id_button_choosen);

                                if (is_achieve)
                                {
                                    UpdateAchieveInfo(id_button_choosen);
                                }
                                else
                                {
                                    int level_id = list_buttons.length * id_page + id_button_choosen;
                                    if (    state_godmode == 0 && level_id + 1 == 17 ||
                                            state_godmode == 1 && level_id + 1 ==  7 ||
                                            state_godmode == 2 && level_id + 1 == 10 ||
                                            state_godmode == 3 && level_id + 1 ==  1 ||
                                            state_godmode == 4 && level_id + 1 ==  3 ||
                                            state_godmode == 5 && level_id + 1 == 42) {
                                        state_godmode++;
                                        if (state_godmode == 6) {
                                            StarController.SetGodmode();
                                            SetMessage("GODMODE IS ACTIVATED!");
                                            /*
                                            SharedPreferences.Editor ed_prog = sPrefProgress.edit();
                                            ed_prog.putInt("COUNT_STARS", 9999);
                                            ed_prog.commit();
                                            */
                                        }
                                    }
                                    UpdateLevelInfo();
                                }
                            } catch (Exception e) {
                                SetMessage("BtnClick", e);
                            }
                        }
                    });
                    list_buttons[k++] = btn;
                    layout.addView(btn, GetLayoutParamsButton());
                }
                grid.addView(layout);
            }

            //AchieveController.Load(getSharedPreferences("achievements", MODE_PRIVATE));
        } catch (Exception e) {
            SetMessage("Title", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StarController.Initialize(sPrefLevels);
        max_level = StarController.GetMaxLevel();
        if (StarController.count_stars < 10) {
            imagePanel.setImageResource(R.drawable.menu_panel);
            images_star[1].setVisibility(View.INVISIBLE);
        }
        if (!is_achieve) SetPage(id_page, false);
    }

    void SetPage(int value, boolean is_animate) {
        id_page = value;
        UpdateLevelInfo();
        UpdateTitleAndStuffs();

        if (is_animate)
			SetPage_Thread(value, 0);
		else {
			int count_levels = LevelController.GetLevelpack(id_mode).length;
			for (int i = 0; i < list_buttons.length; i++) {
				int id_level = i + id_page * list_buttons.length;
				list_buttons[i].setVisibility(id_level < count_levels ? View.VISIBLE : View.INVISIBLE);
				list_buttons[i].setText(is_hardmode_level && StarController.GetCountCompletedLevelsOnDifficulty(id_level) < 4 
				                     || id_level >= max_level 
                     				 ? "X" : String.valueOf(id_level + 1));
                UpdateButtonColor(i);
			}	
		}
    }

    void SetPage_Thread(final int value, final int index) {
        int count_levels = LevelController.GetLevelpack(id_mode).length;
        if (index != 0) {
            for (int i = index - 1; i < list_buttons.length; i += 6) {
                int id_level = i + value * list_buttons.length;
                list_buttons[i].setVisibility(id_level < count_levels ? View.VISIBLE : View.INVISIBLE);
                list_buttons[i].setText(is_hardmode_level && StarController.GetCountCompletedLevelsOnDifficulty(id_level) < 4
                        || id_level >= max_level
                        ? "X" : String.valueOf(id_level + 1));
                UpdateButtonColor(i);
            }
        }

        if (index != 7) {
            for (int i = index; i < list_buttons.length; i += 6)
                list_buttons[i].setText("");

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SetPage_Thread(value, index + 1);
                }
            }, 60);
        }
    }

    void SetHardmode(boolean value) {
        is_hardmode_level = value;
        UpdateTitleAndStuffs();

        // TextViews
        int clr = is_hardmode_level ? Color.WHITE : Color.DKGRAY;
        tvLevelDifficulty.setTextColor(clr);
        tvLevelCurrent.setTextColor(clr);
        tvLevelResult.setTextColor(clr);
        tvLevelProgress.setTextColor(clr);
        tvLevelSigns.setTextColor(clr);
        btnPageLeft.setTextColor(clr);
        btnPageRight.setTextColor(clr);

        //Drawable btnHardmodeDrawable = btnHardmode.getDrawable();
        //int sign = is_hardmode_level ? -1 : 1;
        //final float[] FILTER = {
        //        sign, 0, 0, 0, 255, // red
        //        0, sign, 0, 0, 255, // green
        //        0, 0, sign, 0, 255, // blue
        //        0, 0, 0,    1,   0  // alpha
        //};
        //btnHardmodeDrawable.setColorFilter(new ColorMatrixColorFilter(FILTER));

        // Buttons
        for (int i = 0; i < list_buttons.length; i++) {
            final Button btn = list_buttons[i];
            final int level_id = i + id_page * list_buttons.length;
            final boolean is_black = is_hardmode_level ^ (i == id_button_choosen);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    UpdateButtonColor(level_id % list_buttons.length);
                    btn.setText(is_hardmode_level && StarController.GetCountCompletedLevelsOnDifficulty(level_id) < 4
                            || level_id >= max_level
                            ? "X" : String.valueOf(level_id + 1));
                }
            }, 42 + rand.nextInt(100));
        }
        UpdateLevelInfo();
    }

    void SetAchieve(boolean value) {
        try {
            is_achieve = value;
            gridAchieveInfo.setVisibility(is_achieve ? View.VISIBLE : View.INVISIBLE);
            gridLevelInfo.setVisibility(is_achieve ? View.INVISIBLE : View.VISIBLE);
            btnStart.setVisibility(is_achieve ? View.INVISIBLE : View.VISIBLE);
            UpdateTitleAndStuffs();
        } catch (Exception e) {
            SetMessage("SetAchieve01", e);
        }

        try {
            if (is_achieve)
            {
                list_achieve_unlocked = AchieveController.GetListUnlocked();

                int count_achieves = list_achieve_unlocked.length;
                for (int i = 0; i < list_buttons.length; i++) {
                    Button b = list_buttons[i];
                    if (i < count_achieves) {
                        b.setVisibility(View.VISIBLE);
                        b.setText("");
                        UpdateButtonColor(i);
                    }
                    else {
                        b.setVisibility(View.INVISIBLE);
                    }
                }
                if (id_button_choosen < 0 || id_button_choosen > 29) {
                    id_button_choosen = 0;
                    UpdateButtonColor(id_button_choosen);
                }
                UpdateAchieveInfo(id_button_choosen);
            } else {
                SetPage(id_page, true);
				//if (!is_hardmode_level)
				//	list_buttons[achieve_id].setTextColor(Color.BLACK);

            }
        } catch (Exception e) {
            SetMessage("SetAchieve01", e);
        }
    }

    void UpdateLevelInfo() {
        int count_levels = LevelController.GetLevelpack(id_mode).length;
		int level_id = list_buttons.length * id_page + id_button_choosen;
        if (level_id < 0) return;

        String message_lock = "";
        if (level_id >= count_levels)
            message_lock = "Play!";
        else if (level_id >= max_level)
            message_lock = StarController.GetCountNeededStars(level_id) + " stars needed";
        else if (is_hardmode_level && StarController.GetCountCompletedLevelsOnDifficulty(level_id) < 4)
            message_lock = "Complete 4 levels in a row";
        if (message_lock != "" && !StarController.is_godmode) {
            btnStart.setEnabled(false);
            btnStart.setText(message_lock);
            gridLevelInfo.setVisibility(View.INVISIBLE);
            return;
        }

        btnStart.setEnabled(true);
        btnStart.setText("PLAY!");
        gridLevelInfo.setVisibility(View.VISIBLE);
        imagePanel.setVisibility(View.INVISIBLE);
        try {
            boolean is_overflow_level = false;
            int value_current = 0, stack_size = 0;
            int[] result_mass = new int[0];
            String[] signs = new String[0];
            for (String code : LevelController.GetLevelpack(id_mode)[level_id]) {
                try {
                    if (code.charAt(0) == 'N') {
                        if (is_hardmode_level) continue;
                        code = code.substring(1);
                    }
                    if (code.charAt(0) == 'H') {
                        if (!is_hardmode_level) continue;
                        code = code.substring(1);
                    }

                    String s = code.substring(1);
                    if (code == "Overflow")
                        is_overflow_level = !is_overflow_level;
                    else if (code != "Binary" && !code.startsWith("StackSet") && !code.startsWith("Sorcerer") && !code.startsWith("Repeat"))
                        switch (code.charAt(0)) {
                            case 'S': case 'D': case 'B':case 'A':case 'А': break;
                            case 'C': // Start number
                            case 'С': value_current = Integer.parseInt(s); break;
                            case 'R': // Result number
                            case 'H': // Result number (hardmode only)
                                String[] mass = s.split("_");
                                result_mass = new int[mass.length];
                                for (int i = 0; i < mass.length; i++)
                                    result_mass[i] = Integer.parseInt(mass[i]);
                                break;
                            case 'K': stack_size = Integer.parseInt(s); break;
                            case 'L': signs = s.split("_"); break;
                            case 'P':   // Chances (probability)
                                String[] chances_mass = s.split("_");
                                signs = new String[chances_mass.length / 2];
                                for (int i = 0; i < chances_mass.length; i += 2)
                                    signs[i/2] = chances_mass[i];
                                break;
                            default:
                                int k = 0;
                                signs = new String[code.length()];
                                for (char sign : code.toCharArray())
                                    signs[k++] = String.valueOf(sign);
                                break;
                        }
                } catch (Exception e) {
                    tvError.setText(tvError.getText() + " [Code:" + e.getMessage());
                }
            }

            String result = "";
            if (result_mass.length > 8) {
                for (int i = 0; i < 3; i++)
                    result += " -> " + result_mass[i];
                result += " -> ... -> " + result_mass[result_mass.length - 1];
            } else
                for (int i = 0; i < result_mass.length; i++)
                    result += " -> " + result_mass[i];

            tvLevelDifficulty.setText("Difficulty: " + list_difficulties[level_id / 7]);
            tvLevelCurrent.setText("Start: " + value_current);
            tvLevelResult.setText("Result: " + (is_overflow_level ? "more than " : "") + result.substring(4));

            int count_stars = StarController.GetCountStars(id_mode, level_id);
            images_star[0].setImageResource(count_stars > 0 ? R.drawable.menu_star : R.drawable.menu_star_empty);
            images_star[1].setImageResource(count_stars > 1 ? R.drawable.menu_star : R.drawable.menu_star_empty);

            for (int i = signs.length; i < images_sign.length; i++)
                images_sign[i].setImageResource(0);
            for (int i = 0; i < signs.length; i++)
                images_sign[i].setImageResource(SignController.GetSign(signs[i]).id_image);

        } catch (Exception e) {
            SetMessage("UpdateLevelInfo", e);
        }
    }

    void UpdateAchieveInfo(int id_achieve) {
        if (id_achieve < 0) return;
        btnStart.setVisibility(View.INVISIBLE);
        UpdateTitleAndStuffs();

        try {
            Achieve a = AchieveController.GetAchieveOnID(id_achieve);
            if (list_achieve_unlocked[id_achieve]) {
                tvAchieveTitle.setText(a.GetTitle());
                tvAchieveCaption.setText(a.GetLabelAfter());
                imageAchieveStarLeft.setVisibility(View.VISIBLE);
                imageAchieveStarRight.setVisibility(View.VISIBLE);
            } else {
                tvAchieveTitle.setText("???");
                tvAchieveCaption.setText(a.GetLabelBefore());
                imageAchieveStarLeft.setVisibility(View.INVISIBLE);
                imageAchieveStarRight.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            SetMessage("UpdateAchieveInfo02", e);
        }
    }

    void UpdateTitleAndStuffs() {
        tvTitle.setText(is_achieve ? "ACHIEVEMENTS" : is_hardmode_level ? "HARDMODE" : "STANDART MODE");
        tvTitle.setTextColor(is_achieve ? getResources().getColor(R.color.menu_button_achieve_noo) :
                is_hardmode_level ? Color.parseColor("#CC0000") : Color.BLACK);
        gridMain.setBackgroundColor(is_achieve || is_hardmode_level ?
                getResources().getColor(R.color.menu_backblack) :
                getResources().getColor(R.color.menu_backwhite));
        btnPageLeft.setVisibility(id_page == 0 || is_achieve ? View.INVISIBLE : View.VISIBLE);
        btnPageRight.setVisibility(id_page == 1 || is_achieve ? View.INVISIBLE : View.VISIBLE);
        btnHardmode.setVisibility(StarController.count_stars < 10 || is_achieve ? View.INVISIBLE : View.VISIBLE);
        imagePanel.setVisibility(gridLevelInfo.getVisibility() == View.VISIBLE || is_achieve || is_hardmode_level ? View.INVISIBLE : View.VISIBLE);

        // Hardmode Icon
        btnHardmode.setImageResource(is_hardmode_level ?
                R.drawable.menu_hardmode_inv :
                R.drawable.menu_hardmode);
        btnAchieve.setImageResource(is_achieve || is_hardmode_level ?
                R.drawable.menu_achieve_inv :
                R.drawable.menu_achieve);
    }

    void UpdateButtonColor(int id_button) {
        Button btn = list_buttons[id_button];
        if (btn.getVisibility() == View.INVISIBLE) return;

        if (is_achieve)
        {
            int id_color;
                 if (id_button == id_button_choosen  ) id_color = R.drawable.menu_star_blue;
            else if (list_achieve_unlocked[id_button]) id_color = R.drawable.menu_star;
            else                                       id_color = R.drawable.menu_star_empty;
            btn.setBackgroundResource(id_color);
        }
        else
        {
            if (id_button == id_button_choosen) {
                btn.setTextColor(is_hardmode_level ? Color.BLACK : Color.WHITE);
                btn.setBackgroundColor(is_hardmode_level ? Color.WHITE : Color.BLACK);
            } else {
                btn.setTextColor(is_hardmode_level ? Color.WHITE : Color.BLACK);
                btn.setBackgroundColor(getResources().getColor(StarController.GetMenuButtonColor(id_mode,
                        id_button + list_buttons.length * id_page,
                        is_hardmode_level)));
            }
        }
    }



    LinearLayout GetLayoutParamsRow() {
        LinearLayout rez = new LinearLayout(this);
        rez.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rez.setOrientation(LinearLayout.HORIZONTAL);
        return rez;
    }

    LinearLayout.LayoutParams GetLayoutParamsButton() {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 0, 0, 0);
        lparams.weight = 1;
        lparams.width = 0;
        lparams.height = BUTTON_HEIGHT;
        lparams.topMargin = lparams.bottomMargin = lparams.leftMargin = lparams.rightMargin = 2;
        return lparams;
    }

    void SetMessage(String title, Exception e) {
        tvError.setText(tvError.getText() + " [" + title + ":" + e.getMessage());
    }

    void SetMessage(String message) {
        tvError.setText(tvError.getText() + " " + message);
    }
}
