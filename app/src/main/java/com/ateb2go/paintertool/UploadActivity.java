package com.ateb2go.paintertool;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class UploadActivity extends AppCompatActivity {

    Context context=this;

    ImageView iv;
    EditText etNickname;
    EditText etPassword;
    EditText etTitle;
    EditText etComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        iv=findViewById(R.id.iv_upload);
        etNickname=findViewById(R.id.et_nick);
        etPassword=findViewById(R.id.et_pass);
        etTitle=findViewById(R.id.et_title);
        etComment=findViewById(R.id.et_comment);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ImageSelectActivity.class);
                startActivityForResult(intent, 10);
            }
        });
    }

    public void clickUpload(View view) {

    }
}
