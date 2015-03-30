package com.thesis.dont.loyaltypointadmin.views;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.RoundedImageView;

/**
 * Created by 11120_000 on 27/03/15.
 */
public class MyRoundedImageView extends RoundedImageView {
    //private static double aspectRatio = 0.5784753363;
    private static double aspectRatio;

    public MyRoundedImageView(Context context) {
        super(context);
    }

    public MyRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        aspectRatio = 610/(double)948;
        int newHeight = (int)(widthMeasureSpec * aspectRatio);
        super.onMeasure(widthMeasureSpec, newHeight);
    }*/


}
