package com.yahoo.melnichuk.a.kom;

import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by xxxalxxx on 23.01.2015.
 */
public class Logic {
    TextView t;
    int tID;
    SpannableString sp;
    TableManager DM;
    Editable e;

    Logic(TableManager DM){
        this.DM = DM;
    }
//    Table table = TM.data.get(0);

    void setText(TextView text){
        t= text;
        e= (Editable) t.getText();
        sp = new SpannableString(t.getText());
    }

    void setID(int ID){
        tID = ID;
    }

    ClickableSpan[] getSpans(){
        return sp.getSpans(DM.tStart(tID),DM.tEnd(tID),ClickableSpan.class);
    }
    void doLogic(){
        switch (tID){
            case 0:communalLogic();
                break;
            case 1:coldWaterLogic();
                break;
            case 2:stdLogic();
                break;
            case 3:stdLogic();
                break;
            case 4:heatingLogic();
                break;
            case 5:electricityLogic();
                break;
            case 6:phoneLogic();
                break;
            default: break;
        }

    }
//TODO: 1. Иногда главній тред не считает достаточно быстро, чтобы
    //TODO:2. Логика в зависимости от индекса


    void communalLogic(){
        new Thread(new Runnable() {

            public void run() {
                final int[][] bounds = getWordBounds(getSpans());
                BigDecimal[] cell = getVals(bounds);
                BigDecimal expr_row1 = cell[0].add(cell[1]).subtract(cell[2]).subtract(cell[3]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row2 = cell[5].add(cell[6]).subtract(cell[7]).subtract(cell[8]).setScale(2, RoundingMode.HALF_EVEN);
                final String result_row1 = emptify(expr_row1,bounds[4][2]);
                final String result_row2 = emptify(expr_row2,bounds[9][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[4][0], bounds[4][1], result_row1);
                        e.replace(bounds[9][0], bounds[9][1], result_row2);
                    }
                });
            }
        }).start();
    }


    void coldWaterLogic(){
        new Thread(new Runnable() {

            public void run() {
                final int[][] bounds = getWordBounds(getSpans());
                BigDecimal[] cell = getVals(bounds);
                BigDecimal expr_diff = cell[0].subtract(cell[1]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_calc = expr_diff.multiply(cell[3]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row1 = expr_calc.add(cell[5]).subtract(cell[6]).subtract(cell[7]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row2 = cell[9].add(cell[10]).subtract(cell[11]).subtract(cell[12]).setScale(2, RoundingMode.HALF_EVEN);

                final String result_diff = emptify(expr_diff,bounds[2][2]);
                final String result_calc = emptify(expr_calc,bounds[4][2]);
                final String result_row1 = emptify(expr_row1,bounds[8][2]);
                final String result_row2 = emptify(expr_row2,bounds[13][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[2][0], bounds[2][1], result_diff);
                        e.replace(bounds[4][0], bounds[4][1], result_calc);
                        e.replace(bounds[8][0], bounds[8][1], result_row1);
                        e.replace(bounds[13][0], bounds[13][1], result_row2);

                    }
                });
            }
        }).start();
    }

    void stdLogic(){
        new Thread(new Runnable() {

            public void run() {
                final ClickableSpan[] spans = getSpans();
                final int[][] bounds = getWordBounds(spans);
                BigDecimal[] cell = getVals(bounds);
                BigDecimal expr_diff = cell[0].subtract(cell[1]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_calc = expr_diff.multiply(cell[3]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row = expr_calc.add(cell[5]).subtract(cell[6]).subtract(cell[7]).setScale(2, RoundingMode.HALF_EVEN);

                final String result_diff = emptify(expr_diff,bounds[2][2]);
                final String result_calc = emptify(expr_calc,bounds[4][2]);
                final String result_row = emptify(expr_row,bounds[8][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[2][0], bounds[2][1], result_diff);
                        e.replace(bounds[4][0], bounds[4][1], result_calc);
                        e.replace(bounds[8][0], bounds[8][1], result_row);
                    }
                });
            }
        }).start();
    }
    void heatingLogic(){
        new Thread(new Runnable() {

            public void run() {
                final ClickableSpan[] spans = getSpans();
                final int[][] bounds = getWordBounds(spans);
                BigDecimal[] cell = getVals(bounds);

                BigDecimal expr_row = cell[0].add(cell[1]).subtract(cell[2]).subtract(cell[3]).setScale(2, RoundingMode.HALF_EVEN);

                final String result_row = emptify(expr_row,bounds[4][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[4][0], bounds[4][1], result_row);
                    }
                });
            }
        }).start();
    }
    void electricityLogic(){
        new Thread(new Runnable() {

            public void run() {
                final ClickableSpan[] spans = getSpans();
                final int[][] bounds = getWordBounds(spans);
                BigDecimal[] cell = getVals(bounds);
                BigDecimal expr_diff = cell[0].subtract(cell[1]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row1 = cell[3].multiply(cell[4]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row2 = cell[6].multiply(cell[7]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row3 = cell[9].multiply(cell[10]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_row4 = cell[12].multiply(cell[13]).setScale(2, RoundingMode.HALF_EVEN);

                final String result_diff = emptify(expr_diff,bounds[2][2]);
                final String result_row1 = emptify(expr_row1,bounds[5][2]);
                final String result_row2 = emptify(expr_row2,bounds[8][2]);
                final String result_row3 = emptify(expr_row3,bounds[11][2]);
                final String result_row4 = emptify(expr_row4,bounds[14][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[2][0], bounds[2][1], result_diff);
                        e.replace(bounds[5][0], bounds[5][1], result_row1);
                        e.replace(bounds[8][0], bounds[8][1], result_row2);
                        e.replace(bounds[11][0], bounds[11][1], result_row3);
                        e.replace(bounds[14][0], bounds[14][1], result_row4);
                    }
                });
            }
        }).start();
    }

    void phoneLogic(){
        new Thread(new Runnable() {

            public void run() {
                final ClickableSpan[] spans = getSpans();
                final int[][] bounds = getWordBounds(spans);
                BigDecimal[] cell = getVals(bounds);
                BigDecimal tax = new BigDecimal("0.20");

                BigDecimal expr_teltax = cell[0].multiply(tax).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_radiotax = cell[2].multiply(tax).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_telradio = cell[0].add(cell[1]).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_totaltax = expr_teltax.add(expr_radiotax).setScale(2, RoundingMode.HALF_EVEN);
                BigDecimal expr_total = expr_teltax.add(expr_radiotax).setScale(2, RoundingMode.HALF_EVEN);



                final String result_teltax = emptify(expr_teltax,bounds[1][2]);
                final String result_radiotax = emptify(expr_radiotax,bounds[3][2]);
                final String result_telradio =  emptify(expr_telradio,bounds[4][2]);
                final String result_totaltax =  emptify(expr_totaltax,bounds[5][2]);
                final String result_total =  emptify(expr_total,bounds[6][2]);

                t.post(new Runnable() {
                    public void run() {
                        e.replace(bounds[1][0], bounds[1][1], result_teltax);
                        e.replace(bounds[3][0], bounds[3][1], result_radiotax);
                        e.replace(bounds[4][0], bounds[4][1], result_telradio);
                        e.replace(bounds[5][0], bounds[5][1], result_totaltax);
                        e.replace(bounds[6][0], bounds[6][1], result_total);
                    }
                });
            }
        }).start();
    }


    int [][] getWordBounds(ClickableSpan[] s){
        int len = s.length;
        int[][] wordBounds = new int[len][3];

        for(int i=0;i<len;i++){
            wordBounds[i][0]= sp.getSpanStart(s[i]);
            wordBounds[i][1]= sp.getSpanEnd(s[i]);
            wordBounds[i][2]= wordBounds[i][1]- wordBounds[i][0];
        }
        return wordBounds;
    }

    BigDecimal[] getVals(int[][] wb){
        int len  = wb.length;
        BigDecimal[] vals = new BigDecimal[len];
        for(int i=0;i<len;i++){
            vals[i] = new BigDecimal(t.getText().subSequence(wb[i][0],wb[i][1]).toString().trim() );
        }
        return vals;
    }


    String emptify(BigDecimal to_fill,int filled_len){
        String emptySpace = "";
        int difference = Math.abs(to_fill.toString().length()-filled_len);
        for (int i = 0; i < difference; i++)
            emptySpace += " ";
        return emptySpace + to_fill;
    }




}
