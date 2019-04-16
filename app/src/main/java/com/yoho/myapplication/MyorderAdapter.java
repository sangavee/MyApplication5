package com.yoho.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyorderAdapter extends RecyclerView.Adapter<MyorderAdapter.MYorderCustomViewHolder> {

    private Context cxt;
    private List<MyorderPojo> myorderlist;

    public MyorderAdapter(Context cxt, List<MyorderPojo> myorderlist) {
        this.cxt = cxt;
        this.myorderlist = myorderlist;
    }

    @NonNull
    @Override
    public MYorderCustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(cxt);
        View v=inflater.inflate(R.layout.myorder_list_design,null);
        return  new MYorderCustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MYorderCustomViewHolder mYorderCustomViewHolder, int i) {

        final MyorderPojo myorderPojoobject=myorderlist.get(i);
        Picasso.get().load(myorderPojoobject.getOverimage()).into(mYorderCustomViewHolder.overview_image);

        mYorderCustomViewHolder.id.setText(myorderPojoobject.getId());
        mYorderCustomViewHolder.productid.setText(myorderPojoobject.getProduct_id());
        mYorderCustomViewHolder.datetime.setText(myorderPojoobject.getDatetime());
        mYorderCustomViewHolder.product_name.setText(myorderPojoobject.getP_name());

        mYorderCustomViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderid=myorderPojoobject.getId();
                String productid=myorderPojoobject.getProduct_id();
                Intent i=new Intent(cxt,MyorderOverviewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("order_id",orderid);
                i.putExtra("product_id",productid);
                cxt.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myorderlist.size();
    }

    public class MYorderCustomViewHolder extends RecyclerView.ViewHolder
    {

        TextView id,datetime,product_name,productid;
        ImageView overview_image;


        public MYorderCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            overview_image=(ImageView)itemView.findViewById(R.id.overviewimage);
            id=(TextView)itemView.findViewById(R.id.orderidvalue);
            datetime=(TextView)itemView.findViewById(R.id.dateandtime);
            product_name=(TextView)itemView.findViewById(R.id.productname);
            productid=(TextView)itemView.findViewById(R.id.productid);


        }

    }
}
