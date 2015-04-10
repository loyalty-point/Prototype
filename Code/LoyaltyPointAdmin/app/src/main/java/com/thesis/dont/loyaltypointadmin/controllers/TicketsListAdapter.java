package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.AwardHistory;
import com.thesis.dont.loyaltypointadmin.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinntt on 3/26/2015.
 */
public class TicketsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<AwardHistory> mAwardHistorys = new ArrayList<AwardHistory>();

    private Activity mParentActivity;

    public TicketsListAdapter(){}

    public TicketsListAdapter(Context context, List<AwardHistory> mAwardHistory) {
        mInflater = LayoutInflater.from(context);
        this.mAwardHistorys = mAwardHistory;
        mParentActivity = (Activity) context;
    }

    public void setListTickets(ArrayList<AwardHistory> listAwardHistorys) {
        mAwardHistorys = listAwardHistorys;
    }

    @Override
    public int getCount() {
        return mAwardHistorys.size();
    }

    @Override
    public Object getItem(int position) {
        return mAwardHistorys.get(position);
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
            view = mInflater.inflate(R.layout.ticket_list_row, parent, false);

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
        holder.available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
