package com.thesis.dont.loyaltypointuser.controllers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFloat;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.Award;
import com.thesis.dont.loyaltypointuser.models.AwardModel;
import com.thesis.dont.loyaltypointuser.models.Global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import butterknife.ButterKnife;

import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.models.History;
import com.thesis.dont.loyaltypointuser.models.ShopModel;
import com.thesis.dont.loyaltypointuser.models.UserModel;

import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;


public class ShopHistoryFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_SHOPID = "shop_id";
    private static final String ARG_SHOPNAME = "shop_name";
    private static final String ARG_SHOPADDRESS = "shop_address";
    public static final String AWARD_OBJECT = "award_object";

    private int position;
    private String shopId,cardId;
    private String shopName;
    private String shopAddress;

    ListView mListView;
    Activity mParentActivity;
    ListHistoriesAdapter mAdapter;

    ArrayList<History> mOriginalList;

    Spinner mTimeFilterSpinner, mTypeFilterSpinner, mSortTypeSpinner;
    int mTimeFilterValue = 0, mTypeFilterValue = 0, mSortTypeValue = 0;

    FancyButton mSortOrderBtn;
    int mSortOrderValue = 1; // Decrease

    ProgressDialog mDialog;

    public ShopHistoryFragment() {
        // Required empty public constructor
    }

    public ShopHistoryFragment(int position, String shopName, String shopAddress){
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        b.putString(ARG_SHOPNAME, shopName);
        b.putString(ARG_SHOPADDRESS, shopAddress);
        this.setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        shopName = getArguments().getString(ARG_SHOPNAME);
        shopAddress = getArguments().getString(ARG_SHOPADDRESS);
    }

    @Override
    public void onResume() {
        super.onResume();
        getListHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_history, container, false);
        ButterKnife.inject(this, rootView);
        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParentActivity = getActivity();
        shopId = ((ShopDetailActivity)getActivity()).getCurrentShop().getId();
        cardId = ((ShopDetailActivity)getActivity()).getCurrentCardId();
        // init dialog
        mDialog = new ProgressDialog(mParentActivity);
        mDialog.setTitle("Reloading list histories");
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);

        mOriginalList = new ArrayList<History>();

        // init list histories
        mListView = (ListView) mParentActivity.findViewById(R.id.listHistories);
        mAdapter = new ListHistoriesAdapter(mParentActivity, new ArrayList<History>(), shopName, shopAddress);
        mListView.setAdapter(mAdapter);

        // time filter spinner
        mTimeFilterSpinner = (Spinner) mParentActivity.findViewById(R.id.timeFilterSpinner);

        ArrayAdapter<CharSequence> timeFilterAdapter = ArrayAdapter.createFromResource(mParentActivity,
                R.array.timeFilterArray, android.R.layout.simple_spinner_item);

        timeFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTimeFilterSpinner.setAdapter(timeFilterAdapter);
        mTimeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTimeFilterValue = position;
                reloadListHistories();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //mTimeFilterValue = timeFilterAdapter.getCount() - 1;

        // type filter spinner
        mTypeFilterSpinner = (Spinner) mParentActivity.findViewById(R.id.typeFilterSpinner);

        ArrayAdapter<CharSequence> typeFilterAdapter = ArrayAdapter.createFromResource(mParentActivity,
                R.array.typeFilterArray, android.R.layout.simple_spinner_item);

        typeFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mTypeFilterSpinner.setAdapter(typeFilterAdapter);
        mTypeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTypeFilterValue = position;
                reloadListHistories();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTypeFilterValue = typeFilterAdapter.getCount() - 1;
        mTypeFilterSpinner.setSelection(mTypeFilterValue);

        // type filter spinner
        mSortTypeSpinner = (Spinner) mParentActivity.findViewById(R.id.sortTypeSpinner);
        mSortTypeSpinner.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> sortTypeAdapter = ArrayAdapter.createFromResource(mParentActivity,
                R.array.sortTypeArray, android.R.layout.simple_spinner_item);

        sortTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSortTypeSpinner.setAdapter(sortTypeAdapter);
        mSortTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSortTypeValue = position;
                sortListHistories(mAdapter.getListHistories());

                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSortTypeValue = 0;

        // sort order button
        mSortOrderBtn = (FancyButton) mParentActivity.findViewById(R.id.sortOrderBtn);
        mSortOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortOrderValue = 1 - mSortOrderValue;

                // change icon
                if(mSortOrderValue == 1) // decrease
                    mSortOrderBtn.setIconResource(R.drawable.ic_down_light);
                else
                    mSortOrderBtn.setIconResource(R.drawable.ic_up_light);

                // reverse and reload list
                reverseAndReloadListHistories();
            }
        });
    }

    private void reverseAndReloadListHistories() {
        Collections.reverse(mAdapter.getListHistories());
        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getListHistory() {
        UserModel.getListHistory(Global.userToken, shopId, new UserModel.OnGetListHistoryResult() {
            @Override
            public void onSuccess(ArrayList<History> listHistories) {
                mOriginalList = listHistories;
                sortListHistories(mOriginalList);

                reloadListHistories();

                /*mAdapter.setListHistories(listHistories);
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                    }
                });*/
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Get listAwards không thành công
                        Toast.makeText(mParentActivity, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void sortListHistories(ArrayList<History> listHistories) {
        if(mSortTypeValue == 0) { // Time
            Collections.sort(listHistories, new Comparator<History>() {
                @Override
                public int compare(History item1, History item2) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    // get time 1
                    Calendar time1 = Calendar.getInstance();
                    try {
                        time1.setTime(dateFormat.parse(item1.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // get time 2
                    Calendar time2 = Calendar.getInstance();
                    try {
                        time2.setTime(dateFormat.parse(item2.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long delta = time2.getTimeInMillis() - time1.getTimeInMillis();
                    if(delta > 0)
                        return 1;
                    else if(delta < 0)
                        return -1;
                    else
                        return 0;
                }
            });
        }else { // User name
            Collections.sort(listHistories, new Comparator<History>() {
                @Override
                public int compare(History item1, History item2) {
                    return item1.getUsername().compareTo(item2.getUsername());
                }
            });
        }
    }

    public void reloadListHistories() {
        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDialog.show();
            }
        });

        ArrayList<History> listHistories = new ArrayList<History>();

        for(int i=0; i<mOriginalList.size(); i++) {
            if(isItemAppropriate(mOriginalList.get(i)))
                listHistories.add(mOriginalList.get(i));
        }

        sortListHistories(listHistories);
        mAdapter.setListHistories(listHistories);
        mParentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mDialog.hide();
            }
        });
    }

    public boolean isItemAppropriate(History item) {

        // type filter
        if(mTypeFilterValue != 2 && mTypeFilterValue != Integer.valueOf(item.getType()))
            return false;

        // Lấy thời gian hiện tại
        /*DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String currentTime = dateFormat.format(date); //2014/08/06 15:59:48*/

        if(mTimeFilterValue == 4) { // ALL
            return true;
        }

        // get current time
        Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(Calendar.YEAR);
        int currentMonth = currentTime.get(Calendar.MONTH);
        int currentWeek = currentTime.get(Calendar.WEEK_OF_YEAR);
        int currentDay = currentTime.get(Calendar.DAY_OF_YEAR);

        // get history time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar historyTime = Calendar.getInstance();
        try {
            historyTime.setTime(dateFormat.parse(item.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int historyYear = historyTime.get(Calendar.YEAR);
        int historyMonth = historyTime.get(Calendar.MONTH);
        int historyWeek = historyTime.get(Calendar.WEEK_OF_YEAR);
        int historyDay = historyTime.get(Calendar.DAY_OF_YEAR);

        if(currentYear != historyYear)
            return false;

        // time filter
        switch (mTimeFilterValue) {
            case 0: // Today
                if(currentDay != historyDay)
                    return false;
                break;
            case 1: // This Week
                if(currentWeek != historyWeek)
                    return false;
                break;
            case 2: // This Month
                if(currentMonth != historyMonth)
                    return false;
                break;
            case 3: // This Year
                if(currentYear != historyYear)
                    return false;
                break;
            default:
                break;
        }

        return true;
    }
}
