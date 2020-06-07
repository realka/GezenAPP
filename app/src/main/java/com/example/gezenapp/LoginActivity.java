package com.example.gezenapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class LoginActivity extends AppCompatActivity {
    private  FirebaseAuth.AuthStateListener mAuthStateListener;

    EditText emailId, password;
    FirebaseAuth mFirebaseAuth;
    TextView kayit;
    // views for button
    //private Button  btnUpload;

    // view for image view
    //private ImageView imageView;

    // Uri indicates, where the image will be picked from
    //private Uri filePath;

    // request code
    //private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        emailId = findViewById(R.id.kayitusername);
        password = findViewById(R.id.kayitpassword);
        kayit = findViewById(R.id.kayitol);

        kayit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                //uploadImage();

                if (pwd.isEmpty() && email.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"Girilen bilgiler hatali",Toast.LENGTH_SHORT).show();
                }
                else if (!pwd.isEmpty() && !email.isEmpty())
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Hesap olusturma basarisiz.",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent iHome = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(iHome);
                            }
                        }
                    });
                }
            }


        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null)
                {
                    Toast.makeText(LoginActivity.this,"Giriş başarili",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,LoginActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Girilen bilgiler hatali",Toast.LENGTH_SHORT).show();
                }
            };
        };
    }
}

