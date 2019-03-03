package com.ateb2go.paintertool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrawActivity extends AppCompatActivity {

    CanvasView cv;

    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        frameLayout=findViewById(R.id.framelayout);

        Intent intent=getIntent();
        int width=Integer.parseInt(intent.getStringExtra("width"));
        int height=Integer.parseInt(intent.getStringExtra("height"));

        Rect rect=new Rect(0, 0, width, height);

        cv=new CanvasView(this, rect);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(width, height);
        layoutParams.gravity=Gravity.CENTER;
        cv.setLayoutParams(layoutParams);
        cv.setBackgroundColor(Color.WHITE);
        frameLayout.addView(cv);


    }

}
