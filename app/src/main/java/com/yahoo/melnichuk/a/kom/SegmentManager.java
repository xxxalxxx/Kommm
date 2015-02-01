package com.yahoo.melnichuk.a.kom;

import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xxxalxxx on 27.01.2015.
 */
public class SegmentManager {
    TextView s;
    EditText editText;
    List<Segment> data = new ArrayList<>();
    int segs_len = 7;
    final int SPAN_NUM = 4;

    int[] span_len = new int[segs_len];
    int[] spIDs = new int[segs_len];
    int result=0;
    int[]segmentBtns = {
            R.id.communalButton,
            R.id.coldWaterButton,
            R.id.wasteWaterButton,
            R.id.hotWaterButton,
            R.id.heatingButton,
            R.id.electricityButton,
            R.id.phoneButton
    };

    SegmentManager(TextView seg,EditText e){
        s = seg;
        Arrays.fill(span_len,1);
        editText = e;
    }

    class Segment {
        int line;
        int upperOffset;
        int[] bounds = new int[2];
        CharSequence content;
        int span_increment=0;
        SparseArray<Integer> idHolder = new SparseArray<>();

        Segment(int b1,int b2) {
            line = b1;
            bounds[0]=line;
            bounds[1]=b2;
        }
    }


    void add(int tID,ImageButton ib){
        addName(tID);
        addSegment(tID);
        addResult(tID);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) ib.getLayoutParams();
        params.leftMargin += result;

        spIDs[tID]++;
        get(tID).span_increment+=SPAN_NUM;
     //       Log.d(MainActivity.TAG," start:"+ s.getLayout().getLineRight(tID)+" end:"+s.getLayout().getLineEnd(tID));
    }


    void addName(int tID){
        final int id = tID;
        final int spanID = spIDs[tID];
        SpannableString ss = new SpannableString("Людина "+(spIDs[tID]+1)+"X");

        int line = get(tID).line;
        get(tID).bounds[0]= s.getLayout().getLineStart(line);
        get(tID).idHolder.append(spanID, get(id).span_increment);

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

                int line = get(id).line;

                get(id).bounds[0] = s.getLayout().getLineStart(line);
                ClickableSpan[] spans;
                SpannableString sp;
                SparseArray<Integer> holder = get(id).idHolder;
                int offset = 1;
                for(int i=0;i<3;i++){
                    spans = getSpans(id);
                    sp = spanStr();
                    s.getEditableText().delete( sp.getSpanStart(spans[ holder.get(spanID)]),  sp.getSpanEnd(spans[ holder.get(spanID)+ offset ]) );
                    offset=0;
                }

               int next = holder.indexOfKey(spanID);
               holder.delete(spanID);
                if( next < holder.size() )
                    for (int i = next; i < holder.size(); i++)
                        holder.setValueAt(i,holder.valueAt(i)-SPAN_NUM);

                get(id).span_increment-=SPAN_NUM;
                get(id).bounds[1]= findLine(line+2);

            }
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }

        },ss.length()-1,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new BackgroundColorSpan(0xFFbc0101), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFFFFFFFF), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ScaleXSpan(3), ss.length()-1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        s.getEditableText().insert(findLine(line),ss);
        Log.d(MainActivity.TAG,""+s.getLayout().getLineStart(line)+ " "+s.getLayout().getLineEnd(line) );

    }


    void addSegment(int tID){

        SpannableString ss = new SpannableString("nums      "+(spIDs[tID]+1) );

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(MainActivity.TAG,""+"clicked nums");
            }
        },0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int line = get(tID).line+1;
       int start = findLine(line);
        s.getEditableText().insert(findLine(line),ss);
        int end = findLine(line);

        Paint textPaint = s.getPaint();
        result= (int)textPaint.measureText(s.getText(),start,end);

    }


    void addResult(int tID){

        SpannableString ss = new SpannableString("sums      "+(spIDs[tID]+1) );

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Log.d(MainActivity.TAG,""+"clicked sums");
            }
        },0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        int line = get(tID).line+2;
        s.getEditableText().insert(findLine(line), ss);
        get(tID).bounds[1]= findLine(line);
    }


    void setContent(int tID,CharSequence c){
        data.get(tID).content=c;
    }


    CharSequence getContent(int tID){
        return data.get(tID).content;
    }
    SegmentManager.Segment get(int tID){
        return data.get(tID);
    }

    int findLine(int l){
        CharSequence cs = s.getText();
        int len = cs.length();
        int count=0;
        for(int i=0;i<len;i++)
            if(cs.charAt(i)=='\n'){
                if(count==l) return i;
                count++;
            }
        return 1;
    }

    ClickableSpan[] getSpans(int id){
        return new SpannableString( s.getText() ).getSpans(get(id).bounds[0], get(id).bounds[1], ClickableSpan.class);
    }

    SpannableString spanStr(){
        return new SpannableString( s.getText() );
    }

    int getUpperLine(int tID){
        return get(tID).line - get(tID).upperOffset;
    }

    int getLowerLine(int tID){
        return get(tID).line+3;
    }

}
                  /*
                                 // int spStart = sp.getSpanStart(spans[ holder.get(spanID)]);
              //  int spEnd = sp.getSpanEnd(spans[ holder.get(spanID)+1 ]);

                for(int i =0;i<spans.length;i++)
                    Log.d(MainActivity.TAG,""+s.getText().subSequence( sp.getSpanStart(spans[i]),sp.getSpanEnd(spans[i]) )
                            +" span start:"+sp.getSpanStart(spans[i]) + " span end:"+sp.getSpanEnd(spans[i]) );


                Log.d(MainActivity.TAG,"ID:"+id+" spanID:"+spanID);
                Log.d(MainActivity.TAG,"sp len:"+spans.length);
                Log.d(MainActivity.TAG,"bound 1:"+get(id).bounds[0] + " bound 2:"+ get(id).bounds[1]);
                Log.d(MainActivity.TAG,"line start:"+s.getLayout().getLineStart(line) );
                Log.d(MainActivity.TAG,"span start:"+spStart+ " span end:"+ spEnd  );
              */