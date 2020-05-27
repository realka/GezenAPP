package com.example.gezenapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {
    Button cikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
}
