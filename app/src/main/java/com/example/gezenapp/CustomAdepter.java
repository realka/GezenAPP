package com.example.gezenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CustomAdepter extends BaseAdapter {

    Context ctx;
    ArrayList<Post> postList;
    LayoutInflater inflater;

    public CustomAdepter(Context ctx, ArrayList<Post> postList) {
        this.ctx = ctx;
        this.postList = postList;
    }


    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (inflater == null)
        {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view  == null)
        {
            view = inflater.inflate(R.layout.listview_layout,viewGroup,false);
        }
        MyHolder holder= new MyHolder(view);
        holder.headerText.setText(postList.get(i).getHeader());
        holder.abstractText.setText(postList.get(i).getContext());
        PicassoClient.downloadimg(ctx,postList.get(i).getImageUrl(),holder.postImage);

        return view;
    }
}
