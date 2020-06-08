package com.example.gezenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class postDetailActivity extends AppCompatActivity {
    TextView header,ctx;
    ImageView   imageView;

    TextView navDisplayName;
    ImageView navImage;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        header = (TextView) findViewById(R.id.detHeader);
        ctx = (TextView) findViewById(R.id.detContent);
        imageView = (ImageView) findViewById(R.id.detImage);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String head = extras.getString("header");
            String content = extras.getString("ctx");
            String url = extras.getString("image");


            header.setText(head);
            ctx.setText(content);
        }


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
                    Intent iMain = new Intent(postDetailActivity.this, MainActivity.class);
                    startActivity(iMain);

                }else  if (id == R.id.home){

                    Intent iMain = new Intent(postDetailActivity.this, MainActivity.class);
                    startActivity(iMain);

                }else if (id == R.id.profile){

                    //Toast.makeText(postDetailActivity.this,"Profile",Toast.LENGTH_SHORT);
                    Intent iHome = new Intent(postDetailActivity.this,ProfileActivity.class);
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

}