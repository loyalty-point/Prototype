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
import com.thesis.dont.loyaltypointadmin.models.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11120_000 on 04/04/15.
 */
public class ListHistoriesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<History> mHistories = new ArrayList<History>();

    private Activity mParentActivity;

    public ListHistoriesAdapter(){}

    public ListHistoriesAdapter(Context context, ArrayList<History> mHistories) {
        mInflater = LayoutInflater.from(context);
        this.mHistories = mHistories;
        mParentActivity = (Activity) context;
    }

    public void setListHistories(ArrayList<History> listHistories) {
        mHistories = listHistories;
    }

    @Override
    public int getCount() {
        return mHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistories.get(position);
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
            view = mInflater.inflate(R.layout.histories_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();

            holder.time = (TextView) view.findViewById(R.id.time);
            holder.billImage = (ImageView) view.findViewById(R.id.billImage);
            holder.billCode = (TextView) view.findViewById(R.id.billCode);
            holder.username = (TextView) view.findViewById(R.id.username);
            holder.fullname = (TextView) view.findViewById(R.id.fullname);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.totalPoints = (TextView) view.findViewById(R.id.totalPoints);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        History history = (History) getItem(position);
        if(history.getBillImage() == null || history.getBillImage().equals(""))
            history.setBillImage(null);

        holder.time.setText(history.getTime());
        Picasso.with(mParentActivity).load(history.getBillImage()).placeholder(R.drawable.bill_ic).into(holder.billImage);
        holder.billCode.setText(history.getBillCode());
        holder.username.setText(history.getUsername());
        holder.fullname.setText(history.getFullname());
        holder.phone.setText(history.getPhone());
        holder.totalPoints.setText(String.valueOf(history.getTotalPoint()));

        return view;
    }

    public class ViewHolder {
        public TextView time;
        public ImageView billImage;
        public TextView billCode;
        public TextView username;
        public TextView fullname;
        public TextView phone;
        public TextView totalPoints;
    }
}
