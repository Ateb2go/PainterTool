package com.ateb2go.paintertool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.Queue;

public class CanvasView extends android.support.v7.widget.AppCompatImageView {

    int width, height;

    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    Path path;
    float oldX, oldY;

    Point point = new Point();

    int color = Color.BLACK;
    boolean isPenEraser = true, isFillPaint = false, isSpoid = false;
    boolean isEmptySpace = false;

    int checkcolor;
    QueueLinearFloodFiller floodFiller;

    public CanvasView(Context context, Rect rect) {
        super(context);
        width = rect.width();
        height = rect.height();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        path = new Path();

        paint = new Paint();
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
        if (bitmap != null) bitmap.recycle();
        bitmap = null;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }


    //dispatchtouchevent로 이벤트가 2개가 되는 순간 기존에 그리던 화면으로 돌아가기


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(x, y);
                oldX = x;
                oldY = y;
                if (isFillPaint) {
                    checkcolor = bitmap.getPixel((int) x, (int) y);
                    if(floodFiller==null)floodFiller=new QueueLinearFloodFiller(bitmap, checkcolor, color);
                    else{
                        floodFiller.setTargetColor(checkcolor);
                        floodFiller.setFillColor(color);
                    }
                    floodFiller.floodFill((int)x, (int)y);
                }
                if (isSpoid) {
                    int pixel = bitmap.getPixel((int) x, (int) y);

                    if (Color.alpha(pixel) != 0) {
                        color = Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                    } else isEmptySpace = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPenEraser) {
                    path.quadTo(oldX, oldY, x, y);
                    oldX = x;
                    oldY = y;
                    canvas.drawPath(path, paint);
                }
                break;
            case MotionEvent.ACTION_UP:
                return false;
        }
        invalidate();
        return true;
    }

    void setPen() {
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        isPenEraser = true;
        isFillPaint = false;
        isSpoid = false;
    }

    void setPenSize(int penPixel) {
        paint.setStrokeWidth(penPixel);
    }

    void setEraser() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        isPenEraser = true;
        isFillPaint = false;
        isSpoid = false;
    }

    void setPenColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    void setFillPaint() {
        isFillPaint = true;
        isPenEraser = false;
        isSpoid = false;
    }



    void setSpoidColor() {
        isSpoid = true;
    }

    int getSpoidColor() {
        return color;
    }

    public boolean getIsEmptySpace() {
        return isEmptySpace;
    }

    public void setIsEmptySpace() {
        isEmptySpace = false;
    }






}
