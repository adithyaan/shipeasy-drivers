package com.example.adithyaan.guide;

/**
 * Created by ADITHYA AN on 03-05-2018.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adithyaan.guide.NewsFeedData;
//import com.example.adithyaan.guide.InsertTask;
//import com.example.adithyaan.guide.R;

import java.util.ArrayList;


/**
 * Created by ADITHYA AN on 02-05-2018.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<MyHolder> {

    OnClick click;
    ArrayList<NewsFeedData> arrayList =new ArrayList<NewsFeedData>();
    Context context;
    String from_location,to_location,customer_name,goods_descp;
    LayoutInflater inflater;

    public NewsFeedAdapter(Context context, ArrayList arrayList) {
        this.context = context;
        this.arrayList = arrayList;

        inflater=LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.news_item,null,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        holder.start_bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAmtDialog();
            }
        });

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context,DetailActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void showAmtDialog()
    {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
        alertDialog.setTitle("Enter Your Amount to Bid");
        View view=inflater.inflate(R.layout.amt_dialog,null);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("Bid", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                InsertTask insertTask=new InsertTask("url",context);
                //insertTask.execute("");

                Toast.makeText(context, "Successfully sent Your Quote to Customer", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

}

class MyHolder extends RecyclerView.ViewHolder
{

    TextView from_location,to_location,customer_name,goods_descp;
    Button start_bid;
    CardView cv;


    public MyHolder(View itemView)
    {
        super(itemView);
        from_location=itemView.findViewById(R.id.vehiclecustomer_name);
        to_location=itemView.findViewById(R.id.drivername2);
        customer_name=itemView.findViewById(R.id.customer_name);
        goods_descp=itemView.findViewById(R.id.goods_type);
        start_bid=itemView.findViewById(R.id.payment2);
        cv=itemView.findViewById(R.id.cv);
    }
}
