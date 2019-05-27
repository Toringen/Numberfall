package ru.startandroid.c9_simpledisorder;

/**
 * Created by Vyacheslav on 23.05.2019.
 */

public class Achieve {
    private int id, id_image, counter, max;
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
        this.max = 0;
        is_unlocked = false;
    }

    public Achieve(int id, String code, String title, int id_image, String caption_before, String caption_after, int max) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.id_image = id_image;
        this.caption_before = caption_before;
        this.caption_after = (caption_after == "") ? caption_before : caption_after;
        this.counter = 0;
        this.max = max;
        is_unlocked = false;
    }

    public String GetCode() {
        return code;
    }

    public String GetTitle() {
        return title;
    }

    public String GetLabelBefore() {
        return caption_before;
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
        is_unlocked = (counter >= max);
        return is_unlocked;
    }
}
