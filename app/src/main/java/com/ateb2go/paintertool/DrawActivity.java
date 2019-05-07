package com.ateb2go.paintertool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawActivity extends AppCompatActivity {

    CanvasView cv;

    //상단 아이콘
    HorizontalScrollView hsv;
    ImageView pen, fillPaint, eraser, palette, spoid, undo, redo, save;
    boolean isPaletteSelected=false;
    boolean isPenSizeSelected=false;
    boolean isSpoidSelected=false;
    boolean isLayerSelected=false;


    //펜사이즈
    LinearLayout penSize;
    TextView tvPenSize;
    SeekBar seekBar;
    int penpixel=10;

    //팔레트
    LinearLayout colorChange;
    ImageView prevcolor;
    EditText etR, etG, etB;
    ImageView[] colorStocks=new ImageView[5];
    CircleImageView frame, spectrum;
    RelativeLayout spectrumbox;
    boolean isSpbox=true;
    int red, blue, green;
    int color, spoidColor;
    InputMethodManager imm;


    RelativeLayout relativeLayout;

    int sWidth, sHeight;
    CustomZoomView zoomView;

    LinearLayout layerBox;
    FloatingActionButton fab;
    int layerNum;
    LinearLayout layerLayout;
    ArrayList<ImageView> layerArray=new ArrayList<>();
    LinearLayout.LayoutParams layerParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        relativeLayout=findViewById(R.id.relativeLayout);
        hsv=findViewById(R.id.hsv);
        hsv.bringToFront();
        //아이콘 참조
        pen=findViewById(R.id.pen);
        fillPaint=findViewById(R.id.fillPaint);
        eraser=findViewById(R.id.eraser);
        palette=findViewById(R.id.palette);
        spoid=findViewById(R.id.spoid);
        undo=findViewById(R.id.undo);
        redo=findViewById(R.id.redo);
        save=findViewById(R.id.save);

        //펜 크기 변경 참조
        penSize=findViewById(R.id.pensize);
        tvPenSize=findViewById(R.id.pensize_tv);
        seekBar=findViewById(R.id.pensize_seekbar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        //색변경 레이아웃 참조
        colorChange=findViewById(R.id.colorchange);
        prevcolor=findViewById(R.id.prevcolor);
        prevcolor.setOnTouchListener(onTouchListener);
        etR=findViewById(R.id.et_color_r);
        etG=findViewById(R.id.et_color_g);
        etB=findViewById(R.id.et_color_b);
        etR.addTextChangedListener(watcherR);
        etG.addTextChangedListener(watcherG);
        etB.addTextChangedListener(watcherB);
        for(int i=0;i<5;i++) {
            colorStocks[i]=findViewById(R.id.colorstock1+i);
            colorStocks[i].setTag(Color.WHITE);
            if(i==4)colorStocks[i].setTag(Color.BLACK);
            colorStocks[i].setOnLongClickListener(longClickListener);
        }
        frame=findViewById(R.id.spectrumframe);
        spectrum=findViewById(R.id.colorspectrum);
        spectrum.setOnTouchListener(onTouchListener);
        spectrumbox=findViewById(R.id.spectrumbox);
        imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        Intent intent=getIntent();
        int width=Integer.parseInt(intent.getStringExtra("width"));
        int height=Integer.parseInt(intent.getStringExtra("height"));

        Rect rect=new Rect(0, 0, width, height);


        zoomView=new CustomZoomView(this);
        RelativeLayout.LayoutParams fLayoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        fLayoutParams.addRule(RelativeLayout.BELOW, R.id.hsv);
        zoomView.setLayoutParams(fLayoutParams);
        relativeLayout.addView(zoomView);
        zoomView.setMaxZoom(4f);


        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        sWidth=point.x;
        sHeight=point.y;

        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams((int)(width), (int)(height));

        FrameLayout inner=new FrameLayout(this);
        layoutParams.gravity=Gravity.CENTER;
        inner.setLayoutParams(layoutParams);
        zoomView.addView(inner);

        cv=new CanvasView(this, rect);
        layoutParams=new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        cv.setLayoutParams(layoutParams);
        cv.setBackgroundColor(Color.WHITE);
        inner.addView(cv);

        layerBox=findViewById(R.id.layerbox);
        layerLayout=findViewById(R.id.layerlayout);
        ImageView layer1=new ImageView(this);
        layerParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layerParams.topMargin=4;
        layer1.setLayoutParams(layerParams);
        layer1.setImageBitmap(cv.getnBitmap());
        layerArray.add(layer1);
        layerLayout.addView(layerArray.get(layerNum));
    }

    public void clickIcons(View view) {
        switch (view.getId()){
            case R.id.pen:
                eraser.setBackgroundColor(Color.TRANSPARENT);
                fillPaint.setBackgroundColor(Color.TRANSPARENT);
                pen.setBackgroundColor(Color.RED);
                if(!isPenSizeSelected) setPenResize();
                else gonePenResize();
                cv.setPen();
                break;

            case R.id.fillPaint:
                pen.setBackgroundColor(Color.TRANSPARENT);
                eraser.setBackgroundColor(Color.TRANSPARENT);
                fillPaint.setBackgroundColor(Color.RED);
                cv.setFillPaint();
                break;

            case R.id.eraser:
                pen.setBackgroundColor(Color.TRANSPARENT);
                fillPaint.setBackgroundColor(Color.TRANSPARENT);
                eraser.setBackgroundColor(Color.RED);
                cv.setEraser();
                if(!isPenSizeSelected) setPenResize();
                else gonePenResize();
                goneSpoid();
                gonePalette();
                break;

            case R.id.palette:
                if(!isPaletteSelected) {
                    setPalette();
                }
                else gonePalette();
                break;

            case R.id.spoid:
                pen.setBackgroundColor(Color.TRANSPARENT);
                fillPaint.setBackgroundColor(Color.TRANSPARENT);
                eraser.setBackgroundColor(Color.TRANSPARENT);
                spoid.setBackgroundColor(Color.RED);
                if(!isSpoidSelected) setSpoid();
                else goneSpoid();
                break;
            case R.id.undo: cv.doUndo(); break;
            case R.id.redo: cv.doRedo(); break;
            case R.id.save: Toast.makeText(this, "준비중입니다", Toast.LENGTH_SHORT).show();  break;

        }//check

        imm.hideSoftInputFromWindow(etR.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etG.getWindowToken(), 1);
        imm.hideSoftInputFromWindow(etB.getWindowToken(), 2);
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ펜 크기 변경ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    void setPenResize(){
        penSize.setVisibility(View.VISIBLE);
        penSize.bringToFront();
        isPenSizeSelected=true;

        gonePalette();
        goneSpoid();
    }
    void gonePenResize(){
        penSize.setVisibility(View.GONE);
        isPenSizeSelected=false;
    }
    SeekBar.OnSeekBarChangeListener seekBarChangeListener=new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            penpixel=progress;
            tvPenSize.setText(penpixel+"");
            cv.setPenSize(penpixel);
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ팔레트ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    void setPalette(){
        colorChange.setVisibility(View.VISIBLE);
        colorChange.bringToFront();
        palette.setBackgroundColor(Color.RED);

        isPaletteSelected=true;
        clickPrevColor();

        gonePenResize();
    }
    void gonePalette(){
        colorChange.setVisibility(View.GONE);
        palette.setBackgroundColor(Color.TRANSPARENT);

        spectrumbox.setVisibility(View.GONE);
        isSpbox=true;

        isPaletteSelected=false;
    }
    TextWatcher watcherR=new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        public void afterTextChanged(Editable s) {
            String c=s.toString();
            if(c==null||c.equals(""))red=0;
            else {
                red=Integer.parseInt(c);
                if(red>255){
                    red=255;
                    etR.setText(255+"");
                }
            }
            if(!isSpoidSelected){
                color=Color.rgb(red, green, blue);
            }else color=spoidColor;
            setPrevcolor();
            cv.setPenColor(color);
        }
    };
    TextWatcher watcherG=new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        public void afterTextChanged(Editable s) {
            String c=s.toString();
            if(c==null||c.equals(""))green=0;
            else {
                green=Integer.parseInt(c);
                if(green>255){
                    green=255;
                    etG.setText(255+"");
                }
            }
            if(!isSpoidSelected){
                color=Color.rgb(red, green, blue);
            }else color=spoidColor;
            setPrevcolor();
            cv.setPenColor(color);
        }
    };
    TextWatcher watcherB=new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        public void afterTextChanged(Editable s) {
            String c=s.toString();
            if(c==null||c.equals(""))blue=0;
            else {
                blue=Integer.parseInt(c);
                if(blue>255){
                    blue=255;
                    etB.setText(255+"");
                }
            }
            if(!isSpoidSelected){
                color=Color.rgb(red, green, blue);
            }else color=spoidColor;
            setPrevcolor();
            cv.setPenColor(color);
        }
    };
    void setPrevcolor() {
        prevcolor.setBackgroundColor(color);
    }
    public void clickColorStock(View view) {
        int position=view.getId()-R.id.colorstock1;
        if(isSpoidSelected){
            colorStocks[position].setBackgroundColor(color);
            colorStocks[position].setTag(color);
        }
        else{
            color=(Integer)colorStocks[position].getTag();
            red=Color.red(color);
            green=Color.green(color);
            blue=Color.blue(color);
            etR.setText(red+"");
            etG.setText(green+"");
            etB.setText(blue+"");
        }
    }
    public void clickPrevColor() {
        if(isSpbox){
            spectrumbox.setVisibility(View.VISIBLE);
            spectrumbox.bringToFront();
            isSpbox=false;
        }else {
            spectrumbox.setVisibility(View.GONE);
            isSpbox=true;
        }
    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(v==prevcolor){
                clickPrevColor();
                return false;
            }else if(v==spectrum){
                CircleImageView view=(CircleImageView)v;
                int sX=(int)event.getX();
                int sY=(int)event.getY();
                view.setDrawingCacheEnabled(true);
                Bitmap bitmap=Bitmap.createBitmap(view.getDrawingCache());
                view.setDrawingCacheEnabled(false);

                int r=bitmap.getWidth()/2;

                if(Math.pow(r-sX, 2)+Math.pow(r-sY, 2)<=Math.pow(r, 2) && sX>=0 && sY>=0){
                    int pixel=bitmap.getPixel(sX, sY);
                    color=Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                    setPaletteDialog(color);
                }
            }
            return true;
        }
    };

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ스포이드ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    void setSpoid(){
        cv.setSpoidColor();
        isSpoidSelected=true;
        isPenSizeSelected=false;
        gonePenResize();
    }
    void goneSpoid(){
        spoid.setBackgroundColor(Color.TRANSPARENT);
        isSpoidSelected=false;
    }
    void getDropperColor(int color){
        spoidColor=color;
        spoid.setBackgroundColor(spoidColor);
        setPaletteDialog(spoidColor);
    }
    void setPaletteDialog(int color){
        etR.setText(Color.red(color)+"");
        etG.setText(Color.green(color)+"");
        etB.setText(Color.blue(color)+"");
        setPrevcolor();
    }
    View.OnLongClickListener longClickListener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            v.setBackgroundColor(Color.WHITE);
            v.setTag(Color.WHITE);
            return true;
        }
    };


    public void clickLayer(View view) {
        if(!isLayerSelected){
            layerBox.setVisibility(View.VISIBLE);
            layerBox.bringToFront();
            isLayerSelected=true;
        }else{
            layerBox.setVisibility(View.GONE);
            isLayerSelected=false;
        }
    }


    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ레이어ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    void newLayer(){
        ImageView layer=new ImageView(this);
        layer.setLayoutParams(layerParams);
        layer.setImageBitmap(cv.getnBitmap());
        layerArray.add(layer);
        layerNum=layerArray.size()-1;
        cv.setLayerCount(layerNum);
    }
    void deleteLayer(){
        layerArray.remove(layerNum);
        layerNum--;
        cv.setLayerCount(layerNum);
    }
    void layerImageChange(Bitmap bitmap, int num){
        layerArray.get(num).setImageBitmap(bitmap);
    }
}