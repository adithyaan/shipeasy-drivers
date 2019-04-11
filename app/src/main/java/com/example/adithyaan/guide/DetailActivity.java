package com.example.adithyaan.guide;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity  {
RecyclerView recyclerView;
    Button enter;
    EditText amt_et;
    String amtstr;
    DatabaseReference myRef;
    FirebaseDatabase database;
    DetailActivity activity;
    MapFragmentView mapFragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        recyclerView=findViewById(R.id.rv);
        amt_et=findViewById(R.id.et);
        String instance = getIntent().getExtras().getString("instance");
        enter = findViewById(R.id.enter);

        getSupportActionBar().setTitle("Shippment");


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders").child(instance).child("bids");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            amt_et.setDefaultFocusHighlightEnabled(false);
        }

        enter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                amtstr=amt_et.getText().toString();
                if (!amtstr.equals("")) {
                    final HashMap map = new HashMap();
                    map.put("Name", Constants.name);
                    map.put("Amount", amtstr);
                    DatabaseReference reference = myRef.push();
                    reference.setValue(map);
                    amt_et.setText("");
//                Toast.makeText(DetailActivity.this, "Waits for 30 seconds, ", Toast.LENGTH_SHORT).show();
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
////                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//
//                        mapFragmentView = new MapFragmentView(activity);
////                        break;
//                    }
//                }, 30000);
//

//                    Thread thread =new Thread()
//                    {
//                        @Override
//                        public void run() {
//                            try {
//                                sleep(2000);
////                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
////                                finish();
//                                    mapFragmentView = new MapFragmentView(activity);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    thread.start();


                }
            }
        });

        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this,MainActivity.class));
            }
        });

        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Data,ViewHolder>(Data.class,R.layout.firebase,ViewHolder.class,myRef)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Data model, int position)
            {
                viewHolder.inflate(model);
            }


        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);




    }

    @Override
    protected void onDestroy() {
        mapFragmentView.onDestroy();
        super.onDestroy();
    }
}
