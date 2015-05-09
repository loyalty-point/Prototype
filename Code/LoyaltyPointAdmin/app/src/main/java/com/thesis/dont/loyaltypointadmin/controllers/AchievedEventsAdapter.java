package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.AchievedEvent;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.Product;

import java.util.ArrayList;

/**
 * Created by 11120_000 on 02/04/15.
 */
public class AchievedEventsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<AchievedEvent> mAchievedEvents = new ArrayList<AchievedEvent>();

    private Activity mParentActivity;

    public AchievedEventsAdapter(Context context, ArrayList<AchievedEvent> mAchievedEvents) {
        mInflater = LayoutInflater.from(context);
        this.mAchievedEvents = mAchievedEvents;
        mParentActivity = (Activity) context;
    }

    public ArrayList<AchievedEvent> getListAchievedEvents() {
        return mAchievedEvents;
    }

    public void setListAchievedEvents(ArrayList<AchievedEvent> listEvents) {
        mAchievedEvents = listEvents;
    }

    @Override
    public int getCount() {
        return mAchievedEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mAchievedEvents.get(position);
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
            view = mInflater.inflate(R.layout.achieved_events_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.name = (TextView) view.findViewById(R.id.eventName);
            holder.point = (TextView) view.findViewById(R.id.point);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        AchievedEvent event = (AchievedEvent) getItem(position);
        holder.quantity.setText(String.valueOf(event.getQuantity()));
        holder.name.setText(event.getEvent().getName());
        int point = event.getEvent().getPoint() * event.getQuantity();
        holder.point.setText(String.valueOf(point));

        return view;
    }

    public void add(AchievedEvent event) {
        mAchievedEvents.add(event);
    }

    public class ViewHolder {
        public TextView quantity;
        public TextView name;
        public TextView point;
    }
}
