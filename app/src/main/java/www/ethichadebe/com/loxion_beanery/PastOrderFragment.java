package www.ethichadebe.com.loxion_beanery;


import android.app.Dialog;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import SingleItem.AdminOrderItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class PastOrderFragment extends Fragment {
    private Dialog myDialog;
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminOrderItem> OrderItems;

    private TextView tvEmpty;
    private RelativeLayout rlLoad, rlError;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_order, container, false);

        tvEmpty = v.findViewById(R.id.tvEmpty);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        OrderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdminOrderItemAdapter(OrderItems);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdminOrderItemAdapter.OnItemClickListener() {

            @Override
            public void onCancelClick(int position) {
                ShowConfirmationPopup(position);
            }

            @Override
            public void onDoneClick(int position) {
                PUTReady(position);
            }

            @Override
            public void onCollectedClick(int position) {
                PUTCollected(position, myDialog);
            }
        });

        GETOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));


        return v;

    }
    private void ShowConfirmationPopup(final int position) {
        TextView tvCancel, tvMessage;
        Button btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Are you sure?");

        btnYes.setOnClickListener(view -> PUTCancel(position, myDialog));

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    private void GETOrders(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/orders/" + getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    Location location = new Location("");
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                OrderItems.add(new AdminOrderItem(Orders.getInt("oID"), Orders.getInt("oID"),
                                        Orders.getString("oRecievedAt"), Orders.getString("oIngredients"),
                                        Orders.getString("oExtras"), Orders.getString("oStatus"),
                                        Orders.getDouble("oPrice")));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
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
    private void PUTCancel(int position, Dialog myDialog) {
        HelperMethods.ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Cancel/" + OrderItems.get(position).getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    myDialog.dismiss();
                    OrderItems.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                }, error -> {
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    private void PUTReady(int position) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Ready/" + OrderItems.get(position).getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    OrderItems.get(position).setStrStatus("Ready for collection");
                    mAdapter.notifyItemChanged(position);
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                }, error -> {
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
    private void PUTCollected(int position, Dialog myDialog) {
        HelperMethods.ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Collected/" + OrderItems.get(position).getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    myDialog.dismiss();
                    OrderItems.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                }, error -> {
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        requestQueue.add(stringRequest);
    }
}
