package com.ateb2go.paintertool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;


public class DrawActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;

    Context context=this;
    DrawView drawView;

    int width, height;
    int sWidth, sHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_draw);
        sWidth=getWindowManager().getDefaultDisplay().getWidth();
        sHeight=getWindowManager().getDefaultDisplay().getHeight();

        Intent intent=getIntent();
        width=Integer.parseInt(intent.getStringExtra("width"));
        height=Integer.parseInt(intent.getStringExtra("height"));


        relativeLayout=new RelativeLayout(this);

        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width*2+sWidth, height*2+sHeight);
        relativeLayout.setBackgroundColor(getResources().getColor(R.color.darkgray));
        relativeLayout.setLayoutParams(layoutParams);
        setContentView(relativeLayout);//배경 생성


        drawView=new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        SurfaceHolder surfaceHolder=drawView.getHolder();

        if(width>sWidth||height>sHeight){
            surfaceHolder.setFixedSize(sWidth, sHeight);
            width=sWidth; height=sHeight;
        }else surfaceHolder.setFixedSize(width, height);//임시로 width height가 화면크기를 넘기면 화면크기로 자동조정(줌 기능 만들기 전까지)
        relativeLayout.addView(drawView);//그리는 view 배경에 붙임

        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) drawView.getLayoutParams();
        params.leftMargin=sWidth/2-width/2;
        params.topMargin=sHeight/2-height/2;
        drawView.setLayoutParams(params);//그리는 view 가운데로 옮기기

    }

    @Override
    protected void onPause() {
        super.onPause();
        drawView.drawThread.stopThread();
    }
}
