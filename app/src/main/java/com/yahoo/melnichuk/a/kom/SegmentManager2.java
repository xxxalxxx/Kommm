package com.yahoo.melnichuk.a.kom;

import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxxalxxx on 01.02.2015.
 */
public class SegmentManager2 {
    TextView t;
    TableManager TM;
    int tID, line3, line2, line1,
            line3end, line2end, line1end;
    ImageButton ib;
    final int SEGS_LEN = 7;
    final int SPAN_NUM = 4;
    int[] spIDs = new int[SEGS_LEN];


    int[]segmentBtns = {
            R.id.communalButton,
            R.id.coldWaterButton,
            R.id.wasteWaterButton,
            R.id.hotWaterButton,
            R.id.heatingButton,
            R.id.electricityButton,
            R.id.phoneButton
    };
    SegmentManager2(TextView t,TableManager TM){
        this.t =t;
        this.TM = TM;
    }

    void add(int tID,ImageButton ib){
        this.tID=tID;
        this.ib = ib;
        addLines();

        addName();
        addFraction();
        addSum();
        spIDs[tID]++;
        for(int i=tID;i<SEGS_LEN;i++){
          TM.get(i).bounds[0]= findLine(TM.get(i).lines[0]);
          TM.get(i).bounds[1] = findLine(TM.get(i).lines[1]);
        }

    }
    void addName(){

        line1end = findLine(line1);
        SpannableString ss = new SpannableString("Людина "+(spIDs[tID]+1)+"X");
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(MainActivity.TAG,""+"clicked name");
            }
        },0,ss.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new BackgroundColorSpan(0xFF009688),0, ss.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF),0, ss.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {


            }
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

        },ss.length()-1,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new BackgroundColorSpan(0xFFbc0101), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ScaleXSpan(3), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        CharSequence text = TextUtils.concat(t.getText().subSequence(0, line1end),
                ss, t.getText().subSequence(line1end, t.length()));
        t.setText(text);
    }
    void addFraction(){
        line2end = findLine(line2);
        SpannableString ss = new SpannableString("nums      "+(spIDs[tID]+1) );
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(MainActivity.TAG,""+"clicked nums");
            }
        },0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence text = TextUtils.concat(t.getText().subSequence(0, line2end),
                ss, t.getText().subSequence(line2end, t.length()));
        t.setText(text);
    }
    void addSum(){
        line3end = findLine(line3);
        SpannableString ss = new SpannableString("sums      "+(spIDs[tID]+1) );
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(MainActivity.TAG,""+"clicked sums");
            }
        },0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        CharSequence text = TextUtils.concat(t.getText().subSequence(0, line3end),
                ss, t.getText().subSequence(line3end, t.length()));
        t.setText(text);
    }

    void addLines(){
        line3 = t.getLayout().getLineForOffset(TM.get(tID).bounds[1]);
        line2 = line3-1;
        line1 = line2-1;
    }


    int findLine(int l){
        CharSequence cs = t.getText();
        int len = cs.length();
        int count=0;
        for(int i=0;i<len;i++)
            if(cs.charAt(i)=='\n'){
                if(count==l) return i;
                count++;
            }
        return 1;
    }

}
