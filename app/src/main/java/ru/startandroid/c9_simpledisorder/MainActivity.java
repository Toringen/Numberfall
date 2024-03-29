package ru.startandroid.c9_simpledisorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
    ConstraintLayout clMessages;
    LinearLayout grid, llMessages;
    TextView tvLLabel, tvGoal, tvStep, tvCountSteps, tvError, tvMessages;
    ImageView imageBackground, imageMessages;
    ImageView[] images_sign;
    Button[][] buttons;
    String[][] levels;
    int SCREEN_WIDTH, SCREEN_HEIGHT;

    Random rand = new Random();
    String markers = "";
    int count_steps, size, level_id, result_id, sorcerer_mode1, sorcerer_mode2, id_mode;
    long current, current_start;
    long[] result_mass;
    List<Integer> list_empty_numbers = new ArrayList<Integer>();
    MessageController m = new MessageController();
    float prob_button_empty;
    boolean is_overflow_level = false;
    boolean is_binary_level = false;
    boolean is_hardmode_level = false;

    // Play (Up) buttons
    ImageView[] play_buttons;
    int play_id_button, play_id_sign;
    String sign;

    // Stack buttons
    ImageView[] stack_buttons;
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
        tvCountSteps = (TextView) findViewById(R.id.textViewCountSteps);
        tvError = (TextView) findViewById(R.id.textViewError);
        tvMessages = (TextView) findViewById(R.id.textViewPlayerMessage);
        llMessages = findViewById(R.id.linearLayoutPlayerMessage);
        clMessages = findViewById(R.id.layoutPlayerMessage);
        imageMessages = findViewById(R.id.imageMessage);
        imageBackground = (ImageView) findViewById(R.id.imageViewBackground);
        ImageView btnPlay1  = findViewById(R.id.btnImageLeft);
        ImageView btnPlay2  = findViewById(R.id.btnImageRight);

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
        play_buttons = new ImageView[] { btnPlay1, btnPlay2 };
        for (ImageView btn : play_buttons) {
            btn.setBackgroundResource(0);
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

                    ImageView btn = (ImageView) v;
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
                        SetBlackWhite(btn);
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
                        SetBlackWhiteClear();
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
        ImageView btnStack1 = findViewById(R.id.btnStack1);
        ImageView btnStack2 = findViewById(R.id.btnStack2);
        ImageView btnStack3 = findViewById(R.id.btnStack3);
        ImageView btnStack4 = findViewById(R.id.btnStack4);
        ImageView btnStack5 = findViewById(R.id.btnStack5);
        stack_buttons = new ImageView[] { btnStack1, btnStack2, btnStack3, btnStack4, btnStack5 };
        stack_default = new String[] { "", "", "", "", "" };
        for (ImageView btn : stack_buttons) {
            btn.setBackgroundResource(0);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ImageView btn = (ImageView) v;
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
                        SetBlackWhite(btn);
                    }
                    catch (Exception e) {
                        ImageView btn = (ImageView) v;
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
        tvMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.ShowLastMessage(MainActivity.this, clMessages, llMessages, imageMessages, tvMessages, false, true);
            }
        });
        clMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.ShowLastMessage(MainActivity.this, clMessages, llMessages, imageMessages, tvMessages, false, true);
            }
        });

        Button btnClear = (Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AchieveController.RestartLevel(m);
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

    @Override
    protected void onDestroy() {
        AchieveController.Save();
        super.onDestroy();
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
                btn.setTextSize(18);
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
                            long value = Long.parseLong(text);
                            String step_label = GetTextStepLabelAfter(value);
                            boolean is_delete_button = true;

                            // Update step
                            switch (sign) {
                                case "+": current += value; break;
                                case "-": current -= value; break;
                                case "*": current *= value; break;
                                case "/":
                                    if (value == 0) {
                                        for (Button[] mass : buttons)
                                            for (final Button btn : mass)
                                                if (btn.isEnabled())
                                                {
                                                    final Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            players[rand.nextInt(11)].start();
                                                            btn.setText("#");
                                                        }
                                                    }, 30 + rand.nextInt(1000));
                                                }
                                    }
                                    else current /= value;
                                    break;
                                case "^2": current *= current; break;

                                case "+2*": current += 2 * value; break;
                                case "+10*": current += 10 * value; break;
                                case "-3*": current -= 3 * value; break;
                                case "G": is_delete_button = false; Gemini(text); break;
                                case "C": is_delete_button = false; Cancer(b, value); break;
                                case "L": is_delete_button = false; Libra(b, value); break;
                                case "P": is_delete_button = false; SetText(b, "" + (value * value)); break;
                                case "R": long temp = result_mass[result_id]; result_mass[result_id] = current; current = temp; break;

                                case "&": current &= value; break;
                                case "^": current |= value; break;
                                case "⊕": current ^= value; break;
                                case "<<": current *= 2; break;
                                case ">>": current /= 2; break;
                                case "-1": current--; break;
                                case "+1": current++; break;
                            }

                            //
                            tvCountSteps.setText("Steps: " + ++count_steps);

                            // Achievements
                            AchieveController.AddStep(m, count_steps, sign, current, buttons);

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
                            SetBlackWhiteClear();
                            play_id_button = -1;
                            sign = " ";

                            // New game
                            if (isEndLevel()) {
                                AchieveController.EndLevel(m, sign, current, buttons);

                                String code = StarController.EndLevel(id_mode, level_id, is_hardmode_level);
                                if (code != "")
                                    for (String s : code.split("_")) {
                                        int id = Integer.parseInt(s.substring(1));
                                        if (s.charAt(0) == 'L')
                                        {
                                            AchieveController.AddLevel(m, id);
                                        }
                                        else
                                        {
                                            switch (id + 1) {
                                                case 5:
                                                case 10:
                                                case 15:
                                                case 20:
                                                case 40:
                                                case 50:
                                                case 60:
                                                case 70:
                                                case 80: m.AddMessage("U:Разблокирована новая сложность!"); break;
                                            }
                                            AchieveController.AddStar(m, id);
                                        }
                                    }

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoadLevel(level_id + 1);
                                    }
                                }, 500);
                            }

                            m.ShowLastMessage(MainActivity.this, clMessages, llMessages, imageMessages, tvMessages, true, false);
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
        if (value >= levels.length ||
                !StarController.is_godmode &&
                    (StarController.GetCountNeededStars(value) > 0 ||
                    is_hardmode_level && StarController.GetCountCompletedLevelsOnDifficulty(value) < 4))
            MainActivity.this.finish();

        try {
            try {
                level_id = value;
                is_overflow_level = false;
                is_binary_level = false;
                prob_button_empty = is_hardmode_level ? 0.35f : 0.05f;
                sorcerer_mode1 = 0;
                sorcerer_mode2 = 0;
                stack_current = 0;
                current_start = 0;
                list_empty_numbers.clear();
                for (int i = 0; i < sign_chances.length; i++)
                    sign_chances[i] = 0;
                for (int i = 0; i < stack_default.length; i++)
                    stack_default[i] = "";
                clMessages.setVisibility(View.INVISIBLE);
                ClearMarker();
                AchieveController.StartLevel(m);
            } catch (Exception e) {
                SetMessage("LoadLevelErrorStart: " + e.getMessage() + e.getClass().toString());
            }

            //m.AddMessage("M:Сообщение");
            //m.AddMessage("U:Окончание");
            //m.AddMessage("A:Достижение");

            for (String code : levels[value]) {
                //SetMessage(code + " ");
                try {
                    if (code.charAt(0) == 'N') {
                        if (is_hardmode_level) continue;
                        code = code.substring(1);
                    }
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
                    else if (code.startsWith("Sorcerer"))
                        sorcerer_mode1 = Integer.parseInt(code.substring(8));
                    else if (code.startsWith("Repeat"))
                        sorcerer_mode2 = Integer.parseInt(code.substring(6));
                    else if (code.startsWith("StackSet"))
                        stack_default[stack_current++] = code.substring(8);
                    else
                        switch (code.charAt(0)) {
                            case 'A':
                            case 'А':   // link
                                if (!sPrefLevels.getBoolean("M" + id_mode + "L" + level_id, false)) {
                                    m.AddMessage("M:" + v);
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
                                result_mass = new long[r_mass.length];
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

            try {
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

            } catch (Exception e) {
                SetMessage(" [LoadLevelSum: " + e.getMessage());
            }

            //SetMessage("CHANCES: ");
            //for (int i = 0; i < sign_chances.length; i++)
            //    SetMarker(sign_chances[i] + " ");
            //SetMessage("");


            // Set result of level
            // Set background menace jojo reference
            imageBackground.setVisibility(is_overflow_level ? View.VISIBLE : View.INVISIBLE);

            // Show user message
            m.ShowLastMessage(MainActivity.this, clMessages, llMessages, imageMessages, tvMessages, true, false);

            // Clear level
            Clear();
        } catch (Exception e) {
            SetMessage(" LoadLevelError: " + e.getMessage());
        }
    }

    void Clear() {
        try {
            //stack_size = 0;
            current = current_start;
            result_id = 0;
            count_steps = 0;
            play_id_button = -1;
            sign = " ";
            tvLLabel.setText("Level " + (level_id + 1));
            tvGoal.setTextColor(GetColorStepLabel());
            tvGoal.setText(GetTextGoalLabel());
            tvGoal.setTextSize(tvGoal.getText().length() < 18
                    && result_mass[0] <= 99999
                    && result_mass.length == 1
                    && is_overflow_level ? 24 : 16);
            tvCountSteps.setText("Steps: " + count_steps);
            tvStep.setTextColor(GetColorGoalLabel());
            tvStep.setText("");
            StackClear();
            SetBlackWhiteClear();

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
                    int id = j + size * i + (is_binary_level || sorcerer_mode1 != 0 ? 0 : 1);
                    if (sorcerer_mode1 != 0) id = 1 + id % sorcerer_mode1;
                    if (sorcerer_mode2 != 0) id = sorcerer_mode2;
                    anim_sign *= -1;

                    // Set text and enable
                    Button btn = buttons[i][j];
                    SetText(btn, String.valueOf(id));
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
        } catch (Exception e) {
            SetMessage("Clear: " + e.getMessage());
        }
    }

    void PlayButtonChange(int id_button){
        int id_sign = 0;
        try {
            ImageView btn = play_buttons[id_button];
            double chance = rand.nextDouble();
            while (chance >= sign_chances[id_sign])
                id_sign++;

            // A заменяется на умножение, если это спец-уровень.
            // Видимо, А - это что-то читерское.
            //if (SignController.list_signs[id_sign].sign.equals("A") && is_overflow_level)
            //    id_sign = 2;

            // Set sign
            Sign sign = SignController.list_signs[id_sign];
            btn.setImageResource(sign.id_image);
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

    void StackSetSign(int id_button, String sign){
        try {
            ImageView btn = stack_buttons[id_button];
            btn.setTag(sign);
            if (sign != "")
                btn.setImageResource(SignController.GetSign(sign).id_image);
            else {
                if (id_button < stack_size)
                    btn.setImageResource(R.drawable.s_empty);
                else
                    btn.setImageResource(0);
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

                ImageView next_stack = stack_buttons[i + 1];
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
        for (Button[] mass : buttons)
            for (Button btn : mass) {
                if (btn.getText() == "") {   // && (--counter >= 0 || rand.nextDouble() < 0.6)
                    SetText(btn, text);
                 }
            }
    }

    void Cancer(Button btn, long value) {
        SetText(btn, "" + current);
        current = value;
    }

    void Libra(Button btn, long value) {
        SetText(btn, "" + (-value));
        current *= -1;
    }

    //region Оформление

    void SetBlackWhite(ImageView click_btn) {
        try {
            ColorMatrix bwMatrix = new ColorMatrix();
            bwMatrix.set(new float[] {
                    1, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0,
                    0, 0, 0, 1, 0 });
            ColorMatrixColorFilter bwFilter = new ColorMatrixColorFilter(bwMatrix);

            ColorMatrix clrMatrix = new ColorMatrix();
            clrMatrix.setSaturation(1);
            ColorMatrixColorFilter clrFilter = new ColorMatrixColorFilter(clrMatrix);

            for (ImageView btn : play_buttons)
                btn.setColorFilter(btn.equals(click_btn) ? clrFilter : bwFilter);
            for (ImageView btn : stack_buttons)
                btn.setColorFilter(btn.equals(click_btn) ? clrFilter : bwFilter);
                //SetMessage(btn.equals(click_btn) ? "1" : "0");
        }
        catch (Exception e) {
            SetMessage("SetBlackWhite:" + e.getMessage());
        }
    }

    void SetBlackWhiteClear() {
        try {
            ColorMatrix clrMatrix = new ColorMatrix();
            clrMatrix.setSaturation(1 /* 0.88f */);
            ColorMatrixColorFilter clrFilter = new ColorMatrixColorFilter(clrMatrix);

            for (ImageView btn : play_buttons)
                btn.setColorFilter(clrFilter);
            for (ImageView btn : stack_buttons)
                btn.setColorFilter(clrFilter);
        }
        catch (Exception e) {
            SetMessage("SetBlackWhite:" + e.getMessage());
        }
    }

    void SetText(Button btn, String text) {
        btn.setText(text);

        float textSize = 18;
        int length = text.length();
        if (length > 13)
            textSize = 5;
        else
            switch (size) {
                case 6: textSize = new int[] { 18,18,18,15,12, 9,7,6,6,6, 6,6,6 }[length]; break;
                case 7: textSize = new int[] { 18,18,15,9, 7,  5,5,5,5,5, 5,5,5 }[length]; break;
            }
        if (btn.getTextSize() != textSize)
            btn.setTextSize(textSize);
    }

    String GetTextGoalLabel() {
        if (is_overflow_level)
            return current + " -> more " + result_mass[0];

        String rez = String.valueOf(current);
        for (int i = result_id; i < result_mass.length; i++) {
            if (i >= result_id + 2) return rez + " -> ...";
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
            case "P": return "Pow";
        }
        return GetString(current) + " " + sign + " x";
    }

    String GetTextStepLabelAfter(long text) {
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
            case "^2":  return current + " * " + current + " = ";
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
            case "P": return "Pow " + text;
            default: return "ERROR [" + sign + "]";
        }
    }

    String GetString(long val) {
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

    void DeleteMonika() {
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
            for (ImageView btn : play_buttons)
                imageBackground.setImageResource(mon[rand.nextInt(2)]);
            for (ImageView btn : stack_buttons)
                imageBackground.setImageResource(mon[rand.nextInt(2)]);
        }
    }

    //endregion
}