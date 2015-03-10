package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Shop;

import java.util.ArrayList;

/**
 * Created by tinntt on 3/6/2015.
 */
public class CustomShopListAdapter extends BaseAdapter implements View.OnClickListener {
    /**
     * ******** Declare Used Variables ********
     */
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    Shop tempValues = null;
    int i = 0;

    /**
     * **********  CustomAdapter Constructor ****************
     */
    public CustomShopListAdapter(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data = d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //Get array size
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //view holder contain data of shop card
    public static class ViewHolder {

        public TextView text;
        public TextView text1;
        public TextView textWide;
        public TextView image;

    }

    //Show view of shop card
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.shops_list, null);
            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            holder.text.setText("No Data");

        } else { //have data, so set data to view
            tempValues = null;
            tempValues = (Shop) data.get(position);
            holder.text.setText(tempValues.getName());
//            vi.setOnClickListener(new OnItemClickListener(position));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }
//
//    /**
//     * ****** Called when Item click in ListView ***********
//     */
//    private class OnItemClickListener implements View.OnClickListener {
//        private int mPosition;
//
//        OnItemClickListener(int position) {
//            mPosition = position;
//        }
//
//        @Override
//        public void onClick(View arg0) {
//
//
//            ShopsListActivity sct = (ShopsListActivity) activity;
//
//            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
//
////            sct.onItemClick(mPosition);
//        }
//    }
}
