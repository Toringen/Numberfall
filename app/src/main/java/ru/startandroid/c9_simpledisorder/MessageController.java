package ru.startandroid.c9_simpledisorder;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vyacheslav on 24.05.2019.
 */

public class MessageController {
    List<String> list = new ArrayList<String>();

    public void AddMessage(String s) {
        list.add(s);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public String GetMessage() {
        if (isEmpty()) return "";

        String rez = list.get(0);
        list.remove(0);
        return rez;
    }

    public void ShowLastMessage(Context context,
                                ConstraintLayout layoutPlayerMessage,
                                LinearLayout llPlayerMessage,
                                ImageView image,
                                TextView tvPlayerMessage,
                                boolean is_force) {
        if (isEmpty())
            layoutPlayerMessage.setVisibility(View.INVISIBLE);
        else if (layoutPlayerMessage.getVisibility() == View.INVISIBLE || is_force) {
            String s = GetMessage();
            tvPlayerMessage.setText(s.substring(2));
            layoutPlayerMessage.setVisibility(View.VISIBLE);
            switch (s.charAt(0))
            {
                case 'U': llPlayerMessage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_messages_red));
                    tvPlayerMessage.setTextColor(Color.BLACK);
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.menu_star);
                    break;
                case 'M': llPlayerMessage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_messages));
                    tvPlayerMessage.setTextColor(Color.WHITE);
                    image.setVisibility(View.GONE);

                    break;
                case 'A': llPlayerMessage.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_messages_blue));
                    tvPlayerMessage.setTextColor(Color.WHITE);
                    image.setVisibility(View.VISIBLE);
                    image.setImageResource(R.drawable.message_crown);
                    break;
            }
        }
    }
}
