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
import android.graphics.Xfermode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View{

    int width, height;

    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    Path path;
    float oldX, oldY;

    int color=Color.BLACK;
    boolean isPenEraser=true, isFillPaint=false, isSpoid=false;
    boolean isEmptySpace=false;


    public CanvasView(Context context, Rect rect) {
        super(context);
        width=rect.width();
        height=rect.height();
        bitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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
                if(isFillPaint) {
                    int pixel=bitmap.getPixel((int)x, (int)y);
                    checkcolor=Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                    paint.setStrokeWidth(5);
                    fillPaint((int)x, (int)y);
                }
                if(isSpoid){
                    int pixel=bitmap.getPixel((int)x, (int)y);

                    if(Color.alpha(pixel)!=0) {
                        color=Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                    }else isEmptySpace=true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isPenEraser) {
                    path.quadTo(oldX, oldY, x, y);
                    oldX=x;
                    oldY=y;
                    canvas.drawPath(path, paint);
                }
                break;
            case MotionEvent.ACTION_UP:
                return false;
        }
        invalidate();
        return true;
    }

    void setPen(){
        paint.setXfermode(null);
        paint.setStyle(Paint.Style.STROKE);
        isPenEraser=true;
        isFillPaint=false;
        isSpoid=false;
    }
    void setPenSize(int penPixel){
        paint.setStrokeWidth(penPixel);
    }

    void setEraser(){
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        isPenEraser=true;
        isFillPaint=false;
        isSpoid=false;
    }

    void setPenColor(int color){
        this.color=color;
        paint.setColor(color);
    }

    void setFillPaint(){
        isFillPaint=true;
        isPenEraser=false;
        isSpoid=false;
    }
    int checkcolor;
    int direction;
    void fillPaint(int x, int y){
        Log.e("CTAG", x+", "+y);
        if(x<=0 || y<=0 || x>=width || y>=height) return;

        int pixel=bitmap.getPixel(x, y);
        int nextColor=Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
        canvas.drawPoint(x, y, paint);
        if(checkcolor==nextColor && x>=0 && y>=0 && x<=width && y<=height){
            while (direction<=3){
                switch (direction){
                    case 0:
                        x-=2;
                        fillPaint(x, y);
                        break;
                    case 1:
                        y-=2;
                        fillPaint(x, y);
                        break;
                    case 2:
                        x+=2;
                        fillPaint(x, y);
                        break;
                    case 3:
                        y+=2;
                        fillPaint(x, y);
                        break;
                }
                direction++;
            }
            direction=0;
        }
    }

    void setSpoidColor(){
        isSpoid=true;
    }
    int getSpoidColor(){
        return color;
    }
    public boolean getIsEmptySpace() {return isEmptySpace;}
    public void setIsEmptySpace() {isEmptySpace = false;}


}
