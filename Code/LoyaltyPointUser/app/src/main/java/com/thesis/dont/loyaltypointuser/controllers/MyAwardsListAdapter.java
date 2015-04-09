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
import com.thesis.dont.loyaltypointuser.models.AwardHistory;

import java.util.ArrayList;

/**
 * Created by 11120_000 on 04/04/15.
 */
public class MyAwardsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<AwardHistory> mAwards = new ArrayList<AwardHistory>();

    private Activity mParentActivity;

    public MyAwardsListAdapter(){}

    public MyAwardsListAdapter(Context context, ArrayList<AwardHistory> mAwards) {
        mInflater = LayoutInflater.from(context);
        this.mAwards = mAwards;
        mParentActivity = (Activity) context;
    }

    public void setListAwards(ArrayList<AwardHistory> listAwards) {
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
            view = mInflater.inflate(R.layout.my_awards_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();

            holder.time = (TextView) view.findViewById(R.id.time);
            holder.awardImage = (ImageView) view.findViewById(R.id.awardImage);
            holder.awardName = (TextView) view.findViewById(R.id.awardName);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.shopImage = (ImageView) view.findViewById(R.id.shopImage);
            holder.shopName = (TextView) view.findViewById(R.id.shopName);
            holder.available = (TextView) view.findViewById(R.id.available);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        AwardHistory award = (AwardHistory) getItem(position);
        if(award.getAwardImage() == null || award.getAwardImage().equals(""))
            award.setAwardImage(null);

        if(award.getShopImage() == null || award.getShopImage().equals(""))
            award.setShopImage(null);

        holder.time.setText(award.getTime());
        Picasso.with(mParentActivity).load(award.getAwardImage()).placeholder(R.drawable.ic_award).into(holder.awardImage);
        holder.awardName.setText(award.getAwardName());
        holder.quantity.setText(String.valueOf(award.getQuantity()));
        Picasso.with(mParentActivity).load(award.getShopImage()).placeholder(R.drawable.ic_store).into(holder.shopImage);
        holder.shopName.setText(award.getShopName());

        if(award.isTaken()) {
            holder.available.setBackgroundColor(mParentActivity.getResources().getColor(R.color.MaterialDarkRed));
            holder.available.setText("-");
        }else {
            holder.available.setBackgroundColor(mParentActivity.getResources().getColor(R.color.AccentColor));
            holder.available.setText("+");
        }

        return view;
    }

    public class ViewHolder {
        public TextView time;
        public ImageView awardImage;
        public TextView awardName;
        public TextView quantity;
        public ImageView shopImage;
        public TextView shopName;
        public TextView available;
    }
}
