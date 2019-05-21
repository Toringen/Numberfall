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
    LinearLayout gridLayout;
    GridLayout gridLevelInfo;
    TextView tvTitle, tvError, tvDifficulty, tvCurrent, tvResult, tvProgress, tvSigns;
    Button[] list_buttons;
    ImageView[] images_star, images_sign;
    ImageButton btnHardmode;
    Button btnStart, btnPageLeft, btnPageRight;
    boolean is_hardmode_level;
    int level_id, count_stars, max_level;
    int id_mode, id_page, state_godmode;
    String[] list_difficulties = new String[] { "EASY", "NORMAL", "HARD", "LUNATIC", "EXTRA", "177013",
            "PRINCE", "THE DOUBLE", "REVOLUTIONARY", "KING" };

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
            sPrefLevels = getSharedPreferences(getString(R.string.PREF_FILE_LEVELS), MODE_PRIVATE);
            sPrefProgress = getSharedPreferences(getString(R.string.PREF_FILE_PROGRESS), MODE_PRIVATE);
            gridMain = findViewById(R.id.menuGridMain);
            gridLayout = findViewById(R.id.menuGrid);
            gridLevelInfo = findViewById(R.id.menuLayoutLevelInfo);
            tvTitle = findViewById(R.id.menuTextViewTitle);
            tvError = findViewById(R.id.menuTextViewError);
            tvDifficulty = findViewById(R.id.menuTextViewDiff);
            tvCurrent = findViewById(R.id.menuTextViewCurrent);
            tvResult = findViewById(R.id.menuTextViewResult);
            tvProgress = findViewById(R.id.menuTextViewProgress);
            tvSigns = findViewById(R.id.menuTextViewSigns);
            btnHardmode = findViewById(R.id.menuButtonHardmode);
            btnStart = findViewById(R.id.menuButtonStart);
            btnPageLeft = findViewById(R.id.menuButtonPageLeft);
            btnPageRight = findViewById(R.id.menuButtonPageRight);
            list_buttons = new Button[42];
            level_id = -1;
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

            // Создание кнопок
            int k = 0;
            for (int i = 0; i < 6; i++) {
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
                                int color_base = is_hardmode_level ? Color.WHITE : Color.BLACK;
                                int color_inv  = is_hardmode_level ? Color.BLACK : Color.WHITE;
                                if (level_id != -1) {
                                    Button btn = list_buttons[level_id % list_buttons.length];
                                    btn.setBackgroundColor(color_inv);
                                    btn.setTextColor(color_base);
                                }

                                Button btn = (Button) v;
                                btn.setBackgroundColor(color_base);
                                btn.setTextColor(color_inv);

                                level_id = list_buttons.length * id_page + (int)btn.getTag();
                                if (    state_godmode == 0 && level_id + 1 == 17 ||
                                        state_godmode == 1 && level_id + 1 ==  7 ||
                                        state_godmode == 2 && level_id + 1 == 10 ||
                                        state_godmode == 3 && level_id + 1 ==  1 ||
                                        state_godmode == 4 && level_id + 1 ==  3 ||
                                        state_godmode == 5 && level_id + 1 == 42) {
                                    state_godmode++;
                                    if (state_godmode == 6) {
                                        SetMessage("GODMODE IS ACTIVATED!");
                                        SharedPreferences.Editor ed_prog = sPrefProgress.edit();
                                        ed_prog.putInt("COUNT_STARS", 9999);
                                        ed_prog.commit();
                                    }
                                }

                                UpdateInfo(level_id);

                            } catch (Exception e) {
                                SetMessage("BtnClick", e);
                            }
                        }
                    });
                    list_buttons[k++] = btn;
                    layout.addView(btn, GetLayoutParamsButton());
                }
                gridLayout.addView(layout);
            }

        } catch (Exception e) {
            SetMessage("Title", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        count_stars = sPrefProgress.getInt("COUNT_STARS", 0);
        max_level = StarController.GetMaxLevel(count_stars);
        if (count_stars < 10) {
            btnHardmode.setVisibility(View.INVISIBLE);
            images_star[1].setVisibility(View.INVISIBLE);
        }
        SetPage(id_page, false);
        //for (int i = 0; i < list_buttons.length; i++)
        //    list_buttons[i].setText(i < max_level ? String.valueOf(i + 1) : "X");
    }

    void SetPage(int value, boolean is_animate) {
        id_page = value;
        btnPageLeft .setVisibility(id_page != 0 ? View.VISIBLE : View.INVISIBLE);
        btnPageRight.setVisibility(id_page != 1 ? View.VISIBLE : View.INVISIBLE);
        level_id = list_buttons.length * id_page + level_id % list_buttons.length;
        UpdateInfo(level_id);

		if (is_animate) 
			SetPage_Thread(value, 0);
		else {
			int count_levels = LevelController.GetLevelpack(id_mode).length;
			for (int i = 0; i < list_buttons.length; i++) {
				int id_level = i + id_page * list_buttons.length;
				list_buttons[i].setVisibility(id_level < count_levels ? View.VISIBLE : View.INVISIBLE);
				list_buttons[i].setText(id_level < max_level ? String.valueOf(id_level + 1) : "X");
			}	
		}
    }

    void SetPage_Thread(final int value, final int index) {
        int count_levels = LevelController.GetLevelpack(id_mode).length;
        if (index != 0) {
            for (int i = index - 1; i < list_buttons.length; i += 6) {
                int id_level = i + value * list_buttons.length;
                list_buttons[i].setVisibility(id_level < count_levels ? View.VISIBLE : View.INVISIBLE);
                list_buttons[i].setText(id_level < max_level ? String.valueOf(id_level + 1) : "X");
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
        tvTitle.setText(is_hardmode_level ? "HARDMODE" : "STANDART MODE");
        tvTitle.setTextColor(is_hardmode_level ? Color.parseColor("#CC0000") : Color.BLACK);
        gridMain.setBackgroundColor(is_hardmode_level ?
                getResources().getColor(R.color.menu_backblack) :
                getResources().getColor(R.color.menu_backwhite));

        // TextViews
        int clr = is_hardmode_level ? Color.WHITE : Color.DKGRAY;
        tvDifficulty.setTextColor(clr);
        tvCurrent.setTextColor(clr);
        tvResult.setTextColor(clr);
        tvProgress.setTextColor(clr);
        tvSigns.setTextColor(clr);

        // Hardmode Icon
        btnHardmode.setImageResource(is_hardmode_level ?
                R.drawable.menu_hardmode_inv :
                R.drawable.menu_hardmode);
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
            final boolean is_inverted = (i == level_id % 42);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (is_hardmode_level ^ is_inverted) {
                        btn.setTextColor(Color.WHITE);
                        btn.setBackgroundColor(Color.BLACK);
                    } else {
                        btn.setTextColor(Color.BLACK);
                        btn.setBackgroundColor(Color.WHITE);
                    }
                }
            }, 42 + rand.nextInt(100));

        }
        UpdateInfo(level_id);
    }

    void UpdateInfo(int id_level) {
        int count_levels = LevelController.GetLevelpack(id_mode).length;
        if (id_level < 0) return;
        if (id_level >= count_levels)
        {
            btnStart.setEnabled(false);
            btnStart.setText("Play!");
            gridLevelInfo.setVisibility(View.INVISIBLE);
            return;
        }
        if (id_level >= max_level)
        {
            int stars_need = StarController.GetCountNeededStars(level_id, count_stars);
            btnStart.setEnabled(false);
            btnStart.setText(stars_need + " stars needed");
            gridLevelInfo.setVisibility(View.INVISIBLE);
            return;
        }

        btnStart.setEnabled(true);
        btnStart.setText("PLAY!");
        gridLevelInfo.setVisibility(View.VISIBLE);
        try {
            boolean is_overflow_level = false;
            int value_current = 0, stack_size = 0;
            int[] result_mass = new int[0];
            String[] signs = new String[0];
            for (String code : LevelController.GetLevelpack(id_mode)[level_id]) {
                try {
                    if (code.charAt(0) == 'H') {
                        if (!is_hardmode_level) continue;
                        code = code.substring(1);
                    }

                    String s = code.substring(1);
                    if (code == "Overflow")
                        is_overflow_level = !is_overflow_level;
                    else if (code != "Binary" && !code.startsWith("StackSet"))
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
            for (int i = 0; i < result_mass.length; i++)
                result += " -> " + result_mass[i];

            tvDifficulty.setText("Difficulty: " + list_difficulties[level_id / 7]);
            tvCurrent.setText("Start: " + value_current);
            tvResult.setText("Result: " + (is_overflow_level ? "more than " : "") + result.substring(4));

            images_star[0].setImageResource(sPrefLevels.getBoolean("M" + id_mode + "L" + level_id, false)
                    ? R.drawable.menu_star : R.drawable.menu_star_empty);
            images_star[1].setImageResource(sPrefLevels.getBoolean("M" + id_mode + "H" + level_id, false)
                    ? R.drawable.menu_star : R.drawable.menu_star_empty);

            for (int i = signs.length; i < images_sign.length; i++)
                images_sign[i].setImageResource(0);
            for (int i = 0; i < signs.length; i++)
                images_sign[i].setImageResource(SignController.GetSign(signs[i]).id_image);

        } catch (Exception e) {
            SetMessage("UpdateInfo", e);
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
