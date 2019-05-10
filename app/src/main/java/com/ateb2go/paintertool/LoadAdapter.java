package com.ateb2go.paintertool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LoadAdapter extends RecyclerView.Adapter {
    ArrayList<File> items;
    Context context;
    String path;

    public LoadAdapter(ArrayList<File> items, Context context, String path) {
        this.items=items;
        this.context=context;
        this.path=path;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.recycler_load, viewGroup, false);

        VH holder=new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        File item=items.get(i);
        VH holder=(VH)viewHolder;
        String path=item.getAbsolutePath();
        String name=item.getName();
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        holder.iv.setImageBitmap(bitmap);
        holder.tv.setText(name);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder {

        TextView tv;
        ImageView iv;
        public VH(@NonNull View itemView) {
            super(itemView);

            tv=itemView.findViewById(R.id.tv_recycler_load);
            iv=itemView.findViewById(R.id.iv_recycler_load);


            //선택
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, DrawActivity.class);
                    intent.putExtra("path", items.get(getLayoutPosition()).toString());
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });

            //삭제
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("정말로 삭제하시겠습니까?");
                    builder.setNegativeButton("취소", null);
                    builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file=new File(path);
                            File[] list=file.listFiles();
                            Log.e("BTAG", list.length+"");
                            list[getLayoutPosition()].delete();
                            items.remove(getLayoutPosition());
                            notifyItemRemoved(getLayoutPosition());
                        }
                    });
                    builder.create().show();

                    return true;
                }
            });
        }
    }
}
