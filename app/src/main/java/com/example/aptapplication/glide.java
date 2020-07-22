package com.example.aptapplication;

import android.app.Application;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class glide extends AppGlideModule {

    public void addImage(MainView main,HorizontalScrollView sc,LinearLayout ll,int num){


        FrameLayout fl = new FrameLayout(main);
        ImageView iv = new ImageView(main);
        String url ="http://121.139.55.207:8000/adRest/getAdImage?aptId=2b2b76a7-491e-4579-a6c5-79b94eca55a8&num="+num;
        Glide.with(main).load(url).into(iv);
        fl.addView(iv);
        ll.addView(fl);

    }

}
