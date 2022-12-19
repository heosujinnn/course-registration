package com.soojin.myfirsttest;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public class Pop extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop); //pop이라는 레이아웃을 띄어주라.

        //화면 크기 조정을 위한 displaymetrics
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); //초기 셋팅

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*0.9),(int)(height*0.85)); //화면의 90퍼 센트의 가로 85퍼의 세로 크기
    }
}
