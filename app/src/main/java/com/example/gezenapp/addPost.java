package com.example.gezenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addPost extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    TextView header,ctx;
    ImageView imageView;

    TextView navDisplayName;
    ImageView navImage;
    Button addPost;

    private Uri filePath;

    String imageUrl,postHeader,postContent;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference,storageReference2;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        header = (TextView) findViewById(R.id.addHeader);
        ctx = (TextView) findViewById(R.id.addContent);
        imageView = (ImageView) findViewById(R.id.addImage);

        addPost = (Button) findViewById(R.id.addPost);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
                imageUrl = uploadImage();
            }
        });


        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                postHeader = header.getText().toString();
                postContent = ctx.getText().toString();
                writeNewPost(postHeader, postContent, imageUrl);
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
                    Intent iMain = new Intent(addPost.this, MainActivity.class);
                    startActivity(iMain);

                }else  if (id == R.id.home){

                    Intent iMain = new Intent(addPost.this, MainActivity.class);
                    startActivity(iMain);

                }else if (id == R.id.profile){

                    //Toast.makeText(addPost.this,"Profile",Toast.LENGTH_SHORT);
                    Intent iHome = new Intent(addPost.this,addPost.class);
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

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private String uploadImage()
    {
        Log.d("TAG", "hatamivar: ");
        String picName = null;
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference

            storageReference2 = FirebaseStorage.getInstance().getReference();
            StorageReference ref
                    = storageReference2
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());
            picName = ref.toString();
            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(addPost.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    Log.d("TAG", "hatamivar: "+"succsess");
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(addPost.this,
                                            "Failed - " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("TAG", "hatamivar: "+e.getMessage());
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });

        }

        Log.d("TAG", "hatamivar: "+picName);
        return picName;
    }
    private void writeNewPost(String postHeader, String postContent, String imageUrl) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newRef = mDatabase.push();

        Post post = new Post();
        post.setHeader(postHeader);
        post.setContext(postContent);
        post.setImageUrl(imageUrl);

        newRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent iHome = new Intent(addPost.this, HomeActivity.class);
                startActivity(iHome);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });;

        /*mDatabase.child("posts").child("header").setValue(postHeader);
        mDatabase.child("posts").child("postContent").setValue(postContent);
        mDatabase.child("posts").child("imageUrl").setValue(imageUrl);*/

        /*String key = mDatabase.push().getKey();
        Post post = new Post(postHeader, postContent, imageUrl);
        Map<String, Object> postValues = post.toMap();*/

        //Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put(key, postValues);

        //mDatabase.updateChildren(childUpdates);

//        mDatabase.setValue(post)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Intent iHome = new Intent(addPost.this, HomeActivity.class);
//                        startActivity(iHome);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });

    }
}