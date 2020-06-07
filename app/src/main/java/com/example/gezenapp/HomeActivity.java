package com.example.gezenapp;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    private DatabaseReference mDatabase;
    //CardView cardView;
    TextView header;
    TextView abs;
    List<Post> posts = new ArrayList<>();
    //ImageView image;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    TextView navDisplayName;
    ImageView navImage;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    StorageReference storageReference;
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

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    posts.add(post);

                }

                recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
                adapter = new MyRecyclerViewAdapter(recyclerView.getContext(), posts);
                adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) recyclerView.getContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });


        //Navigation
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.cikisyap){

                     FirebaseAuth.getInstance().signOut();
                     Intent iMain = new Intent(HomeActivity.this, MainActivity.class);
                     startActivity(iMain);

                }else  if (id == R.id.home){

                    Toast.makeText(HomeActivity.this,"Anasayfa",Toast.LENGTH_SHORT);

                }else if (id == R.id.profile){

                    //Toast.makeText(HomeActivity.this,"Profile",Toast.LENGTH_SHORT);
                    Intent iHome = new Intent(HomeActivity.this,ProfileActivity.class);
                    startActivity(iHome);
                }
                return true;
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null ) {
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                Uri photoUrl = profile.getPhotoUrl();

                View header = navigationView.getHeaderView(0);
                navDisplayName = (TextView) header.findViewById(R.id.nav_text);
                navImage = (ImageView) header.findViewById(R.id.nav_pic);

                if (name != null)
                {
                    navDisplayName.setText(name);

                } else
                {
                    navDisplayName.setText("Bilinmeyen Kullanici");
                }
                if (photoUrl != null)
                {


                    // Create a storage reference from our app
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                    // Create a reference with an initial file path and name
                    StorageReference pathReference = storageRef.child("images/"+photoUrl.getLastPathSegment().toString());

                    Log.d("TAG", "onFailure: "+pathReference);

                    final long ONE_MEGABYTE = 1024 * 1024 * 4;
                    pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            navImage.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.d("TAG", "onFailure: "+exception.getMessage());
                        }
                    });

                }
            }
        }
        //Navigation
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) ||  super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
