package com.example.aptapplication;

import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public interface MainInterface {
    FrameLayout createImage(Bitmap bitmap);
    LinearLayout createLayout();
    HorizontalScrollView getScrollView();
    int getImageSize(Integer size);
}


