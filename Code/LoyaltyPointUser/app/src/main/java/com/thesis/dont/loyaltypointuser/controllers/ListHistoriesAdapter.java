package com.thesis.dont.loyaltypointuser.controllers;

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
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.History;

import java.util.ArrayList;

/**
 * Created by tinntt on 4/24/2015.
 */
public class ListHistoriesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<History> mHistories = new ArrayList<History>();

    private Activity mParentActivity;
    private String shopName, shopAddress;

    public ListHistoriesAdapter() {
    }

    public ListHistoriesAdapter(Context context, ArrayList<History> mHistories, String shopName, String shopAddress) {
        mInflater = LayoutInflater.from(context);
        this.mHistories = mHistories;
        mParentActivity = (Activity) context;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

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

        if (convertView == null) {
            view = mInflater.inflate(R.layout.histories_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();

            holder.time = (TextView) view.findViewById(R.id.time);
            holder.billImage = (ImageView) view.findViewById(R.id.billImage);
            holder.shopname = (TextView) view.findViewById(R.id.shopname);
            holder.detail = (TextView) view.findViewById(R.id.detail);
            holder.totalPoint = (TextView) view.findViewById(R.id.totalPoints);

            // save holder
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        final History history = (History) getItem(position);
        if (history.getBillImage() == null || history.getBillImage().equals(""))
            history.setBillImage(null);

        holder.time.setText(history.getTime());
        Picasso.with(mParentActivity).load(history.getBillImage()).placeholder(R.drawable.ic_about).into(holder.billImage);

        if (history.getType().equals("0")) { //buy award history
            holder.shopname.setTextColor(mParentActivity.getResources().getColor(R.color.MaterialRed));
            holder.shopname.setText("Buy Award");
            holder.totalPoint.setText("-" + String.valueOf(history.getTotalPoint()));
            holder.totalPoint.setTextColor(mParentActivity.getResources().getColor(R.color.MaterialRed));
            holder.detail.setText("You used " + String.valueOf(history.getTotalPoint()) + " points to buy award(s)");
        } else if (history.getType().equals("1")) {//got point from event history
            holder.shopname.setTextColor(mParentActivity.getResources().getColor(R.color.AccentColor));
            holder.shopname.setText("Add Point");
            holder.totalPoint.setText("+" + String.valueOf(history.getTotalPoint()));
            holder.totalPoint.setTextColor(mParentActivity.getResources().getColor(R.color.AccentColor));
            holder.detail.setText("You got " + String.valueOf(history.getTotalPoint()) + " points from buying some products");
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (history.getType().equals("1")) {//got point from buying product
                    Intent i = new Intent(mParentActivity, UpdatePointDetailActivity.class);
                    i.putExtra(Global.HISTORY_OBJECT, history);
                    if(shopName == null){
                        i.putExtra(Global.SHOP_NAME, history.getFullname());
                    }else{
                        i.putExtra(Global.SHOP_NAME, shopName);
                    }
                    if(shopAddress == null){
                        i.putExtra(Global.SHOP_ADDRESS, history.getPhone());
                    }else{
                        i.putExtra(Global.SHOP_ADDRESS, shopAddress);
                    }

                    mParentActivity.startActivity(i);
                } else if (history.getType().equals("0")) { //buy award
                    Intent i = new Intent(mParentActivity, BuyAwardDetailActivity.class);
                    i.putExtra(Global.HISTORY_OBJECT, history);
                    if(shopName == null){
                        i.putExtra(Global.SHOP_NAME, "");
                    }else{
                        i.putExtra(Global.SHOP_NAME, shopName);
                    }
                    if(shopAddress == null){
                        i.putExtra(Global.SHOP_ADDRESS, "");
                    }else{
                        i.putExtra(Global.SHOP_ADDRESS, shopAddress);
                    }
                    mParentActivity.startActivity(i);
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        public TextView time;
        public ImageView billImage;
        public TextView shopname;
        public TextView detail;
        public TextView totalPoint;
    }
}
