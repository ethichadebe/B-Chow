package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import Adapter.UpcomingOrderItemAdapter;
import SingleItem.AdminOrderItem;
import SingleItem.UpcomingOrderItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class UpcomingOrderFragmentCustomer extends Fragment {
    private Dialog myDialog;
    private RecyclerView mRecyclerView;
    private UpcomingOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UpcomingOrderItem> upcomingOrderItems;
    private static UpcomingOrderItem upcomingOrderItem;

    static UpcomingOrderItem getUpcomingOrderItem() {
        return upcomingOrderItem;
    }

    static void setUpcomingOrderItem(UpcomingOrderItem upcomingOrderItem) {
        UpcomingOrderFragmentCustomer.upcomingOrderItem = upcomingOrderItem;
    }

    private RelativeLayout rlLoad, rlError, rlEmpty;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming_order_customer, container, false);

        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        rlEmpty = v.findViewById(R.id.rlEmpty);
        mRecyclerView = v.findViewById(R.id.upcomingRecyclerView);

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        upcomingOrderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new UpcomingOrderItemAdapter(upcomingOrderItems);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            upcomingOrderItem = upcomingOrderItems.get(position);
            startActivity(new Intent(getActivity(), OrderConfirmationActivity.class));
        });
        GETUpcomingOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));


        return v;
    }

    private void GETUpcomingOrders(View vLine, View vLineGrey) {
        rlEmpty.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/orders/Upcoming/" + getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                upcomingOrderItems.add(new UpcomingOrderItem(Orders.getInt("oID"),
                                        Orders.getString("sName"), Orders.getInt("oID"),
                                        Orders.getString("createdAt"), Orders.getString("oIngredients"),
                                        Orders.getString("oExtras"), Orders.getDouble("oPrice"),
                                        Orders.getString("oStatus"), new LatLng(Orders.getDouble("sLatitude"),
                                        Orders.getDouble("sLongitude"))));
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
        requestQueue.add(objectRequest);
    }

}
