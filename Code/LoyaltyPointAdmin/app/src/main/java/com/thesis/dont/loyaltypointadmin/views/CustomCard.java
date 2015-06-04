package com.thesis.dont.loyaltypointadmin.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thesis.dont.loyaltypointadmin.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class CustomCard extends RelativeLayout {

    //protected FancyButton mCardName, mUserName, mQRCode;
    protected TextView mCardName, mUserName, mQRCode;
    protected MyRoundedImageView mCardBackground;
    protected int mCurrentTouchedView = -1;

    protected boolean mIsDragging = false;
    protected int pointerId;

    protected boolean allowHorizontalDrag = true;
    protected boolean allowVerticalDrag = true;

    protected DropListener dropListener;

    boolean isDrag = false;

    public TextView getCardName() {
        return mCardName;
    }

    public TextView getUserName() {
        return mUserName;
    }

    public TextView getQRCode() {
        return mQRCode;
    }

    public MyRoundedImageView getCardBackground() {
        return mCardBackground;
    }

    public interface DropListener {
        public void onDrop(final int dropIndex, final View dropTarget);
    }

    public CustomCard(Context context) {
        super(context);
        init();
    }

    public CustomCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(context, attrs);
        init();
    }

    @SuppressLint("NewApi")
    public CustomCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttrs(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.custom_card_layout, this, true);

        mCardBackground = (MyRoundedImageView) findViewById(R.id.cardImage);
        mCardName = (TextView) findViewById(R.id.cardName);
        mUserName = (TextView) findViewById(R.id.customerName);
        mQRCode = (TextView) findViewById(R.id.qrCode);

        setupDragHandle();
    }

    public void setDropListener(DropListener dropListener) {
        this.dropListener = dropListener;
    }

    public boolean isAllowHorizontalDrag() {
        return allowHorizontalDrag;
    }

    public void setAllowHorizontalDrag(boolean allowHorizontalDrag) {
        this.allowHorizontalDrag = allowHorizontalDrag;
    }

    public boolean isAllowVerticalDrag() {
        return allowVerticalDrag;
    }

    public void setAllowVerticalDrag(boolean allowVerticalDrag) {
        this.allowVerticalDrag = allowVerticalDrag;
    }

    protected void applyAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCard, 0, 0);

        isDrag = a.getBoolean(R.styleable.CustomCard_isDrag, false);

        try {
            /*
            layoutId = a.getResourceId(R.styleable.ConstrainedDragAndDropView_layoutId, 0);

            if (layoutId > 0) {
                LayoutInflater.from(context).inflate(layoutId, this, true);
            }
            */
        } finally {
            a.recycle();
        }
    }

    protected void setupDragHandle() {
        this.setOnTouchListener(new DragAreaTouchListener());
    }

    protected class DragAreaTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(view, motionEvent);
                    break;

                case MotionEvent.ACTION_UP:
                    onActionUp(view, motionEvent);
                    break;

               case MotionEvent.ACTION_MOVE:
                    onActionMove(view, motionEvent);
                    break;

                default:
                    break;
            }

            return true;
        }
    }

    protected int findCurrentTouchedView(MotionEvent motionEvent) {
        if(isDragHandleTouch(motionEvent, mCardName))
            return 0;
        else if(isDragHandleTouch(motionEvent, mUserName))
            return 1;
        else if(isDragHandleTouch(motionEvent, mQRCode))
            return 2;
        else return -1;
    }

    protected void onActionDown(View view, MotionEvent motionEvent) {
        // if we're not already dragging, and the touch position is on the drag handle,
        // then start dragging
        mCurrentTouchedView = findCurrentTouchedView(motionEvent);
        if(mCurrentTouchedView == -1) {
            callOnClick();
            return;
        }

        if(!mIsDragging && mCurrentTouchedView != -1) {
            pointerId = motionEvent.getPointerId(0);
            updateDragPosition(motionEvent);
            mIsDragging = true;
            Log.d("drag", "drag start");
        }
    }

    protected void onActionUp(View view, MotionEvent motionEvent) {

        // if we're dragging, then stop dragging
        if (mIsDragging && motionEvent.getPointerId(0) == pointerId) {
            updateDragPosition(motionEvent);
            mIsDragging = false;
            Log.d("drag", "drag end");
            //Toast.makeText(getContext(), dragHandle.getX() + "," + dragHandle.getY(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onActionMove(View view, MotionEvent motionEvent) {
        if (mIsDragging && motionEvent.getPointerId(0) == pointerId) {
            updateDragPosition(motionEvent);
        }
    }

    @SuppressLint("NewApi")
    protected void updateDragPosition(MotionEvent motionEvent) {

        // this is where we constrain the movement of the dragHandle
        View currentTouchedView = getCurrentTouchedView(mCurrentTouchedView);

        if(allowHorizontalDrag) {
            float candidateX = motionEvent.getX() - currentTouchedView.getWidth() / 2;
            if(candidateX > 0 && candidateX + currentTouchedView.getWidth() < this.getWidth()) {
                currentTouchedView.setX(candidateX);
            }
        }

        if(allowVerticalDrag) {
            float candidateY = motionEvent.getY() - currentTouchedView.getHeight() / 2;
            if(candidateY > 0 && candidateY + currentTouchedView.getHeight() < this.getHeight()) {
                currentTouchedView.setY(candidateY);
            }
        }
    }

    private View getCurrentTouchedView(int id) {
        if(id == 0)
            return mCardName;
        else if(id == 1)
            return mUserName;
        else if(id == 2)
            return mQRCode;

        return null;
    }


    protected boolean isDragHandleTouch(MotionEvent motionEvent, View view) {
        Point point = new Point(
            new Float(motionEvent.getRawX()).intValue(),
            new Float(motionEvent.getRawY()).intValue()
        );

        return isPointInView(point, view);
    }


    /**
     * Determines whether a raw screen coordinate is within the bounds of the specified view
     * @param point - Point containing screen coordinates
     * @param view - View to test
     * @return true if the point is in the view, else false
     */
    protected boolean isPointInView(Point point, View view) {

        int[] viewPosition = new int[2];
        view.getLocationOnScreen(viewPosition);

        int left = viewPosition[0];
        int right = left + view.getWidth();
        int top = viewPosition[1];
        int bottom = top + view.getHeight();

        return point.x >= left && point.x <= right && point.y >= top && point.y <= bottom;
    }


}
