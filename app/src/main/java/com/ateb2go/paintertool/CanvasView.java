package com.ateb2go.paintertool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View{

    Bitmap bitmap;
    Canvas canvas;
    Paint paint=null;
    Path path;
    float oldX, oldY;

    public CanvasView(Context context, Rect rect) {
        super(context);
        bitmap=Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        path=new Path();

        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setDither(true);
        paint.setStrokeWidth(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        if(bitmap!=null) bitmap.recycle();
        bitmap=null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap!=null){
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX();
        float y=event.getY();

        switch (event.getAction()&MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                oldX=x;
                oldY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(oldX, oldY, x, y);
                oldX=x;
                oldY=y;
                canvas.drawPath(path, paint);
                invalidate();
                break;
        }
        return true;
    }

    void setPaintColor(int color){
        paint.setColor(color);
    }

    void setPen(){
        paint.setXfermode(null);
    }

    void setEraser(){
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
}
