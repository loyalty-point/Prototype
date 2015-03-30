package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11120_000 on 18/03/15.
 */
public class AwardsListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Award> mAwards = new ArrayList<Award>();

    private Activity mParentActivity;

    public AwardsListAdapter(){}

    public AwardsListAdapter(Context context, List<Award> mAwards) {
        mInflater = LayoutInflater.from(context);
        this.mAwards = mAwards;
        mParentActivity = (Activity) context;
    }

    public void setListAwards(ArrayList<Award> listAwards) {
        mAwards = listAwards;
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
        Picasso.with(mParentActivity).load(award.getImage()).placeholder(R.drawable.ic_award).into(holder.awardImage);
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
