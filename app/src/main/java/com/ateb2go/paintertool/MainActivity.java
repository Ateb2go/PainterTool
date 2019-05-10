package com.ateb2go.paintertool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context=this;

    ImageView newPaint;
    ImageView loadCanvas;
    ImageView upload;
    ImageView appreciate;
    ImageView exit;

    EditText etwidth;
    EditText etheight;
    RadioGroup rg;
    RadioButton rb34;
    RadioButton rb916;
    RadioButton rbcustom;

    int width=900, height=1200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newPaint=findViewById(R.id.btn_newpaint);
        loadCanvas=findViewById(R.id.btn_loadcanvas);
        upload=findViewById(R.id.btn_upload);
        appreciate=findViewById(R.id.btn_appreciate);
        exit=findViewById(R.id.btn_exit);

        newPaint.setOnClickListener(onClickListener);
        newPaint.setTag(0);
        loadCanvas.setOnClickListener(onClickListener);
        loadCanvas.setTag(1);
        upload.setOnClickListener(onClickListener);
        upload.setTag(2);
        appreciate.setOnClickListener(onClickListener);
        appreciate.setTag(3);
        exit.setOnClickListener(onClickListener);
        exit.setTag(4);

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;

            switch ((int)v.getTag()){
                case 0:
                    //커스텀다이얼로그
                    dialogSet();
                    break;
                case 1:
                    //저장한 작업 로딩
                    intent=new Intent(context, LoadActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent=new Intent(context, UploadActivity.class);
                    startActivity(intent);
//                    Toast.makeText(context, "준비중입니다", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    intent=new Intent(context, AppreciateActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    finish();
                    break;
            }

        }
    };

    void dialogSet(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.dialog_size, null);
        etwidth = layout.findViewById(R.id.etgaro);
        etheight = layout.findViewById(R.id.etsero);

        etwidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String sWidth=etwidth.getText().toString();
                if(sWidth.equals("")) etheight.setText("");
                if(!sWidth.equals("")){
                    width=Integer.parseInt(sWidth);
                    if(rb34.isChecked()){
                        height=width*4/3;
                        etheight.setText(height+"");
                    }else if(rb916.isChecked()){
                        height=width*16/9;
                        etheight.setText(height+"");
                    }
                }
            }
        });
        etheight.setEnabled(false);

        rg=layout.findViewById(R.id.rg);
        rb34=layout.findViewById(R.id.rb34);
        rb916=layout.findViewById(R.id.rb916);
        rbcustom=layout.findViewById(R.id.rbcustom);
        rg.check(R.id.rb34);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String sWidth=etwidth.getText().toString();
                switch (checkedId){
                    case R.id.rb34:
                        etheight.setEnabled(false);
                        if(!etwidth.getText().toString().equals("")){
                            width=Integer.parseInt(sWidth);
                            height=width*4/3;
                            etheight.setText(height+"");
                        }
                        break;
                    case R.id.rb916:
                        etheight.setEnabled(false);
                        if(!etwidth.getText().toString().equals("")){
                            width=Integer.parseInt(sWidth);
                            height=width*16/9;
                            etheight.setText(height+"");
                        }
                        break;
                    case R.id.rbcustom:
                        etheight.setEnabled(true);
                        break;
                }
            }
        });

        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("시작", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        builder.setView(layout);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etwidth.getText().toString().equals("") ||
                        etheight.getText().toString().equals("") ||
                        width==0 || height==0){
                    Toast.makeText(context, "캔버스의 크기를 지정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1=new Intent(context, DrawActivity.class);
                intent1.putExtra("width", etwidth.getText().toString());
                intent1.putExtra("height", etheight.getText().toString());
                intent1.putExtra("path", "");
                startActivity(intent1);
                dialog.dismiss();
            }
        });
    }
}
