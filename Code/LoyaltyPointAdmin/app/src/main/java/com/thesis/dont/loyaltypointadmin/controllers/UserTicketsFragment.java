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

import com.gc.materialdesign.views.ButtonRectangle;
import com.squareup.picasso.Picasso;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.models.AwardHistory;
import com.thesis.dont.loyaltypointadmin.models.Event;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.TicketModel;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    Activity mParentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_tickets, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    public UserTicketsFragment() {}

    public UserTicketsFragment(int position, String userId, String shopId) {
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

        mParentActivity = getActivity();

        mAdapter = new TicketsListAdapter(mParentActivity, new ArrayList<AwardHistory>());
        mListView = (ListView) mParentActivity.findViewById(R.id.listTickets);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        getListTickets();
    }

    public void getListTickets() {
        TicketModel.getUserTicket(Global.userToken, userId, shopId, new TicketModel.OnGetUserTicket() {
            @Override
            public void onSuccess(ArrayList<AwardHistory> listTickets) {
                mAdapter.setListTickets(listTickets);
                UserTicketsFragment.this.listTickets = listTickets;
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(final String error) {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    public class TicketsListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<AwardHistory> mAwardHistorys = new ArrayList<AwardHistory>();

        private Activity mParentActivity;

        public TicketsListAdapter() {
        }

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

            if (convertView == null) {
                view = mInflater.inflate(R.layout.ticket_list_row, parent, false);

                holder = new ViewHolder();

                holder.time = (TextView) view.findViewById(R.id.time);
                holder.awardImage = (ImageView) view.findViewById(R.id.awardImage);
                holder.awardName = (TextView) view.findViewById(R.id.awardName);
                holder.quantity = (TextView) view.findViewById(R.id.quantity);
                holder.sell = (ButtonRectangle) view.findViewById(R.id.sell);
                holder.cancel = (ButtonRectangle) view.findViewById(R.id.cancel);
                // save holder
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            final AwardHistory ticket = (AwardHistory) getItem(position);
            if (ticket.getAwardImage() == null || ticket.getAwardImage().equals(""))
                ticket.setAwardImage(null);

            if (ticket.getShopImage() == null || ticket.getShopImage().equals(""))
                ticket.setShopImage(null);

            holder.time.setText(ticket.getTime());
            Picasso.with(mParentActivity).load(ticket.getAwardImage()).placeholder(R.drawable.ic_award).into(holder.awardImage);
            holder.awardName.setText(ticket.getAwardName());
            holder.quantity.setText("Number of award: " + String.valueOf(ticket.getQuantity()));
            //serve order case.
            holder.sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                    mDialogBuilder.setTitle("Give ticket");
                    mDialogBuilder.setCancelable(false);

                    // Set up the input
                    final EditText IdentityNumberEditText = new EditText(mParentActivity);

                    // set properties for quantityEditText
                    IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    IdentityNumberEditText.setGravity(Gravity.CENTER);
                    IdentityNumberEditText.setHint("User's Identity Number Confirm");

                    mDialogBuilder.setView(IdentityNumberEditText);

                    //initDialog();
                    // Set listeners for dialog's buttons
                    mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //check identity number, if it's invalid. give user gift and delete ticket
                            UserModel.checkIdentityNumberUser(Global.userToken, ticket.getUsername(), IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                @Override
                                public void onSuccess() {
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    String time = dateFormat.format(date);
                                    //delete award ticket when identity number checking was ok.
                                    TicketModel.deleteUserTicket(Global.userToken, ticket.getId(), ticket.getAwardID(), shopId, userId, time, ticket.getQuantity(), ticket.getTotal_point(), new TicketModel.OnDeleteUserTicket() {
                                        @Override
                                        public void onSuccess() {
                                            listTickets.remove(position);
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TicketsListAdapter.this.notifyDataSetChanged();
                                                    new AlertDialog.Builder(mParentActivity)
                                                            .setTitle("Successfully")
                                                            .setMessage("Award has been sold!")
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
            //Cancel order case.
            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(mParentActivity);
                    mDialogBuilder.setTitle("Cancel Orders");
                    mDialogBuilder.setCancelable(false);

                    // Set up the input
                    final EditText IdentityNumberEditText = new EditText(mParentActivity);

                    // set properties for quantityEditText
                    IdentityNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    IdentityNumberEditText.setGravity(Gravity.CENTER);
                    IdentityNumberEditText.setHint("User's Identity Number Confirm");

                    mDialogBuilder.setView(IdentityNumberEditText);

                    //initDialog();
                    // Set listeners for dialog's buttons
                    mDialogBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Check identity number of user, if it's invalid. the order will be cancel.
                            UserModel.checkIdentityNumberUser(Global.userToken, ticket.getUsername(), IdentityNumberEditText.getText().toString(), new UserModel.OnCheckIdentityNumberResult() {
                                @Override
                                public void onSuccess() {
                                    //Cancel the order if the indentity number checking was ok.
                                    TicketModel.cancelUserTicket(Global.userToken, shopId, userId, ticket.getId(), new TicketModel.OnCancelUserTicket() {
                                        @Override
                                        public void onSuccess() {
                                            listTickets.remove(position);
                                            mParentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TicketsListAdapter.this.notifyDataSetChanged();
                                                    new AlertDialog.Builder(mParentActivity)
                                                            .setTitle("Cancel Successfully")
                                                            .setMessage("User has got their point back!")
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

                                @Override
                                public void onError(final String error) {

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

            return view;
        }

        public class ViewHolder {
            public TextView time;
            public ImageView awardImage;
            public TextView awardName;
            public TextView quantity;
            public ButtonRectangle sell;
            public ButtonRectangle cancel;
        }
    }
}
