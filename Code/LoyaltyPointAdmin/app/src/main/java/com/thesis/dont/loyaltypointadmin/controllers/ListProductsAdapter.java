package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.Award;
import com.thesis.dont.loyaltypointadmin.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11120_000 on 02/04/15.
 */
public class ListProductsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Product> mProducts = new ArrayList<Product>();

    private Activity mParentActivity;

    public ListProductsAdapter(){}

    public ListProductsAdapter(Context context, List<Product> mProducts) {
        mInflater = LayoutInflater.from(context);
        this.mProducts = mProducts;
        mParentActivity = (Activity) context;
    }

    public void setListProducts(ArrayList<Product> listProducts) {
        mProducts = listProducts;
    }

    @Override
    public int getCount() {
        return mProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return mProducts.get(position);
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
            view = mInflater.inflate(R.layout.products_list_row, parent, false);

            // Create holder
            holder = new ViewHolder();
            holder.index = (TextView) view.findViewById(R.id.index);
            holder.quantity = (EditText) view.findViewById(R.id.quantity);
            holder.barcode = (EditText) view.findViewById(R.id.barcode);
            holder.productName = (TextView) view.findViewById(R.id.productName);

            // save holder
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(position);
        holder.index.setText(String.valueOf(position+1));
        holder.quantity.setText(String.valueOf(product.getQuantity()));
        holder.barcode.setText(product.getBarcode());
        holder.productName.setText(product.getName());

        return view;
    }

    public void add(Product product) {
        mProducts.add(product);
    }

    public class ViewHolder {
        public TextView index;
        public EditText quantity;
        public EditText barcode;
        public TextView productName;
    }
}
