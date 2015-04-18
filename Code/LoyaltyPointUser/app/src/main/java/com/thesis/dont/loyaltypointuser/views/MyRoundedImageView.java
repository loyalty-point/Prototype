package com.thesis.dont.loyaltypointuser.views;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        aspectRatio = 610/(double)948;
        int newHeight = (int)(originalWidth * aspectRatio);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(originalWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY));

        //super.onMeasure(widthMeasureSpec, newHeight);
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


}
