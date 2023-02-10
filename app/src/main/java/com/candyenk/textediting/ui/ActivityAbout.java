package com.candyenk.textediting.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import candyenk.android.asbc.ActivityCDK;
import candyenk.android.utils.UFile;
import candyenk.android.widget.DialogBottomText;
import candyenk.android.widget.Item;
import candyenk.java.io.IO;
import com.candyenk.textediting.R;

public class ActivityAbout extends ActivityCDK {
    private ImageView logo;
    private TextView copyright;
    private Item gson, luaj, glide;
    private Spanned sWhatever, sGson, sLuaj, sGlide;
    DialogBottomText db;

    @Override
    protected void intentInit() {
        db = new DialogBottomText(this);
    }

    @Override
    protected void viewInit() {
        setContentView(R.layout.activity_about);
        setTitle(R.string.act_about);
        logo = findViewById(R.id.logo);
        gson = findViewById(R.id.gson);
        luaj = findViewById(R.id.luaj);
        glide = findViewById(R.id.glide);
        copyright = findViewById(R.id.copyright);
    }

    @Override
    protected void contentInit(Bundle save) {

    }

    @Override
    protected void eventInit() {
        gson.setOnClickListener(this::show);
        luaj.setOnClickListener(this::show);
        glide.setOnClickListener(this::show);
        copyright.setMovementMethod(LinkMovementMethod.getInstance());
        logo.setOnLongClickListener(this::whatever);
    }

    @Override
    protected Bundle saveData(Bundle bundle) {
        return null;
    }

    @SuppressLint("NonConstantResourceId")
    private void show(View v) {
        switch (v.getId()) {
            case R.id.gson:
                if (sGson == null) sGson = Html.fromHtml(IO.readString(UFile.readAssets(this, "gson.html")));
                db.setContent(sGson);
                db.setTitle("Gson");
                break;
            case R.id.luaj:
                if (sLuaj == null) sLuaj = Html.fromHtml(IO.readString(UFile.readAssets(this, "luaj.html")));
                db.setContent(sLuaj);
                db.setTitle("Luaj");
                break;
            case R.id.glide:
                if (sGlide == null) sGlide = Html.fromHtml(IO.readString(UFile.readAssets(this, "glide.html")));
                db.setContent(sGlide);
                db.setTitle("Glide");
                break;
        }
        db.show();
    }

    private boolean whatever(View v) {
        if (sWhatever == null) sWhatever = Html.fromHtml(IO.readString(UFile.readAssets(this, "whatever.html")));
        db.setContent(sWhatever);
        db.setTitle(R.string.about_last_words);
        db.show();
        return true;
    }
}
