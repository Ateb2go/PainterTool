package com.ateb2go.paintertool;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

public class UploadActivity extends AppCompatActivity {

    Context context=this;

    ImageView iv;
    EditText etNickname;
    EditText etPassword;
    EditText etTitle;
    EditText etComment;

    String path=null;
    String nickname;
    String password;
    String title;
    String comment;

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
                Intent intent=new Intent(context, LoadActivity.class);
                intent.putExtra("upload", true);
                startActivityForResult(intent, 10);
            }
        });
    }

    public void clickUpload(View view) {
        if(path==null){
            Toast.makeText(context, "그림을 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        nickname=etNickname.getText().toString();
        if(nickname.equals("")){
            Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        password=etPassword.getText().toString();
        if(password.equals("")){
            Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        title=etTitle.getText().toString();
        if(title.equals("")){
            Toast.makeText(context, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        comment=etComment.getText().toString()+" ";

        AlertDialog.Builder stay=new AlertDialog.Builder(this);
        stay.setMessage("업로드하는 중...");
        final AlertDialog stayD=stay.create();
        stayD.setCancelable(false);
        stayD.show();

        String serverUrl="http://ahpla.dothome.co.kr/Paintndots/uploadDB.php";

        SimpleMultiPartRequest multiPartRequest=new SimpleMultiPartRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //응답을 받았을 때 자동 실행
//                new AlertDialog.Builder(UploadActivity.this).setMessage(response).show();
                stayD.dismiss();
                Toast.makeText(context, "업로드 완료", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stayD.dismiss();
                Toast.makeText(UploadActivity.this, error.getMessage()+" 에러", Toast.LENGTH_SHORT).show();
            }
        });

        multiPartRequest.addStringParam("nickname", nickname);
        multiPartRequest.addStringParam("password", password);
        multiPartRequest.addStringParam("title", title);
        multiPartRequest.addStringParam("comment", comment);
        multiPartRequest.addFile("image", path);

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(multiPartRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch ( requestCode ){
            case 10:
                if( resultCode==RESULT_OK){
                    iv.setBackgroundColor(Color.TRANSPARENT);
                    path=data.getStringExtra("path");
                    iv.setImageBitmap(BitmapFactory.decodeFile(path));

                }
                break;
        }
    }
}
