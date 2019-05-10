package com.ateb2go.paintertool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Locale;

public class LoadActivity extends AppCompatActivity {

    ImageView iv;
    RecyclerView recyclerView;
    LoadAdapter adapter;
    String path;

    ArrayList<File> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        iv=findViewById(R.id.iv_load);
        recyclerView=findViewById(R.id.recycler_load);

        loadImage();
        adapter=new LoadAdapter(arrayList, this, path);
        recyclerView.setAdapter(adapter);

    }

    void loadImage(){
        path = "/data/data/com.ateb2go.paintertool/files";

        File f = new File(path);
//        File[] files = f.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.getName().toLowerCase(Locale.US).endsWith(".png"); //확장자
//            }
//        });
        File[] files=f.listFiles();

        if(files.length!=0){
            for(int i=0;i<files.length;i++){
                arrayList.add(files[i]);
            }
        }

//        String imgpath=arrayList.get(0).toString();
//        Bitmap bm=BitmapFactory.decodeFile(imgpath);
//        iv.setImageBitmap(bm);
    }
}
