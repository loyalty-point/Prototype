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
import com.thesis.dont.loyaltypointuser.models.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinntt on 3/26/2015.
 */
public class EventsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Event> mEvents = new ArrayList<Event>();

    private Activity mParentActivity;

    public EventsListAdapter(){}

    public EventsListAdapter(Context context, List<Event> mEvents) {
        mInflater = LayoutInflater.from(context);
        this.mEvents = mEvents;
        mParentActivity = (Activity) context;
    }

    public void setListEvents(ArrayList<Event> listEvents) {
        mEvents = listEvents;
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvents.get(position);
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
            view = mInflater.inflate(R.layout.events_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.eventName = (TextView) view.findViewById(R.id.eventName);
            holder.eventPoint = (TextView) view.findViewById(R.id.eventPoint);
            holder.eventImage = (ImageView) view.findViewById(R.id.eventImage);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Event event = (Event) getItem(position);
        Picasso.with(mParentActivity).load(event.getImage()).placeholder(R.drawable.ic_award).into(holder.eventImage);
        holder.eventName.setText(event.getName());
        holder.eventPoint.setText("Point: " + String.valueOf(event.getPoint()));

        return view;
    }

    public class ViewHolder {
        public TextView eventName;
        public TextView eventPoint;
        public ImageView eventImage;
    }
}
