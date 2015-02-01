package com.yahoo.melnichuk.a.kom;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import android.os.Bundle;

import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;

import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    MainActivity mCallback = this;
    TableManager TM = new TableManager();
    Logic logic = new Logic(TM);
    ColourHandler greenDark = new ColourHandler(0xFF009688),
            greenLight = new ColourHandler(0xFF19a093),
            whiteDark = new ColourHandler(0xFFF0F0F0),// yellowy F5F5DD
            whiteLight = new ColourHandler(0xFFFFFFFF);
    TextView t;
   static TextView s;
    Editable e;
    SegmentManager SM;
    SegmentManager2 SM2;
    public static final String TAG = "MY_ACTIVITY";
    void numErr(){  Toast.makeText(getApplicationContext(),"Будь ласка, вкажіть вірний формат числа!", Toast.LENGTH_LONG).show(); }

    CharSequence value = "#";
    int valStart = 0,
        valEnd = 0,
        spanIndex = 0,
        spanPivot=0,
        spanLen = 0,
        counter = 0,
        diffrence = 0,
        tableSetter=0,
        tID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.textView);
        s = (TextView) findViewById(R.id.segmentsView);
        final EditText editText = (EditText) findViewById(R.id.editText);
        SM = new SegmentManager(s,editText);


        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableStringBuilder ssb_segments = new SpannableStringBuilder();

        t.setMovementMethod(LinkMovementMethod.getInstance());
        s.setMovementMethod(LinkMovementMethod.getInstance());

        String[][] ROWS = TM.ROWS;
        Table table=null;
        int flag = 0;
        SegmentManager.Segment segment;

        for (int i = 0; i < ROWS.length; i++) {
            ssb_segments.append(" \n");
            for (int j = 0; j < ROWS[i].length; j++) {

                final int startIndex = ssb.length();

                if(ROWS[i][0].contains("\t")){
                    String tableName = TM.names[tableSetter];
                    table = new Table(tableName,startIndex+2);
                    TM.data.add(tableSetter,table);
                    counter =0;
                    flag = i;
                    tableSetter++;
                }

                ssb.append(ROWS[i][j]);

                final int endIndex = ssb.length();


                if( ROWS[i][0].contains("\f")) {
                    if(table!=null) {
                        table.bounds[1] = endIndex-3;
                        table.lines[0] = flag;
                        table.lines[1] = i-1;

                        int segLineStart =i-3;
                        segment = SM.new Segment(segLineStart,i);
                        segment.upperOffset = segLineStart - flag-1;
                        SM.data.add(segment);
                        table.setContent(ssb.subSequence(table.bounds[0],table.bounds[1]));
                    }
                }


                if (ROWS[i][j].matches(".*\\d+.*") && !ROWS[i][j].matches(".*\\p{L}+.*")) {
                    final int spCnt = counter;
                    final int tID = tableSetter-1;
                    final int offsetStart = startIndex - TM.tStart(tID);
                    final int offsetEnd = endIndex - TM.tStart(tID);
                    ssb.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View v) {

                            int start = TM.tStart(tID)+offsetStart;
                            int end = TM.tStart(tID)+offsetEnd;
                            Layout layout = ((TextView) v).getLayout();
                            float wordStart = layout.getPrimaryHorizontal(start);
                            float wordEnd = layout.getPrimaryHorizontal(end);
                            int line =  layout.getLineForOffset(start);
                            int y = layout.getLineTop(line);

                            mCallback.tID=tID;
                            valStart = start;
                            valEnd = end;
                          //  spanIndex = TM.spanOffset(tID)+spCnt;
                            spanPivot = spCnt;
                            spanLen = TM.sLen(tID);
                            value = t.getText().subSequence(start, end);

                            params.setMargins((int) wordStart,y, 0, 0);
                            int difference = (int) wordEnd - (int) wordStart;
                            mCallback.diffrence = difference;
                            editText.setMinimumWidth(difference);
                            editText.setLayoutParams(params);
                            editText.setText(t.getText().subSequence(start, end).toString().trim() );
                            editText.setSelection(editText.getText().length());

                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                           Toast.makeText(getApplicationContext(),"tID"+tID+"spanPivot:"+spanPivot, Toast.LENGTH_LONG).show();
                        }
                    }, ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    counter++;
                    if (j % 2 == 0) {
                        ssb.setSpan(new BackgroundColorSpan(whiteDark.form()), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    } else {
                        ssb.setSpan(new BackgroundColorSpan(whiteLight.hex), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }

                } else if (ROWS[i][j].matches(".*\\p{L}+.*") || ROWS[i][j].matches("^[ \\t\\r\\n\\s]*$")) {
                    if (j % 2 == 0) {
                        ssb.setSpan(new BackgroundColorSpan(greenDark.form()), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ssb.setSpan(new ForegroundColorSpan(0xFFFFFFFF), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    } else {
                        ssb.setSpan(new BackgroundColorSpan(greenLight.form()), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        ssb.setSpan(new ForegroundColorSpan(0xFFFFFFFF), ssb.length() - ROWS[i][j].length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    }
                }


            }
            ssb.append("\n");
        }
        t.append(ssb);
       s.append(ssb_segments);

        SM2 = new SegmentManager2(t,TM);



        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String newValue = editText.getText().toString();
                    if (!newValue.matches(".*\\d+.*")
                            || newValue.replace("0", "").length() == 0) newValue = "0.00";

                    newValue = String.format("%." + TM.format(tID, spanPivot) + "f", Float.valueOf(newValue)).replace(",", ".");
                    String emptySpace = "";
                    Layout layout = t.getLayout();

                    int difference = Math.abs(newValue.length() - value.length());
                    if (newValue.length() <= value.length()) {
                        for (int i = 0; i < difference; i++)
                            emptySpace += " ";
                        newValue = emptySpace + newValue;
                        e = (Editable) t.getText();
                        e.replace(valStart, valEnd, newValue);
                    } else {
                        numErr();
                        return false;
                    }

                    logic.setText(t);
                    logic.setID(tID);
                    logic.doLogic();


                    SpannableString sp = new SpannableString(t.getText());
                    ClickableSpan[] spans = TM.get(tID).spans;
                    //spanIndex++;
                    spanPivot++;
                    //if (spanIndex < spans.length - 1){ spanPivot++;}
                    if (spanPivot >= spanLen) {
                        tID = TM.correlate(tID);
                        spanPivot = 0;
                        spanLen = TM.get(tID).spanLen;
                    }


                    Toast.makeText(getApplicationContext(), " iID:" + tID + " pivot" + spanPivot + " len" + spanLen, Toast.LENGTH_LONG).show();
                    valStart = sp.getSpanStart(spans[spanPivot]);
                    valEnd = sp.getSpanEnd(spans[spanPivot]);
                    value = t.getText().subSequence(valStart, valEnd);

                    float wordStart = layout.getPrimaryHorizontal(valStart);
                    float wordEnd = layout.getPrimaryHorizontal(valEnd);
                    int ln = layout.getLineForOffset(valStart);
                    int y = layout.getLineTop(ln);
                    int diff = (int) wordEnd - (int) wordStart;
                    editText.setMinimumWidth(diff);
                    params.setMargins((int) wordStart, y, 0, 0);
                    editText.setText(t.getText().subSequence(valStart, valEnd).toString().trim());
                    editText.setLayoutParams(params);
                    editText.setSelection(editText.getText().length());
                }
                return false;
            }
        });

        for (int id : TM.checkBoxIds) {
            CheckBox ch = (CheckBox) findViewById(id);
            ch.setOnCheckedChangeListener(this);
        }


        for (int id : SM.segmentBtns) {
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int[] cbIds = TM.checkBoxIds;

        if (!isChecked) {
            for (int i = 0; i < cbIds.length; i++)
                if (buttonView.getId() == cbIds[i]) {

                    TextView emptyT = (TextView) findViewById(TM.emptyIds[i]);
                    ImageButton imgBtn = (ImageButton) findViewById(SM.segmentBtns[i]);
                    SegmentManager.Segment seg =  SM.data.get(i);
                    imgBtn.setVisibility(View.GONE);
                    emptyT.setVisibility(View.GONE);
                    TM.uncheck(i);


/*
                    int upperLine = seg.line - seg.upperOffset;
                    int lowerLine = seg.line+3;
                    int segStart = s.getLayout().getLineStart(upperLine);
                    int segEnd = SM.findLine(lowerLine);
*/
                    int start = TM.get(i).bounds[0];
                    int end = TM.get(i).bounds[1];
    /*                int line1 = t.getLayout().getLineForOffset(start);
                    int line3 = t.getLayout().getLineForOffset(end);
                    int segStart = s.getLayout().getLineStart(line1);
                    int segEnd = SM.findLine(line3);
*/
                    TM.setContent(i,t.getText().subSequence(start,end));
  //                  SM.setContent(i,s.getText().subSequence(segStart,segEnd));
//                    final Layout layout = t.getLayout();
                   CharSequence text = TextUtils.concat(t.getText().subSequence(0,start),
                            t.getText().subSequence(end,t.length()));
    //               CharSequence seg_text = TextUtils.concat(s.getText().subSequence(0,segStart),
    //                        s.getText().subSequence(segEnd,s.length()));

                   t.setText(text);
       //            s.setText(seg_text);

                    int len = TM.tEnd(i)-TM.tStart(i);
                    int lineLen = TM.get(i).lines[1]-TM.get(i).lines[0];
//                   int diff = lowerLine - upperLine;
                    Log.d(TAG,""+TM.getContent(i));
                    for(int j=i;j<cbIds.length;j++){
                        TM.get(j).bounds[0]-=len;
                        TM.get(j).bounds[1]-=len;
                        TM.get(j).lines[0]-=lineLen;
                        TM.get(j).lines[1]-=lineLen;
  //                      int offset = TM.get(j).bounds[1];
    //                  int line = layout.getLineForOffset(offset-len);
//                        SM.get(j).line -=diff;
  //                      SM.get(j).line = line-2;


                    }
                    break;
                }

        } else
            for (int i = 0; i < cbIds.length; i++)
                if (buttonView.getId() == cbIds[i]) {

                    TextView emptyT = (TextView) findViewById(TM.emptyIds[i]);
                    ImageButton imgBtn = (ImageButton) findViewById(SM.segmentBtns[i]);
                    emptyT.setVisibility(View.VISIBLE);
                    imgBtn.setVisibility(View.VISIBLE);
                    TM.check(i);


//                    int diff = SM.getLowerLine(i) - SM.getUpperLine(i);
                    int len = TM.tEnd(i) - TM.tStart(i);
                    int lineLen = TM.get(i).lines[1]-TM.get(i).lines[0];
                    for(int j=i;j<cbIds.length;j++){
                        TM.get(j).bounds[0]+=len;
                        TM.get(j).bounds[1]+=len;
                        TM.get(j).lines[0]+=lineLen;
                        TM.get(j).lines[1]+=lineLen;
  //                      SM.get(j).line = t.getLayout().getLineForOffset( TM.tEnd(j) )-2;
                    }


                    int start = TM.tStart(i);
                    int end = TM.tEnd(i);
    //                int line1 = t.getLayout().getLineForOffset(start);
    //                int line3 = t.getLayout().getLineForOffset(end);
    //                int segStart = s.getLayout().getLineStart(line1);
   //                 int segEnd = SM.findLine(line3);
  //                  int segStart = s.getLayout().getLineStart( SM.getLowerLine(i) - SM.getUpperLine(i) );
                   CharSequence text = TextUtils.concat(t.getText().subSequence(0,start),
                            TM.getContent(i),
                            t.getText().subSequence(start,t.length()));

 /*                  CharSequence text2 = TextUtils.concat(s.getText().subSequence(0,segStart),
                            SM.getContent(i),
                          s.getText().subSequence(segStart,s.length() ) );
 */
                    t.setText(text);
  //                  s.setText(text2);
                    TM.setContent(i,t.getText().subSequence(start, end));
      //              SM.setContent(i,s.getText().subSequence(segStart , segEnd));
                    break;
            }


    }


    @Override
    public void onClick(View v) {
        int[] btnIds = SM.segmentBtns;
        for(int i=0;i<btnIds.length;i++)
            if (v.getId() == btnIds[i]) {
                ImageButton ib = (ImageButton) findViewById(v.getId() );
                SM2.add(i,ib);
                break;
            }
        }

}



/*
        final SpannableStringBuilder sb = new SpannableStringBuilder();
        TextView tv = t;
        BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(),bd.getIntrinsicHeight());

        sb.append("123123");
        sb.setSpan(new ImageSpan(bd), 0, 15,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t.setText(sb);
*/



/*
                    else{
                        e.replace(valStart,valEnd,newValue);
                        t.setText(t.getText());

                        SpannableString sp = new SpannableString(t.getText());
                        ClickableSpan[] spans = sp.getSpans(0, t.getText().length(),ClickableSpan.class);

                        for (ClickableSpan span:spans) {
                            int spStart = sp.getSpanStart(span);
                            int spEnd = sp.getSpanEnd(span);
                            int ln = layout.getLineForOffset(spStart);
                            int lineStart =  layout.getLineStart(ln);
                            int offsetStart = spStart -lineStart;
                            int offsetEnd = spEnd - lineStart;
                            if(mCallback.offset>=offsetStart && mCallback.offset<=offsetEnd){
                                e.insert(spStart,"123");
                            }

                           // Toast.makeText(getApplicationContext(),"LineStart:"+offsetStart+"offsetEnd"+offsetEnd, Toast.LENGTH_LONG).show();
                            //sp.removeSpan(span);
                        }
                        t.setText(sp);
                    }*/

/*
        Drawable myIcon = getApplicationContext().getResources().getDrawable(R.drawable.table_cell_border);
        myIcon.setBounds(0, 0, 150,t.getLineHeight());
        ssb.setSpan(new ImageSpan(myIcon, ImageSpan.ALIGN_BASELINE), 0, ssb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        Spannable wordtoSpan = new SpannableString(" ");
        ssb.append(wordtoSpan);
        wordtoSpan.setSpan(new TypefaceSpan("serif"), 0, wordtoSpan.length(), 0);
        */

      /*
                       newValue = newValue.replaceAll("\\.0*$", "");
                       if (newValue.charAt(0) == '.') newValue = "0" + newValue;



                        if (newValue.contains(".") && newValue.length() - newValue.indexOf(".") > 4)
                                newValue = newValue.substring(0, newValue.indexOf(".") + 5);
*/