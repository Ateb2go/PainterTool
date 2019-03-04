package com.ateb2go.paintertool;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DrawActivity extends AppCompatActivity {

    CanvasView cv;

    ImageView pen, paint, eraser, palette, spoid, undo, redo, save;

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        relativeLayout=findViewById(R.id.relativeLayout);
        pen=findViewById(R.id.pen);
        paint=findViewById(R.id.paint);
        eraser=findViewById(R.id.eraser);
        palette=findViewById(R.id.palette);
        spoid=findViewById(R.id.spoid);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        save=findViewById(R.id.save);

        Intent intent=getIntent();
        int width=Integer.parseInt(intent.getStringExtra("width"));
        int height=Integer.parseInt(intent.getStringExtra("height"));

        Rect rect=new Rect(0, 0, width, height);

        cv=new CanvasView(this, rect);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        cv.setLayoutParams(layoutParams);
        cv.setBackgroundColor(Color.WHITE);
        relativeLayout.addView(cv);
    }

    public void clickIcons(View view) {
        switch (view.getId()){
            case R.id.pen:
                cv.setPen();
                eraser.setBackgroundColor(Color.TRANSPARENT);
                pen.setBackgroundColor(Color.RED);
                break;
            case R.id.paint: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show(); break;
            case R.id.eraser:
                cv.setEraser();
                pen.setBackgroundColor(Color.TRANSPARENT);
                eraser.setBackgroundColor(Color.RED);
                break;
            case R.id.palette: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;
            case R.id.spoid: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;
            case R.id.undo: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;
            case R.id.redo: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;
            case R.id.save: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;
        }
    }
}
