package com.yoho.myapplication;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewpagerAdapter extends PagerAdapter {

    private ArrayList<ViewPagerPojo> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;

    public ViewpagerAdapter(ArrayList<ViewPagerPojo> imageModelArrayList, LayoutInflater inflater, Context context) {
        this.imageModelArrayList = imageModelArrayList;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.flipimage, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.imageView_flipper);
        ViewPagerPojo viewPagerPojo=imageModelArrayList.get(position);


     //   imageView.setImageResource(        Picasso.get().load(viewPagerPojo.getImage()).into(imageLayout.im ));

      //  imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());

        view.addView(imageLayout, 0);

        return imageLayout;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
