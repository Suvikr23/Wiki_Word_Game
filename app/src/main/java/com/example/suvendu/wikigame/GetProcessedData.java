package com.example.suvendu.wikigame;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Suvendu on 6/26/2016.
 */
public class GetProcessedData {

    static String[] words;
    static String[] adapterWords = new String[10];
    static int ran;
    private static int[] adapterIndexes = new int[10];
    static StringBuilder sb = new StringBuilder();

    public static SpannableString processData(String string, Context con, ListPopupWindow popupList) {

        words = string.split("\\s+");
        Random random = new Random();
        Log.v("Length of the string", words.length - 1 + "");
        for (int i = 0; i <= 9; i++) {
            ran = random.nextInt(words.length - 1);
            adapterIndexes[i] = ran;
            Log.v("random", ran + "");
            //to remove periods and commas
            if (words[ran].contains(",") || words[ran].contains(".")) {
                adapterWords[i] = words[ran].substring(0, words[ran].length() - 2).toLowerCase();
            } else
                adapterWords[i] = words[ran].toLowerCase();

            words[ran] = "_____";//replacing the word with a blank and highlighting it
        }

        for (String j : words) {
            sb.append(j + " ");
        }

        return getClickableData(sb.toString(), adapterWords,con,popupList);
    }

    //context is sent as parameter to setup the Pop-up List View
    public static SpannableString getClickableData(String string, String[] adapterWords, Context con , ListPopupWindow popupList) {

        SpannableString ssb = new SpannableString(string);
        Log.v("String dashed",string);
        int index = string.indexOf("_____");

        while (index >= 0) {
            ssb.setSpan(new ClickableSpanSetup(index, adapterWords,con, popupList), index, index + 5,Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            Log.v("Index",index+"");
            index = string.indexOf("_____", index + 1);

        }
        return ssb;
    }

    public static String[] getAdapterWords() {
        return adapterWords;
    }

    public static int[] getAdapterIndex() {
        return adapterIndexes;
    }


}
