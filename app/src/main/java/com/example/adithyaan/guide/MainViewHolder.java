package com.example.adithyaan.guide;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ADITHYA AN on 01-06-2018.
 */

class MainViewHolder extends RecyclerView.ViewHolder
{

    TextView from_location,to_location,customer_name,goods_descp;
    Button start_bid;
    CardView cv;


    public MainViewHolder(View itemView)
    {
        super(itemView);
        from_location=itemView.findViewById(R.id.vehiclecustomer_name);
        to_location=itemView.findViewById(R.id.drivername2);
        customer_name=itemView.findViewById(R.id.customer_name);
        goods_descp=itemView.findViewById(R.id.goods_type);
        start_bid=itemView.findViewById(R.id.payment2);
        cv=itemView.findViewById(R.id.cv);
    }

    public void inflate(final NewsFeedData model, final Context context)
    {
        from_location.setText(model.getFrom_location());
        customer_name.setText(model.getCustomer_name());
        goods_descp.setText(model.getGoods_descp());
        to_location.setText(model.getTo_location());

        cv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("instance",model.getGoods_descp());
                context.startActivity(intent);
            }
        });
    }

}
