package ru.startandroid.c9_simpledisorder;

/**
 * Created by Vyacheslav on 23.05.2019.
 */

public class Achieve {
    private int id, id_image, counter, counter_max, counter_goal;
    private String code, title, caption_before, caption_after;
    private boolean is_unlocked;

    public Achieve(int id, String code, String title, int id_image, String caption_before, String caption_after) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.id_image = id_image;
        this.caption_before = caption_before;
        this.caption_after = (caption_after == "") ? caption_before : caption_after;
        this.counter = 0;
        this.counter_max = 0;
        this.counter_goal = 0;
        is_unlocked = false;
    }

    public Achieve(int id, String code, String title, int id_image, String caption_before, String caption_after, int counter_goal) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.id_image = id_image;
        this.caption_before = caption_before;
        this.caption_after = (caption_after == "") ? caption_before : caption_after;
        this.counter = 0;
        this.counter_max = 0;
        this.counter_goal = counter_goal;
        is_unlocked = false;
    }

    public String GetCode() {
        return code;
    }

    public String GetTitle() {
        return title;
    }

    public String GetLabelBefore() {
        return caption_before + (counter_goal == 0 ? "" : String.format(" (%1$s / %2$s)", counter_max, counter_goal));
    }

    public String GetLabelAfter() {
        return caption_after;
    }

    public int GetCounter() {
        return counter;
    }

    public boolean isUnlocked() {
        return id > 0 && is_unlocked;
    }


    public boolean IsItYou(String code) {
        return this.code.equals(code);
    }


    public boolean Unlock() {
        if (is_unlocked) return false;
        is_unlocked = true;
        return true;
    }

    public boolean AddCounter(int value) {
        return SetCounter(counter + value);
    }

    public boolean SetCounter(int value) {
        if (is_unlocked) return false;
        counter = value;
        if (counter >= counter_max) counter_max = counter;
        is_unlocked = (counter >= counter_goal);
        return is_unlocked;
    }
}
