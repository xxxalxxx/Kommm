package com.yahoo.melnichuk.a.kom;

/**
 * Created by xxxalxxx on 19.01.2015.
 */
public class ColourHandler {
    int A, R,G,B,hex;
    int tempA,tempR,tempG,tempB,tempHex;
    ColourHandler(int hex){
        this.hex = hex;
        A = ( hex>>24 ) & 0xFF;
        R = ( hex>>16 ) & 0xFF;
        G = ( hex>>8 ) & 0xFF;
        B = hex  & 0xFF;
        tempA = A;
        tempR=R;
        tempG=G;
        tempB=B;
        tempHex = hex;
    }
    int form(){
        return  (A <<24)|(R<<16)|(G<<8)|B;
    }
    void reset(){
        int temp = tempHex;
        hex = temp;
    }
    void decrement(){
        A--;
        R--;
        G--;
        B--;
    }
    void decrement(int i){
        A-=i;
        R-=i;
        G-=i;
        B-=i;
    }

}
