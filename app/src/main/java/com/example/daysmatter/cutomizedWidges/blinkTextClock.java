package com.example.daysmatter.cutomizedWidges;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class blinkTextClock extends androidx.appcompat.widget.AppCompatTextView {
    private Context context;

    public blinkTextClock(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public blinkTextClock(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public blinkTextClock(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setText(CharSequence text, Animation animation, List<Integer> indices) {
        if (animation == null){
            super.setText(text);
            return;
        }
        if (text != null && text.length() != 0) {
            for (int i = 0; i < text.length(); i++){
                final TextView t = new TextView(context);
                t.setText(text.charAt(i));
                if (indices.get(i) == 1) {
                    t.setAnimation(animation);
                }
            }
        }
    }
}