package com.example.adithyaan.guide;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by ADITHYA AN on 01-06-2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder{
    TextView name,amount;
    public ViewHolder(View itemView) {
        super(itemView);
        amount=itemView.findViewById(R.id.Amount);
        name=itemView.findViewById(R.id.name);
    }
    public void inflate(Data data)
    {
        /*if(data.Name.equals("Test"))
            name.setVisibility(View.GONE);*/
       name.setText(data.Name);
       amount.setText(data.Amount);

    }
}
