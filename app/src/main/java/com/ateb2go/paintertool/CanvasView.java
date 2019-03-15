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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class CanvasView extends android.support.v7.widget.AppCompatImageView {

    DrawActivity drawActivity;

    int width, height;

    Bitmap bitmap;
    ArrayList<Bitmap> bitmaps=new ArrayList<>();
    int taskCount=0;
    Canvas canvas;
    Paint paint;
    Path path;
    float oldX, oldY;

    int color = Color.BLACK;
    boolean isPenEraser = true, isFillPaint = false, isSpoid = false;
    boolean isEmptySpace = false;

    int checkcolor;
    QueueLinearFloodFiller floodFiller;

    int layerNum;
    ArrayList<Bitmap> layers=new ArrayList<>();

    public CanvasView(Context context, Rect rect) {
        super(context);
        drawActivity=(DrawActivity) context;
        width = rect.width();
        height = rect.height();
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmaps.add(Bitmap.createBitmap(bitmap));
        canvas = new Canvas(bitmap);
        path = new Path();

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setDither(true);

        paint.setStrokeWidth(10);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(false);

        floodFiller=new QueueLinearFloodFiller(bitmap, Color.BLACK, Color.WHITE);
        floodFiller.floodFill(2, 2);

    }

    @Override
    protected void onDetachedFromWindow() {
        if (bitmap != null) bitmap.recycle();
        bitmap = null;
        bitmaps.clear();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getPointerCount()==2){
            bitmap=Bitmap.createBitmap(bitmaps.get(taskCount));
            makeCanvas();
            invalidate();
            return false;
        }
        return super.dispatchTouchEvent(event);
    }

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

                    floodFiller=new QueueLinearFloodFiller(bitmap, checkcolor, color);
                    floodFiller.floodFill((int)x, (int)y);
                }
                if (isSpoid) {
                    int pixel = bitmap.getPixel((int) x, (int) y);

                    if (Color.alpha(pixel) != 0) {
                        color = Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                    } else isEmptySpace = true;
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
                if(isSpoid && !isEmptySpace) {
                    drawActivity.getDropperColor(color);
                }
                isEmptySpace=false;

                if(taskCount>20){
                    taskCount=20;
                    bitmaps.remove(0);
                }
                if(bitmaps.size()-1>taskCount){
                    for(int i=bitmaps.size()-1;i>taskCount;i--){
                        bitmaps.remove(i);
                    }
                }
                taskCount++;
                bitmaps.add(Bitmap.createBitmap(bitmap));
                invalidate();
                drawActivity.layerImageChange(bitmap, layerNum);
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
        isPenEraser=false;
        isFillPaint=false;
    }

    void doUndo(){
        taskCount--;
        if(taskCount<0){
            taskCount=0;
            return;
        }
        bitmap=Bitmap.createBitmap(bitmaps.get(taskCount));
        makeCanvas();
        invalidate();
        drawActivity.layerImageChange(bitmap, layerNum);
    }
    void doRedo(){
        taskCount++;
        if(taskCount>bitmaps.size()-1){
            taskCount=bitmaps.size()-1;
            return;
        }
        bitmap=Bitmap.createBitmap(bitmaps.get(taskCount));
        makeCanvas();
        invalidate();
        drawActivity.layerImageChange(bitmap, layerNum);
    }
    void makeCanvas(){
        canvas=new Canvas(bitmap);
    }

    public boolean getIsEmptySpace() {
        return isEmptySpace;
    }

    public void setIsEmptySpace() {
        isEmptySpace = false;
    }


    Bitmap getnBitmap(){
        return bitmap;
    }

    void getLayerCount(int layerNum){
        this.layerNum=layerNum;
    }

    void layerChange(){

    }



}
