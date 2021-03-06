package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.AchievedEvent;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.History;
import com.thesis.dont.loyaltypointuser.models.UserModel;

import java.util.ArrayList;

public class UpdatePointDetailActivity extends ActionBarActivity {
    TextView dateTv, shopNameTv, shopAddressTv, billCodeTv, totalPointTv;
    ImageView billImageIv;
    ListView detailLv;
    ButtonRectangle backBtn, deleteBtn;
    History history;
    String shopName, shopAddress;

    AchievedEventsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_point_detail);
        Intent i = getIntent();
        history = i.getParcelableExtra(Global.HISTORY_OBJECT);
        shopName = i.getStringExtra(Global.SHOP_NAME);
        shopAddress = i.getStringExtra(Global.SHOP_ADDRESS);

        dateTv = (TextView) findViewById(R.id.date);
        shopNameTv = (TextView) findViewById(R.id.shopName);
        shopAddressTv = (TextView) findViewById(R.id.shopAddress);
        billCodeTv = (TextView) findViewById(R.id.billCode);
        billImageIv = (ImageView) findViewById(R.id.billImage);
        detailLv = (ListView) findViewById(R.id.listEvent);
//        backBtn = (ButtonRectangle) findViewById(R.id.printBtn);
//        backBtn.setVisibility(View.INVISIBLE);
//        backBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UpdatePointDetailActivity.this.finish();
//            }
//        });
        deleteBtn = (ButtonRectangle) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePointDetailActivity.this.finish();
            }
        });
        totalPointTv = (TextView) findViewById(R.id.totalPoint);

        dateTv.setText(history.getTime());
        shopNameTv.setText(shopName);
        shopAddressTv.setText(shopAddress);
        billCodeTv.setText("billcode: " + history.getId());
        totalPointTv.setText("total point: " + String.valueOf(history.getTotalPoint()));

        if(history.getBillImage() == null || history.getBillImage().equals(""))
            history.setBillImage(null);

        Picasso.with(this).load(history.getBillImage()).placeholder(R.drawable.card_img2).into(billImageIv);


        mAdapter = new AchievedEventsAdapter(this, new ArrayList<AchievedEvent>());
        detailLv.setAdapter(mAdapter);

        getAchievedEventsData();
    }

    private void getAchievedEventsData() {
        UserModel.getAchievedEventList(Global.userToken, history.getId(), new UserModel.OngetAchievedEventListResult() {
            @Override
            public void onSuccess(ArrayList<AchievedEvent> listAchievedEvents) {
                int idx = -1, point = history.getTotalPoint();
                for (int i = 0; i < listAchievedEvents.size(); i++) {
                    if (listAchievedEvents.get(i).getEvent().getType() == 1) //get event type 1
                        idx = i;
                    point = point - listAchievedEvents.get(i).getEvent().getPoint() * listAchievedEvents.get(i).getQuantity();
                }
                if (idx != -1) {
                    listAchievedEvents.get(idx).getEvent().setPoint(point);
                }
                mAdapter.setListAchievedEvents(listAchievedEvents);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdatePointDetailActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


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

            if (convertView == null) {
                view = mInflater.inflate(R.layout.achieved_events_list_row, parent, false);

                // Create holder
                holder = new ViewHolder();
                holder.quantity = (TextView) view.findViewById(R.id.quantity);
                holder.name = (TextView) view.findViewById(R.id.eventName);
                holder.point = (TextView) view.findViewById(R.id.point);
                holder.pointPerEvent = (TextView) view.findViewById(R.id.pointPerEvent);

                // save holder
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            AchievedEvent event = (AchievedEvent) getItem(position);
            holder.quantity.setText(String.valueOf(event.getQuantity()));
            holder.name.setText(event.getEvent().getName());

            int point = 0;
            if(event.getEvent().getType() == 1){
                point = event.getEvent().getPoint();
                holder.quantity.setText(String.valueOf(point * event.getEvent().getRatio()));
                holder.pointPerEvent.setText(String.valueOf(1/event.getEvent().getRatio()));
            }else{
                point = event.getEvent().getPoint() * event.getQuantity();
                holder.pointPerEvent.setText(String.valueOf(event.getEvent().getPoint()));
            }
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
            public TextView pointPerEvent;
        }
    }

}
