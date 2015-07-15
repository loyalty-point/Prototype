package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.Global;
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

    public ArrayList<History> getListHistories() {
        return mHistories;
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
            holder.fullname = (TextView) view.findViewById(R.id.fullname);
            holder.detail = (TextView) view.findViewById(R.id.detail);
            holder.totalPoint = (TextView) view.findViewById(R.id.totalPoints);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        final History history = (History) getItem(position);
        if(history.getBillImage() == null || history.getBillImage().equals(""))
            history.setBillImage(null);

        holder.time.setText(history.getTime());
        Picasso.with(mParentActivity).load(history.getBillImage()).placeholder(R.drawable.bill_ic).into(holder.billImage);
        holder.fullname.setText(history.getFullname());
        if(history.getType().equals("0")){ //buy award history
            holder.totalPoint.setText("-" + String.valueOf(history.getTotalPoint()));
            holder.totalPoint.setTextColor(mParentActivity.getResources().getColor(R.color.MaterialRed));
            holder.detail.setText(history.getUsername() + " used " + String.valueOf(history.getTotalPoint()) + " points to buy award(s)");
        }else if(history.getType().equals("1")){//got point from event history
            holder.totalPoint.setText("+" + String.valueOf(history.getTotalPoint()));
            holder.totalPoint.setTextColor(mParentActivity.getResources().getColor(R.color.AccentColor));
            holder.detail.setText(history.getUsername() + " got " + String.valueOf(history.getTotalPoint()) + " points from buying some products");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(history.getType().equals("1")){//got point from buying product
                    Intent i = new Intent(mParentActivity, UpdatePointDetailActivity.class);
                    i.putExtra(Global.HISTORY_OBJECT, history);
                    mParentActivity.startActivity(i);
                }else if(history.getType().equals("0")){ //buy award
                    Intent i = new Intent(mParentActivity, BuyAwardDetailActivity.class);
                    i.putExtra(Global.HISTORY_OBJECT, history);
                    mParentActivity.startActivity(i);
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        public TextView time;
        public ImageView billImage;
        public TextView fullname;
        public TextView detail;
        public TextView totalPoint;
    }
}
