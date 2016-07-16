package com.example.suvendu.wikigame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Suvendu on 7/4/2016.
 */
public class ClickableSpanSetup extends ClickableSpan {

    int localIndex;
    String[] words;
    Context context;
    ListPopupWindow listPopupWindow;
    TextView anchor;

    public ClickableSpanSetup(int index, String[] words, Context con, ListPopupWindow listPopupWindow) {
        localIndex = index;
        this.words = words;
        context=con;
        this.listPopupWindow= listPopupWindow;
    }

    @Override
    public void onClick(View widget) {
        Log.v("On click","Inside On click of the clickable");
        listPopupWindow.setAnchorView(widget);
        //listPopupWindow.setContentWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setHorizontalOffset(widget.getLeft());
        listPopupWindow.setVerticalOffset(-(widget.getRight()));
        listPopupWindow.setAnimationStyle(R.style.AppTheme);
        //listPopupWindow.setVerticalOffset();
        listPopupWindow.show();
        Log.v("On click","After Show");

    }


    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.BLUE);
    }
}
