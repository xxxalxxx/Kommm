package com.yahoo.melnichuk.a.kom;

import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by xxxalxxx on 19.01.2015.
 */
public class TableManager {

    List<Integer> charsPerRow = new ArrayList<>();
    Queue<String> emptySpaceQueue = new LinkedList<>();
    int maxCharsPerRow = 0;
    int maxRows = 0;

    int[]checkBoxIds = {
            R.id.communalCheckBox,
            R.id.coldWaterCheckBox,
            R.id.wasteWaterCheckBox,
            R.id.hotWaterCheckBox,
            R.id.heatingCheckBox,
            R.id.electricityCheckBox,
            R.id.phoneCheckBox
    };

    int[]emptyIds = {
            R.id.communalEmpty,
            R.id.coldWaterEmpty,
            R.id.wasteWaterEmpty,
            R.id.hotWaterEmpty,
            R.id.heatingEmpty,
            R.id.electricityEmpty,
            R.id.phoneEmpty
    };


  List<Table> data = new ArrayList<>();

    String[] names = {"communal", "coldWater","wasteWater", "hotWater", "heating", "electricity", "phone"};


    String[][] ROWS ={
            {" "},
            {" "},
            {" "},
            {" "},
            {"\t "},
            {"Стаття нарахувань","Нараховано","Перерах-к","Субсидія","Компенс.","До сплати"},
            {"Утримання будинків","0.00","0.00","0.00","0.00","0.00"},
            {"та прибуд. тер.","","","","",""},
            {"Газ","0.00","0.00","0.00","0.00","0.00"},
            {"","","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            {"Показання лічильників, м3"},
            {" поточні "," попередні "," різниця "," Тариф "," Нараховано "," Перерах-к "," Субсидія "," Компенс. "," До сплати "},
            {"0.00","0.00","0.00","7.464","0.00","0.00","0.00","0.00","0.00"},
            {"","","","Спожито:","0.00","0.00","0.00","0.00","0.00"},
            {"","","","","","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            {"Показання лічильників, м3"},
            {" поточні "," попередні "," різниця "," Тариф "," Нараховано "," Перерах-к "," Субсидія "," Компенс. "," До сплати "},
            {"0.00","0.00","0.00","7.464","0.00","0.00","0.00","0.00","0.00"},
            {"","","","","","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            {"Показання лічильників, м3"},
            {" поточні "," попередні "," різниця "," Тариф "," Нараховано "," Перерах-к "," Субсидія "," Компенс. "," До сплати "},
            {"0.00","0.00","0.00","7.464","0.00","0.00","0.00","0.00","0.00"},
            {"","","","","","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            {"Показання лічильників, м3"},
            {"Нараховано","Перерах-к","Субсидія","Компенс.","До сплати"},
            {"0.00","0.00","0.00","0.00","0.00"},
            {"","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            {"Поточні показання,","Попередні показання,","Спожито,","",""},
            {"кВтг","кВтг","кВтг","Тариф, грн","До сплати"},
            {"0.00","0.00","0.00","",""},
            {" ","пільгові,кВтг","0.00","0.00","0.00"},
            {" ","до 150/250 кВтг","0.00","0.3084","0.00"},
            {" ","від 150/250 до 800 кВтг","0.00","0.4194","0.00"},
            {" ","понад 800 кВтг","0.00","0.00","0.00"},
            {"","","","Всього:","0.00"},
            {"\f "},
            {" "},
            {"\t "},
            //{"Пільговий тариф?", "Тариф до 150/250 кВтг", "Тариф від 150/250 до 800 кВтг","Тариф понад 800 кВтг","До сплати"},
            {"Нараховано за місяць грн" , "без ПДВ" , "ПДВ 20%" , "Разом"},
            {"Абонплата за ТП", "33.26" , "0.00" , ""},
            {"Абонплата за користування радіоточкою","10.00","0.00",""},
            {"Всього нараховано за послуги, грн","43.26","8.65","0.00"},
            {"\f "},
    };

    TableManager() {


        int max = 0;
        int temp = 0;
        int maxRows = 0;

        for (int i = 0; i < ROWS.length; i++) {
            for (int j = 0; j < ROWS[i].length; j++) {
                if (i > 0 && ROWS[i - 1].length == ROWS[i].length) {
                    if (ROWS[i][j].length() < ROWS[i - 1][j].length()) {
                        if (ROWS[i][j].matches("\\d+"))
                            ROWS[i][j] = String.format("%-" + ROWS[i - 1][j].length() + "s", ROWS[i][j]);
                        else
                            ROWS[i][j] = String.format("%" + ROWS[i - 1][j].length() + "s", ROWS[i][j]);

                    } else {
                        int k = i;
                        while (ROWS[i].length == ROWS[--k].length && k > 0 && ROWS[i].length > 1) {
                            if (ROWS[k][j].matches("\\d+"))
                                ROWS[k][j] = String.format("%-" + ROWS[i][j].length() + "s", ROWS[k][j]);
                            else
                                ROWS[k][j] = String.format("%" + ROWS[i][j].length() + "s", ROWS[k][j]);

                        }

                    }
                }
                max += ROWS[i][j].length();

            }
            if (ROWS[i].length > maxRows) maxRows = ROWS[i].length;
            this.maxRows = maxRows;
            if (temp < max) temp = max;
            charsPerRow.add(i, max);
            max = 0;
            maxCharsPerRow = temp;
            int iter = i;
            while (iter > -1 && charsPerRow.get(iter) > 2)
                charsPerRow.set(iter--, charsPerRow.get(i));

        }

        boolean flag = true;
        for (int i = 0; i < ROWS.length; i++) {
            int j = ROWS[i].length;
            while (j != 1 && emptySpaceQueue.size() < (maxCharsPerRow - charsPerRow.get(i)/*+(maxRows - ROWS[i].length)*/) )
                emptySpaceQueue.add(" ");
            while (!emptySpaceQueue.isEmpty()) {
                if (ROWS[i][--j].matches(".*\\d+.*") && !ROWS[i][j].matches(".*\\p{L}+.*"))
                    ROWS[i][j] = emptySpaceQueue.poll() + ROWS[i][j];
                else{
                    if(flag)
                        ROWS[i][j] += emptySpaceQueue.poll();
                    else ROWS[i][j] = emptySpaceQueue.poll() + ROWS[i][j];
                }

                if (j == 0 && emptySpaceQueue.size() > 0){
                    j = ROWS[i].length;
                    flag =!flag;
                }
            }
        }

    }
    int tStart(int tID){
        return data.get(tID).bounds[0];
    }
    int tEnd(int tID){
        return data.get(tID).bounds[1];
    }
    void setContent(int tID,CharSequence c){
        int len = c.length();
        Table t =  data.get(tID);
        t.content=c;
        t.spans = new SpannableString(c).getSpans(0, len, ClickableSpan.class);
        t.spanLen = t.spans.length;

    }



    CharSequence getContent(int tID){
        return data.get(tID).content;
    }



    int tLen(int tID){
        return data.get(tID).len;
    }
    int sLen(int tID){
        return data.get(tID).spanLen;
    }

    void check(int tID){
        data.get(tID).checked=true;
    }
    void uncheck(int tID){
        data.get(tID).checked=false;
    }
    Table get(int tID){
        return data.get(tID);
    }

    int correlate(int tID){
        int len = data.size();
       while(++tID < len)
           if(data.get(tID).checked)
               return tID;

       return tID;
    }

    int format(int tID,int spPivot) {

        switch (tID) {
            case 1:
                if (spPivot == 3) return 3;
            case 2:
                if (spPivot == 3) return 3;
            case 3:
                if (spPivot == 3) return 3;
            case 5:
                if (spPivot == 4 || spPivot == 7 || spPivot == 10 || spPivot == 13) return 4;
            default: return 2;
        }
    }



}

