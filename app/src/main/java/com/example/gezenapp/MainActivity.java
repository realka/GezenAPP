package com.example.gezenapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button   giris;
    FirebaseAuth mFirebaseAuth;
    TextView    kayit;
    private  FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.password);
        giris = findViewById(R.id.giris);
        kayit = findViewById(R.id.kayit);

        kayit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();

                if (pwd.isEmpty() && email.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Girilen bilgiler hatali",Toast.LENGTH_SHORT);
                }
                else if (!pwd.isEmpty() && !email.isEmpty())
                {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Hesap olusturma basarisiz.",Toast.LENGTH_SHORT);
                            }
                            else{
                                Intent iHome = new Intent(MainActivity.this,HomeActivity.class);
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
                    Toast.makeText(MainActivity.this,"Giriş başarili",Toast.LENGTH_SHORT);
                    Intent i = new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Girilen bilgiler hatali",Toast.LENGTH_SHORT);
                }
            };
        };

        giris.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                String usr = emailId.getText().toString();
                String pwd  = password.getText().toString();
                mFirebaseAuth.signInWithEmailAndPassword(usr,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Başarısız",Toast.LENGTH_SHORT);
                        }
                        else{
                            Intent iHome = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(iHome);
                        }
                    }
                });
            }
        });
    }
}
