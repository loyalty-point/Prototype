package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;

import java.util.List;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class AwardsListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Award> mAwards;

    private Activity mParentActivity;

    public AwardsListAdapter(){}

    public AwardsListAdapter(Context context, List<Award> mAwards) {
        mInflater = LayoutInflater.from(context);
        this.mAwards = mAwards;
        mParentActivity = (Activity) context;
    }

    @Override
    public int getCount() {
        return mAwards.size();
    }

    @Override
    public Object getItem(int position) {
        return mAwards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;

        if(convertView == null) {
            view = mInflater.inflate(R.layout.awards_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.awardName = (TextView) view.findViewById(R.id.awardName);
            holder.awardPoint = (TextView) view.findViewById(R.id.awardPoint);
            holder.awardQuantity = (TextView) view.findViewById(R.id.awardQuantity);
            holder.awardImage = (ImageView) view.findViewById(R.id.awardImage);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Award award = (Award) getItem(position);
        holder.awardName.setText(award.getName());
        holder.awardPoint.setText("Point: " + String.valueOf(award.getPoint()));
        holder.awardQuantity.setText("Quantity: " + String.valueOf(award.getQuantity()));

        return view;
    }

    public class ViewHolder {
        public TextView awardName;
        public TextView awardPoint;
        public TextView awardQuantity;
        public ImageView awardImage;
    }
}
