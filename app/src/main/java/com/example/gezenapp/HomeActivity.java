package com.example.gezenapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    private DatabaseReference mDatabase;
    //CardView cardView;
    TextView header;
    TextView abs;
    List<Post> posts = new ArrayList<>();
    //ImageView image;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;

    Button cikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //cardView = (CardView) findViewById(R.id.cardView);
        header = (TextView) findViewById(R.id.header);
        abs = (TextView) findViewById(R.id.abs);
        //image = (ImageView) findViewById(R.id.postImage);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);
                    // here you can access to name property like university.name

                }

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                adapter = new MyRecyclerViewAdapter(recyclerView.getContext(), posts);
                //adapter.setClickListener(recyclerView.getContext());
                adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) recyclerView.getContext());
                recyclerView.setAdapter(adapter);

                /*mAdapter = new CustomAdepter(getBaseContext(), posts);
                for (int i =0; i < posts.size(); i++){
                    recyclerView.setAdapter(mAdapter.getView(i,recyclerView,null));
                }*/
                //recyclerView.setAdapter(mAdapter);
                /*for (int i =0; i < posts.size(); i++){
                    header.setText(posts.get(i).getHeader());
                    abs.setText(posts.get(i).getContext());
                }*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        cikis = findViewById(R.id.cikisyap);
        cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent iMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(iMain);
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_LONG).show();
    }
}
