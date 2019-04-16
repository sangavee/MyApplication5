package com.yoho.myapplication;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<ImageModel> imageModelArrayList;
    private ArrayList<String> images_list;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(Context context, ArrayList<String> imageModelArrayList) {
        this.context = context;
        this.images_list = imageModelArrayList;
       // inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images_list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        /*View imageLayout = inflater.inflate(R.layout.flipimage, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageView_flipper);


        imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());


        view.addView(imageLayout, 0);

        return imageLayout;*/


        ImageView imgDisplay;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.flipimage, view,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imageView_flipper);
        Picasso.get().load(images_list.get(position)).into(imgDisplay);

      //  Picasso.setSingletonInstance(context).load(String.valueOf(images_list.get(position))).into(imgDisplay);
        (view).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}