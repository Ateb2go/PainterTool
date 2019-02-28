package com.ateb2go.paintertool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    Context context;
    int width, height;

    DrawThread drawThread;

    public DrawView(Context context) {
        super(context);
        this.context=context;
        holder=getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(drawThread==null){
            drawThread=new DrawThread();
            drawThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width=width; this.height=height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        drawThread.stopThread();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x=(int)event.getX();
        int y=(int)event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                drawThread.touchDM(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawThread.touchDM(x, y);
                break;
            case MotionEvent.ACTION_UP:
                drawThread.touchUp();
                break;
        }

        return true;
    }

    class DrawThread extends Thread{

        boolean isRun=true;
        boolean isWait=false;

        Bitmap img;


        int x=250, y=250;//터치된 좌표

        @Override
        public void run() {

            Canvas canvas= null;
            while (isRun){
                canvas=holder.lockCanvas();
                try {
                    synchronized (holder){
                        Log.i("TAG","aaaa"+canvas);
                        Paint paint=new Paint();
                        paint.setColor(Color.BLACK);
                        canvas.drawRect(10, 10, 20, 25, paint);
                        //drawCV(canvas);
                    }
                }finally {
                    if(canvas!=null) holder.unlockCanvasAndPost(canvas);
                }
            }

//            synchronized (this){
//                if(isWait){
//                    try {
//                        wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }

        public void stopThread(){
            isRun=false;
            synchronized (this){this.notify();}
        }
        void pauseThread(){isWait=true;}
        void resumeThread(){
            isWait=false;
            synchronized (this){this.notify();}
        }

        void touchDM(int x, int y){
            this.x=x;
            this.y=y;
        }
        void touchUp(){
            isRun=false;
        }

        void drawCV(Canvas canvas){
            Paint paint=new Paint();
            paint.setColor(Color.BLACK);
            canvas.drawRect(10, 10, 20, 25, paint);
        }

    }//DrawThread
}
