package com.example.dell.attendancesystem.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class TestAlgo {
    public static void main(String[] a) {

        abc b = new abc();
        b.Fistalfo();
    }

}
    class abc{

        public void Fistalfo(){
            ArrayList<String> arrayList = new ArrayList<>();
            String test = "It's the number 1 way ahahah ashir hahah";
            Scanner tokenize = new Scanner(test);
            while (tokenize.hasNext()) {
                arrayList.add(tokenize.next());
            }
            for(int i=arrayList.size();i>0;i--){
                System.out.println(arrayList.get(i-1));
            }
    }

}





