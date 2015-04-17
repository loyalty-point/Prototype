package com.thesis.dont.loyaltypointadmin.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.AwardHistory;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.TicketModel;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class UserTicketsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_USERID = "user_id";
    private static final String ARG_SHOPID = "shop_id";

    int position;
    String userId;
    String shopId;

    ListView mListView;
    TicketsListAdapter mAdapter;
    ArrayList<AwardHistory> listTickets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_tickets, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    public UserTicketsFragment(int position, String userId, String shopId){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_USERID, userId);
        b.putString(ARG_SHOPID, shopId);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        userId = getArguments().getString(ARG_USERID);
        shopId = getArguments().getString(ARG_SHOPID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new TicketsListAdapter(getActivity(), new ArrayList<AwardHistory>());
        mListView = (ListView) getActivity().findViewById(R.id.listTickets);
        mListView.setAdapter(mAdapter);
        // Load dữ liệu lên list
        getListTickets();
    }

    public void getListTickets() {
        TicketModel.getUserTicket(Global.userToken, userId, shopId, new TicketModel.OnGetUserTicket() {
            @Override
            public void onSuccess(ArrayList<AwardHistory> listTickets) {
                mAdapter.setListTickets(listTickets);
                UserTicketsFragment.this.listTickets = listTickets;
                UserTicketsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(UserTicketsFragment.this.getActivity(), error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


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
        public View getView(final int position, View convertView, ViewGroup parent) {
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

            final AwardHistory award = (AwardHistory) getItem(position);
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
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                    mDialogBuilder.setTitle("Identity number confirm");
                    mDialogBuilder.setCancelable(false);

                    // Set up the input
                    final EditText IdentityNumberEditText = new EditText(mParentActivity);

                    // set properties for quantityEditText
                    IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    IdentityNumberEditText.setGravity(Gravity.CENTER);
                    IdentityNumberEditText.setHint("User's Identity Number");

                    mDialogBuilder.setView(IdentityNumberEditText);

                    //initDialog();
                    // Set listeners for dialog's buttons
                    mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserModel.checkIdentityNumberUser(Global.userToken, award.getUsername(), IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                @Override
                                public void onSuccess() {
                                    TicketModel.deleteUserTicket(Global.userToken, shopId, userId, award.getId(), new TicketModel.OnDeleteUserTicket() {
                                        @Override
                                        public void onSuccess() {
                                            listTickets.remove(position);
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TicketsListAdapter.this.notifyDataSetChanged();
                                                    new AlertDialog.Builder(mParentActivity)
                                                            .setTitle("Success")
                                                            .setMessage("Award's sold!")
                                                            .setIcon(android.R.drawable.ic_dialog_info)
                                                            .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // do nothing
                                                                }
                                                            })
                                                            .show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(String error) {

                                        }
                                    });
                                }

                                @Override
                                public void onError(final String error) {
                                    mParentActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            new AlertDialog.Builder(mParentActivity)
                                                    .setTitle("Error")
                                                    .setMessage(error)
                                                    .setIcon(android.R.drawable.ic_dialog_info)
                                                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // do nothing
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });

                                }
                            });
                        }
                    });
                    mDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    mDialogBuilder.show();
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
}
