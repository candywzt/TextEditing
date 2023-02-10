package com.candyenk.textediting.ui;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import androidx.cardview.widget.CardView;
import candyenk.android.asbc.ActivityCDK;
import candyenk.android.tools.NV;
import com.candyenk.textediting.R;

import java.util.Random;


public class ActivityTest extends ActivityCDK {
    @Override
    protected void intentInit() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void viewInit() {
        setContentView(R.layout.activity_test);
        setTitle(R.string.act_nimble);
        CardView cv = findViewById(R.id.card);
        NV.apply(cv);
        cv.setOnClickListener(v -> {
            int w = v.getWidth();
            int h = v.getHeight();
            Animator a = ViewAnimationUtils.createCircularReveal(v, w / 2, h / 2, 0, h);
            a.setInterpolator(new DecelerateInterpolator());
            a.setDuration(1000);
            cv.setCardBackgroundColor(0xff000000 + new Random().nextInt(0xffffff));
            a.start();
        });

    }


    @Override
    protected void contentInit(Bundle save) {

    }

    @Override
    protected void eventInit() {

    }

    @Override
    protected Bundle saveData(Bundle bundle) {
        return null;
    }
}
