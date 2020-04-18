package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import SingleItem.AdminOrderItem;
import SingleItem.ShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.setNewShop;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminOrderItem> OrderItems;
    private Dialog myDialog;
    private TextView tvOpen, tvUnavailable, tvClosed, tvEmpty, tvCompleteReg;
    private RelativeLayout rlLoad, rlError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        tvEmpty = findViewById(R.id.tvEmpty);
        OrderItems = new ArrayList<>();
        tvOpen = findViewById(R.id.tvOpen);
        tvUnavailable = findViewById(R.id.tvUnavailable);
        tvClosed = findViewById(R.id.tvClosed);
        tvCompleteReg = findViewById(R.id.tvCompleteReg);

        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
        tvUnavailable.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));

        if (!getNewShop().isActive()) {
            tvCompleteReg.setVisibility(View.VISIBLE);
        }

        tvCompleteReg.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterShopActivity.class));
        });

        GETOrders(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AdminOrderItemAdapter(OrderItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdminOrderItemAdapter.OnItemClickListener() {

            @Override
            public void onCancelClick(int position) {
                ShowConfirmationPopup(position);
            }

            @Override
            public void onDoneClick(int position) {

                OrderItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });


    }

    public void ShowConfirmationPopup(final int position) {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Are you sure?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            OrderItems.remove(position);
            mAdapter.notifyItemRemoved(position);
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void back(View view) {
        finish();
    }

    public void settings(View view) {
        startActivity(new Intent(OrdersActivity.this, ShopSettingsActivity.class));
    }

    public void open(View view) {
        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
        tvUnavailable.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
    }

    public void unavailable(View view) {
        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvUnavailable.setBackground(getResources().getDrawable(R.drawable.ripple_effect_yellow));
    }

    public void closed(View view) {
        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_red));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvUnavailable.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
    }

    private void GETOrders(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/orders/" + getNewShop().getIntID(), null,
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
                                String[] dateAndTime = Orders.getString("oRecievedAt").split("T");
                                OrderItems.add(new AdminOrderItem(Orders.getInt("oID"), Orders.getInt("oNumber"),
                                        dateAndTime[1].substring(0, 5), Orders.getDouble("oPrice"),
                                        Orders.getString("oIngredients"), Orders.getString("oStatus")));
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
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

}
