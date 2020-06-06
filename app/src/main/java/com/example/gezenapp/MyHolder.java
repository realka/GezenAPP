package com.example.gezenapp;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyHolder {

    TextView headerText,abstractText;
    ImageView postImage;

    public MyHolder(View itemView) {

        postImage = (ImageView) itemView.findViewById(R.id.postImage);
        headerText = (TextView) itemView.findViewById(R.id.header);
        abstractText = (TextView) itemView.findViewById(R.id.abs);

    }

}
