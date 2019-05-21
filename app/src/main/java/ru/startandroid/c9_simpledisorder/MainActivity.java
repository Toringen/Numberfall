package ru.startandroid.c9_simpledisorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPrefLevels, sPrefProgress;
    ConstraintLayout layoutLink;
    LinearLayout grid;
    TextView tvLLabel, tvGoal, tvStep, tvError, tvLink;
    ImageView imageBackground;
    ImageView[] images_sign;
    Button[][] buttons;
    String[][] levels;
    int SCREEN_WIDTH, SCREEN_HEIGHT;

    Random rand = new Random();
    String markers = "";
    int size, level_id, result_id, current, current_start;
    int count_stars, id_mode;
    int[] result_mass;
    List<Integer> list_empty_numbers = new ArrayList<Integer>();
    float prob_button_empty;

    boolean is_overflow_level = false;
    boolean is_binary_level = false;
    boolean is_hardmode_level = false;

    // Play (Up) buttons
    Button[] play_buttons;
    int play_id_button, play_id_sign;
    String sign;

    // Stack buttons
    Button[] stack_buttons;
    String[] stack_default;
    MediaPlayer stack_player;
    int stack_current = 0, stack_size = 0;
    boolean is_stack_button = false;
    int click_listener = 0;

    double[] sign_chances = { 0, 0, 0, 0, 0,  0, 0, 0, 0, 0, 0,  0, 0, 0, 0, 0 };
    MediaPlayer[] players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sPrefLevels = getSharedPreferences(getString(R.string.PREF_FILE_LEVELS), MODE_PRIVATE);
        sPrefProgress = getSharedPreferences(getString(R.string.PREF_FILE_PROGRESS), MODE_PRIVATE);
        grid = (LinearLayout) findViewById(R.id.layoutGrid);
        tvLLabel = (TextView) findViewById(R.id.textViewResult);
        tvGoal = (TextView) findViewById(R.id.textViewCurrent);
        tvStep = (TextView) findViewById(R.id.textViewStep);
        tvError = (TextView) findViewById(R.id.textViewError);
        tvLink = (TextView) findViewById(R.id.textViewLink);
        layoutLink = findViewById(R.id.layoutLink);
        imageBackground = (ImageView) findViewById(R.id.imageViewBackground);
        Button btnPlay1 = (Button) findViewById(R.id.btnImageLeft);
        Button btnPlay2 = (Button) findViewById(R.id.btnImageRight);
        Button btnStack1 = (Button) findViewById(R.id.btnStack1);
        Button btnStack2 = (Button) findViewById(R.id.btnStack2);
        Button btnStack3 = (Button) findViewById(R.id.btnStack3);
        Button btnStack4 = (Button) findViewById(R.id.btnStack4);
        Button btnStack5 = (Button) findViewById(R.id.btnStack5);

        // Размер экрана
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_WIDTH = size.x;
        SCREEN_HEIGHT = size.y;

        // Levels
        Intent intent = getIntent();
        level_id = intent.getIntExtra("level_id", 0);
        id_mode = intent.getIntExtra("mode", 0);
        is_hardmode_level = intent.getBooleanExtra("hardmode", false);
        count_stars = sPrefProgress.getInt("COUNT_STARS", 0);
        levels = LevelController.GetLevelpack(id_mode);

        // Animation
        int anim_sign = -1;
        int anim_counter = 1;
        View[] views_animation = { tvLLabel, tvGoal, btnPlay1, btnPlay2, tvStep };
        for (View view : views_animation) {
            TranslateAnimation transAnimation = new TranslateAnimation(200 * anim_sign, 0, 0, 0);
            transAnimation.setDuration(300 * anim_counter);
            view.setAnimation(transAnimation);
            anim_sign *= -1;
            anim_counter++;
        }

        // Sounds
        try {
            stack_player = MediaPlayer.create(MainActivity.this, R.raw.move);
            players = SignController.GetListPlayers(MainActivity.this);
        }
        catch (Exception e)
        {
            SetMarker("Sounds:");
            SetMessage(e.getMessage());
        }

        // Play buttons
        play_buttons = new Button[] { btnPlay1, btnPlay2 };
        for (Button btn : play_buttons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                try {
                    // TODO Auto-generated method stub
                    click_listener++;
                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            click_listener = 0;
                        }
                    };

                    Button btn = (Button) v;
                    int btn_id = Arrays.asList(play_buttons).indexOf(btn);
                    String btn_sign = btn.getTag().toString();
                    is_stack_button = false;

                    if (click_listener == 1)
                    {
                        // Single click
                        handler.postDelayed(r, 250);
                        sign = btn_sign;
                        tvStep.setText(GetTextStepLabel());
                        play_id_button = btn_id;
                        play_id_sign = SignController.GetSignIndex(sign);
                        PlayButtonsEnable();
                    }
                    else if (click_listener >= 2)
                    {
                        // Double click
                        click_listener = 0;
                        if (stack_current >= stack_size) return;

                        PlayButtonChange(btn_id);                      // Clear click button
                        StackSetSign(stack_current, btn_sign);    // Change stack button
                        PlayButtonsDisable();
                        PlaySound(stack_player);
                        tvStep.setText("");
                        stack_current++;
                    }
                }
                catch (Exception e) {
                    Button btn = (Button) v;
                    CharSequence tag = btn.getTag().toString();
                    SetMessage("TAG: " + tag + "; Err:" + e.getMessage());
                }
                }
            });
        }

        // Stack buttons
        stack_buttons = new Button[] { btnStack1, btnStack2, btnStack3, btnStack4, btnStack5 };
        stack_default = new String[] { "", "", "", "", "" };
        for (Button btn : stack_buttons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Button btn = (Button) v;
                        String btn_sign = btn.getTag().toString();
                        if (btn_sign == "" || btn_sign == " ")
                            return;

                        // click
                        int btn_id = Arrays.asList(stack_buttons).indexOf(btn);
                        sign = btn_sign;
                        play_id_button = btn_id;
                        is_stack_button = true;
                        tvStep.setText(GetTextStepLabel());
                        PlayButtonsEnable();
                    }
                    catch (Exception e) {
                        Button btn = (Button) v;
                        CharSequence tag = btn.getTag().toString();
                        SetMessage("TAG: " + tag + "; Err:" + e.getMessage());
                    }
                }
            });
        }

        // Operation buttons
        int[] id_signs = new int[] { R.id.imageViewSign1, R.id.imageViewSign2,
                R.id.imageViewSign3, R.id.imageViewSign4, R.id.imageViewSign5 };
        images_sign = new ImageView[id_signs.length];
        for (int i = 0; i < images_sign.length; i++)
            images_sign[i] = findViewById(id_signs[i]);

        // Link
        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLink.setVisibility(View.INVISIBLE);
            }
        });
        layoutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutLink.setVisibility(View.INVISIBLE);
            }
        });

        Button btnClear = (Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });

        Button btnExit = (Button) findViewById(R.id.buttonExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SetSize(size + 1);
                MainActivity.this.finish();
            }
        });

        SetSize(4);
        LoadLevel(level_id);
    }

    void SetSize(int value) {
        size = value;
        int[] btn_sizes = { 13, 12, 10, 8, 8, 8, 8, 8 };

        grid.removeAllViews();
        buttons = new Button[size][size];
        for (int i = 0; i < size; i++)
        {
            LinearLayout horiz = GetHorizLayout();
            for (int j = 0; j < size; j++)
            {
                Button btn = new Button(this);
                btn.setLayoutParams(GetButtonParams((SCREEN_WIDTH - 50 - 10 * size) / size, 0));
                if (size >= 8) {
                    btn.setLayoutParams(GetButtonParams(560 / size, 0));
                    btn.setTextSize(btn_sizes[size - 8]);
                    if (j + size * i >= 100) btn.setTextSize(6);
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (sign == "")
                                return;

                            Button b = (Button) v;
                            String text = String.valueOf(b.getText());
                            int value = Integer.parseInt(text);
                            String step_label = GetTextStepLabelAfter(value);
                            boolean is_delete_button = true;

                            // Update step
                            switch (sign) {
                                case "+": current += value; break;
                                case "-": current -= value; break;
                                case "*": current *= value; break;
                                case "/": current /= value; break;
                                case "^2": current *= current; break;

                                case "+2*": current += 2 * value; break;
                                case "+10*": current += 10 * value; break;
                                case "-3*": current -= 3 * value; break;
                                case "G": is_delete_button = false; Gemini(text); break;
                                case "C": is_delete_button = false; Cancer(b, value); break;
                                case "L": is_delete_button = false; Libra(b, value); break;
                                case "R": int temp = result_mass[result_id]; result_mass[result_id] = current; current = temp; break;

                                case "&": current &= value; break;
                                case "^": current |= value; break;
                                case "⊕": current ^= value; break;
                                case "<<": current *= 2; break;
                                case ">>": current /= 2; break;
                                case "-1": current--; break;
                                case "+1": current++; break;
                            }

                            // Just Monika
                            //if (result_mass[result_id] - current == 1)
                            //    JustMonika();

                            // Update value
                            if (is_delete_button) {
                                b.setEnabled(false);
                                b.setText("");
                            }
                            if (is_delete_button)
                                step_label += "" + GetString(current);
                            tvStep.setText(step_label);
                            tvGoal.setText(GetTextGoalLabel());
                            if (!is_stack_button)
                                PlayButtonChange(play_id_button);
                            else
                                StackRemove(play_id_button);

                            // Sound
                            PlaySound(players[play_id_sign]);

                            // Clear
                            PlayButtonsDisable();
                            SelectionClear();

                            // New game
                            if (isEndLevel()) {
                                int add_stars = 0;

                                SharedPreferences.Editor ed = sPrefLevels.edit();
                                if (!sPrefLevels.getBoolean("M" + id_mode + "L" + level_id, false)) {
                                    ed.putBoolean("M" + id_mode + "L" + level_id, true);
                                    add_stars++;
                                }
                                if (!sPrefLevels.getBoolean("M" + id_mode + "H" + level_id, false) && is_hardmode_level) {
                                    ed.putBoolean("M" + id_mode + "H" + level_id, true);
                                    add_stars++;
                                }
                                if (add_stars > 0) {
                                    SharedPreferences.Editor ed_prog = sPrefProgress.edit();
                                    ed_prog.putInt("COUNT_STARS", count_stars + add_stars);
                                    ed_prog.commit();
                                }
                                count_stars += add_stars;
                                ed.commit();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoadLevel(level_id + 1);
                                    }
                                }, 500);
                            }
                        }
                        catch (Exception e){
                            SetMessage(e.getMessage());
                        }
                    }
                });

                horiz.addView(btn);
                buttons[i][j] = btn;
            }

            grid.addView(horiz);
        }
    }

    void LoadLevel(int value) {
        if (value >= levels.length || StarController.GetCountNeededStars(value, count_stars) > 0)
            MainActivity.this.finish();

        try {
            level_id = value;
            is_overflow_level = false;
            is_binary_level = false;
            prob_button_empty = is_hardmode_level ? 0.35f : 0.05f;
            stack_current = 0;
            current_start = 0;
            for (int i = 0; i < sign_chances.length; i++)
                sign_chances[i] = 0;
            for (int i = 0; i < stack_default.length; i++)
                stack_default[i] = "";
            layoutLink.setVisibility(View.INVISIBLE);
            ClearMarker();

            for (String code : levels[value]) {
                SetMessage(code + " ");
                try {
                    if (code.charAt(0) == 'H') {
                        if (!is_hardmode_level) continue;
                        prob_button_empty = 0.07f;
                        code = code.substring(1);
                    }

                    String v = code.substring(1);
                    if (code == "Binary")
                        is_binary_level = true;
                    else if (code == "Overflow")
                        is_overflow_level = !is_overflow_level;
                    else if (code.startsWith("StackSet"))
                        stack_default[stack_current++] = code.substring(8);
                    else
                        switch (code.charAt(0)) {
                            case 'A':
                            case 'А':   // link
                                SetMessage("[Label1]");
                                if (!sPrefLevels.getBoolean("M" + id_mode + "L" + level_id, false)) {
                                    SetMessage("[Label2]");
                                    tvLink.setText(v);
                                    layoutLink.setVisibility(View.VISIBLE);
                                }
                                break;
                            case 'S': // Size
                                SetSize(Integer.parseInt(v));
                                break;
                            case 'C': // Start number
                            case 'С':
                                current_start = Integer.parseInt(v);
                                break;
                            case 'R': // Result number (HR - hardmode only)
                                String[] r_mass = v.split("_");
                                result_mass = new int[r_mass.length];
                                for (int i = 0; i < r_mass.length; i++)
                                    result_mass[i] = Integer.parseInt(r_mass[i]);
                                break;
                            case 'D': // Result number (HR - hardmode only)
                                String[] d_mass = v.split("_");
                                for (int i = 0; i < d_mass.length; i++)
                                    list_empty_numbers.add(Integer.parseInt(d_mass[i]));
                                break;
                            case 'B':   // Probability of empty cell
                                prob_button_empty = Integer.parseInt(v) / 100f;
                                break;
                            case 'K': // StackSize
                                stack_size = Integer.parseInt(v);
                                break;
                            case 'L':
                                String[] sign_mass = v.split("_");
                                for (String sign : sign_mass)
                                {
                                    int index = SignController.GetSignIndex(sign);
                                    sign_chances[index] = SignController.list_signs[index].default_chance;
                                    //SignController.FillArrayChances(sign, sign_chances);
                                }
                                break;
                            case 'P':   // Chances (probability)
                                String[] chances_mass = v.split("_");
                                for (int i = 0; i < chances_mass.length; i += 2)
                                    sign_chances[SignController.GetSignIndex(chances_mass[i])] = Integer.parseInt(chances_mass[i + 1]) / 100f;
                                break;
                            default:
                                for (char ch_sign : code.toCharArray()) {
                                    SetMarker("[" + ch_sign + "]");
                                    //SignController.FillArrayChances(String.valueOf(ch_sign), sign_chances);
                                    int index = SignController.GetSignIndex(String.valueOf(ch_sign));
                                    sign_chances[index] = SignController.list_signs[index].default_chance;
                                }
                                break;
                        }
                } catch (Exception e) {
                    SetMessage(" [ReadCodeError (" + code + "): " + e.getMessage());
                }
            }

            // Сверхспособность: Беспредельное сумирование!
            int id_sign = 0;
            float sum = 0;
            for (int i = 0; i < sign_chances.length; i++)
            {
                if (sign_chances[i] != 0)
                    images_sign[id_sign++].setImageResource(SignController.list_signs[i].id_image);
                sum += sign_chances[i];
                sign_chances[i] = sum;
            }
            for (int i = 0; i < sign_chances.length; i++)
                sign_chances[i] /= sum;
            for (int i = id_sign; i < images_sign.length; i++)
                images_sign[i].setImageResource(0);


            //SetMessage("CHANCES: ");
            //for (int i = 0; i < sign_chances.length; i++)
            //    SetMarker(sign_chances[i] + " ");
            //SetMessage("");


            // Set result of level
            // Set background menace jojo reference
            if (is_overflow_level) {
                imageBackground.setVisibility(View.VISIBLE);
            }
            else {
                imageBackground.setVisibility(View.INVISIBLE);
            }

            // Clear level
            Clear();
        } catch (Exception e) {
            SetMessage(" LoadLevelError: " + e.getMessage());
        }
    }

    void Clear() {
        //stack_size = 0;
        current = current_start;
        result_id = 0;
        tvLLabel.setText("Level " + (level_id + 1));
        tvGoal.setTextColor(GetColorStepLabel());
        tvGoal.setText(GetTextGoalLabel());
        tvGoal.setTextSize(tvGoal.getText().length() < 18
                && result_mass[0] <= 99999
                && result_mass.length == 1
                && is_overflow_level ? 24 : 16);
        tvStep.setTextColor(GetColorGoalLabel());
        tvStep.setText("");
        SelectionClear();
        StackClear();

        if (tvGoal.getText().length() < 18
                && result_mass[0] <= 99999
                && result_mass.length == 1
                && !is_overflow_level) {
            tvGoal.setTextSize(24);
            ViewGroup.LayoutParams lp = tvStep.getLayoutParams();
            lp.height = 36;
            tvStep.setLayoutParams(lp);
        } else {
            tvGoal.setTextSize(16);
            ViewGroup.LayoutParams lp = tvStep.getLayoutParams();
            lp.height = 44;
            tvStep.setLayoutParams(lp);
        }

        int anim_sign = 1;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                int id = j + size * i + (is_binary_level ? 0 : 1);
                anim_sign *= -1;

                // Set text and enable
                Button btn = buttons[i][j];
                btn.setText(String.valueOf(id));
                btn.setEnabled(true);

                // Disable chance 5% (normal) or 35% (hardmode)
                if (rand.nextDouble() < prob_button_empty
                        || is_binary_level && result_mass[0] == id
                        || is_binary_level && result_mass[result_mass.length - 1] == id
                        || list_empty_numbers.contains(id)) {
                    btn.setEnabled(false);
                    btn.setText("");
                }

                // Animation
                TranslateAnimation transAnimation = new TranslateAnimation(200 * anim_sign, 0, 0, 0);
                transAnimation.setDuration(1500 + rand.nextInt(1500));
                btn.setAnimation(transAnimation);
            }
        }

        PlayButtonChange(0);
        PlayButtonChange(1);
        PlayButtonsDisable();
    }

    void PlayButtonChange(int id_button){
        int id_sign = 0;
        try {
            Button btn = play_buttons[id_button];
            double chance = rand.nextDouble();
            while (chance >= sign_chances[id_sign])
                id_sign++;

            // A заменяется на умножение, если это спец-уровень.
            // Видимо, А - это что-то читерское.
            if (SignController.list_signs[id_sign].sign.equals("A") && is_overflow_level)
                id_sign = 2;

            // Set sign
            Sign sign = SignController.list_signs[id_sign];
            btn.setBackgroundResource(sign.id_image);
            btn.setTag(sign.sign);
        }
        catch (Exception e) {
            SetMessage("SetSign(" + id_sign + "):" + e.getMessage());
        }
    }

    void PlayButtonsDisable() {
        for (Button[] btn_line : buttons)
            for (Button btn : btn_line)
                btn.setEnabled(false);
    }

    void PlayButtonsEnable() {
        for (Button[] btn_line : buttons)
            for (Button btn : btn_line)
                if (btn.getText() != "")
                    btn.setEnabled(true);
    }

    void SelectionClear() {
        play_id_button = -1;
        sign = " ";
    }

    void StackSetSign(int id_button, String sign){
        try {
            Button btn = stack_buttons[id_button];
            btn.setTag(sign);
            if (sign != "")
                btn.setBackgroundResource(SignController.GetSign(sign).id_image);
            else {
                if (id_button < stack_size)
                    btn.setBackgroundResource(R.drawable.s_empty);
                else
                    btn.setBackgroundResource(0);
            }
        }
        catch (Exception e) {
            SetMessage("SetSignStack:" + sign + ":" + e.getMessage());
        }
    }

    void StackRemove(int id_button) {
        try {
            for (int i = id_button; i < stack_buttons.length; i++) {
                //if (i == stack_size - 1 || i == stack_buttons.length - 1) {
                if (i == stack_buttons.length - 1) {
                    StackSetSign(i, "");
                    break;
                }

                Button next_stack = stack_buttons[i + 1];
                String tag = next_stack.getTag().toString();
                StackSetSign(i, tag);
                if (tag == "") break;
            }
            stack_current--;
        }
        catch (Exception e) {
            SetMessage("RemoveSignStack:" + e.getMessage());
        }
    }

    void StackClear() {
        try {
            stack_current = 0;
            for (int i = 0; i < stack_buttons.length; i++) {
                StackSetSign(i, stack_default[i]);
                if (stack_default[i] != "") stack_current++;
            }
        }
        catch (Exception e) {
            SetMessage("ClearStack:" + e.getMessage());
        }
    }

    boolean isEndLevel(){
        if (is_overflow_level)
            return current >= result_mass[0];

        if (current == result_mass[result_id])
            result_id++;
        return result_id >= result_mass.length;
    }

    void Gemini(String text) {
        int counter = 30;
        for (Button[] btn_line : buttons)
            for (Button btn_empty : btn_line) {
                if (btn_empty.getText() == "" && (--counter >= 0 || rand.nextDouble() < 0.6)) {
                    btn_empty.setText(text);
                }
            }
    }

    void Cancer(Button btn, int value) {
        btn.setText("" + current);
        current = value;
    }

    void Libra(Button btn, int value) {
        btn.setText("" + (-value));
        current *= -1;
    }

    //region Оформление

    String GetTextGoalLabel() {
        if (is_overflow_level)
            return current + " -> more " + result_mass[0];

        String rez = String.valueOf(current);
        for (int i = result_id; i < result_mass.length; i++) {
            if (i >= result_id + 2) return rez + "...";
            rez += " -> " + result_mass[i];
        }
        return rez;
    }

    String GetTextStepLabel() {
        if (sign == null || sign == "") return "";
        switch (sign) {
            //case "+": return "[[" + current + " " + sign + "]]";    // Пытаюсь понять, что происходит.
            case "^2":   return current + " * " + current;
            case "+2*":  return current + " + 2 * x";
            case "+10*": return current + " + 10 * x";
            case "-3*":  return current + " - 3 * x";
            case "<<":
            case ">>": return GetString(current) + " " + sign + " 1";
            case "-1": return GetString(current) + "--";
            case "+1": return GetString(current) + "++";
            case "G": return "Duplicate";
            case "C": return "Switch";
            case "L": return "Inversion";
        }
        return GetString(current) + " " + sign + " x";
    }

    String GetTextStepLabelAfter(int text) {
        switch (sign)
        {
            case "+":
            case "-":
            case "*":
            case "/":
            case "&":
            case "^":
            case "⊕":
                return GetString(current) + " " + sign + " " + GetString(text) + " = ";
            case "^2":   return current + " * " + current + " = ";
            case "+2*":  return current + " + 2 * " + text + " = ";
            case "+10*": return current + " + 10 * " + text + " = ";
            case "-3*":  return current + " - 3 * " + text + " = ";
            case "<<":
            case ">>": return GetString(current) + " " + sign + " 1 = ";
            case "-1": return GetString(current) + "-- = ";
            case "+1": return GetString(current) + "++ = ";
            case "G": return "Duplicate " + text;
            case "L": return "Inversion " + text;
            case "C": return "Switch " + text;
            default: return "ERROR [" + sign + "]";
        }
    }

    String GetString(int val) {
        if (!is_binary_level)
            return String.valueOf(val);

        // Binary
        String rez = "";
        while (val > 0)
        {
            rez = String.valueOf(val % 2) + rez;
            val /= 2;
        }
        while (rez.length() < 5)
            rez = "0" + rez;
        return rez;
        //return "0b" + rez;
    }

    int GetColorGoalLabel() {
        if (is_overflow_level)
            return Color.RED;
        return Color.BLACK;
    }

    int GetColorStepLabel() {
        if (is_overflow_level)
            return Color.BLUE;
        return Color.BLUE;
    }

    //endregion

    //region Layout

    LinearLayout GetHorizLayout(){
        LinearLayout horiz = new LinearLayout(this);
        horiz.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        horiz.setOrientation(LinearLayout.HORIZONTAL);
        return horiz;
    }

    LinearLayout.LayoutParams GetButtonParams(){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(10, 80);
        lparams.setMargins(0, 0, 0, 0);
        lparams.width = 0;
        lparams.weight = 1;
        return lparams;
    }

    LinearLayout.LayoutParams GetButtonParams(int height, int margin){
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(0, 0, 0, 0);
        lparams.weight = 1;
        lparams.width = 0;
        lparams.height = height;
        lparams.leftMargin = margin;
        lparams.rightMargin = margin;
        lparams.topMargin = margin;
        lparams.bottomMargin = margin;
        return lparams;
    }

    //endregion

    //region Methods

    void SetMessage(String text){
        markers += text;
        tvError.setText(markers);

//        tvError.setText(markers + ": " + text);
//        markers = "";
    }

    void SetMarker(String text){
        markers += text;
    }

    void ClearMarker(){
        markers = "";
    }

    void PlaySound(MediaPlayer mp) {
        if (mp.isPlaying())
            mp.pause();

        mp.seekTo(0);
        mp.start();
    }


    void JustMonika() {
        // Easter egg #1
        imageBackground.setImageResource(R.drawable.background_monika);
        imageBackground.setAlpha(1);
        imageBackground.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeleteMonika();
            }
        }, 1000);
    }

    void DeleteMonika()
    {
        if (!is_overflow_level)
        {
            imageBackground.setImageResource(R.drawable.background);
            imageBackground.setVisibility(View.INVISIBLE);
        }
        else
        {
            // Easter egg #2
            int[] mon = { R.drawable.background_monika, R.drawable.background_monika2 };

            imageBackground.setImageResource(mon[1]);
            for (Button btn : play_buttons)
                imageBackground.setImageResource(mon[rand.nextInt(2)]);
            for (Button btn : stack_buttons)
                imageBackground.setImageResource(mon[rand.nextInt(2)]);
        }
    }

    //endregion
}