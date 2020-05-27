package www.ethichadebe.com.loxion_beanery;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import Adapter.AdminOrderItemPastAdapter;
import Adapter.PastOrderItemAdapter;
import SingleItem.AdminOrderItem;
import SingleItem.AdminOrderItemPast;
import SingleItem.PastOrderItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.OrderActivity.oID;

public class PastOrderFragmentCustomer extends Fragment {
    private static final String TAG = "PastOrderFragmentCustom";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private RecyclerView mRecyclerView;
    private PastOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<PastOrderItem> pastOrderItems;
    private static PastOrderItem pastOrderItem;

    private RelativeLayout rlLoad, rlError, rlEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_order_customer, container, false);

        rlEmpty = v.findViewById(R.id.rlEmpty);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        mRecyclerView = v.findViewById(R.id.pastRecyclerView);

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        pastOrderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PastOrderItemAdapter(pastOrderItems);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        GETPastOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));

        mAdapter.setOnItemClickListener(new PastOrderItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemReorderClick(int position) {
                pastOrderItem = pastOrderItems.get(position);
                POSTOrder(pastOrderItem.getStrMenu(), pastOrderItem.getStrExtras(),
                        pastOrderItem.getsID(), pastOrderItem.getDblPrice());
            }

            @Override
            public void OnItemClickRate(int position) {
                pastOrderItem = pastOrderItems.get(position);
                startActivity(new Intent(getActivity(), RatingActivity.class));
            }
        });

        return v;

    }

    private void GETPastOrders(View vLine, View vLineGrey) {
        rlEmpty.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/orders/Past/" + getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                pastOrderItems.add(new PastOrderItem(Orders.getInt("oID"),
                                        Orders.getInt("sID"), Orders.getInt("oID"),
                                        Orders.getInt("oRating"), Orders.getString("sName"),
                                        Orders.getString("createdAt"), Orders.getString("oIngredients"),
                                        Orders.getString("oExtras"), Orders.getDouble("oPrice"),
                                        Orders.getString("oStatus")));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            rlEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    rlError.setVisibility(View.VISIBLE);
                    rlLoad.setVisibility(View.GONE);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    private void POSTOrder(String list, String extras, int sID, Double dblPrice) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/orders/Order",
                response -> {
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        oID = JSONResponse.getInt("data");
                        ShowLoadingPopup(myDialog, false);
                        if (oID != -1) {
                            startActivity(new Intent(getActivity(), OrderConfirmationActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oIngredients", list);
                params.put("oPrice", String.valueOf(dblPrice));
                params.put("sID", String.valueOf(sID));
                params.put("oExtras", extras);
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    static PastOrderItem getPastOrderItem() {
        return pastOrderItem;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
