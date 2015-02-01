package com.yahoo.melnichuk.a.kom;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxxalxxx on 19.01.2015.
 */
public class Table {
    String name;
    SpannableString sp;
    CharSequence content;
    ClickableSpan[] spans;
    boolean checked=true;
    int len=0;
    int spanLen=0;
    int[] bounds = new int[2];
    int[] lines = new int[2];

    Table(String name,int start){
          this.name = name;
          bounds[0]=start;
    }
    void setContent(CharSequence c){
    content=c;
    len = c.length();
    spans = new SpannableString(c).getSpans(0, len, ClickableSpan.class);
    spanLen = spans.length;
    }
}
