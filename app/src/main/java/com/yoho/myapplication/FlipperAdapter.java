package com.yoho.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


class FlipperAdapter extends BaseAdapter
{
Context cxt;
int[] images;
LayoutInflater inflater;

    public FlipperAdapter(Context cxt, int[] images) {
        this.cxt = cxt;
        this.images = images;
        inflater=LayoutInflater.from(cxt);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.flipimage,null);
        ImageView img=convertView.findViewById(R.id.imageView_flipper);
        img.setImageResource(images[position]);

        return convertView;
    }
}