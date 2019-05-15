package com.ateb2go.paintertool;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FragAdapter extends RecyclerView.Adapter {

    ArrayList<PictureVO> pictures;
    Context context;

    public FragAdapter(ArrayList<PictureVO> pictures, Context context) {
        this.context=context;
        this.pictures=pictures;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View pictureView=inflater.inflate(R.layout.recycler_newpicture, viewGroup, false);

        VH holder=new VH(pictureView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        VH vh= (VH)viewHolder;

        PictureVO picture=pictures.get(i);
        vh.nickname.setText(picture.getNickname());
        vh.title.setText(picture.getTitle());
        vh.date.setText(picture.getDate());
        Glide.with(context).load(picture.imgPath).into(vh.iv);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    class VH extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView nickname;
        TextView title;
        TextView date;

        public VH(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.newpic_image);
            nickname=itemView.findViewById(R.id.newpic_nick);
            title=itemView.findViewById(R.id.newpic_title);
            date=itemView.findViewById(R.id.newpic_date);

        }
    }
}
