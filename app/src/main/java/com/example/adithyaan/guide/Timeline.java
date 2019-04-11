package com.example.adithyaan.guide;

import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;

public class Timeline extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList list = new ArrayList();
    FireInstanceService fireInstanceService;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        recyclerView = findViewById(R.id.rv);
        FireInstanceService fi=new FireInstanceService();
        fi.onTokenRefresh();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");
        FirebaseRecyclerAdapter firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<NewsFeedData,MainViewHolder>(NewsFeedData.class,R.layout.news_item,MainViewHolder.class,myRef)
        {
            @Override
            protected void populateViewHolder(MainViewHolder viewHolder, NewsFeedData model, int position)
            {
                viewHolder.inflate(model, Timeline.this);
            }


        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

   /* public void getData()
    {
        for (int i=0;i<3;i++)
        {
            list.add(new NewsFeedData("Kundrathur","Chennai","Adithya","Tv and a berow"));
        }
    }*/

}
