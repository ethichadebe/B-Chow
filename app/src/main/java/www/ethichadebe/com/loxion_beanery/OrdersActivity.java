package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import SingleItem.AdminOrderItem;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.setNewShop;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<AdminOrderItem> OrderItems = new ArrayList<>();
    private Dialog myDialog;
    private TextView tvOpen, tvUnavailable, tvClosed, tvCompleteReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        tvOpen = findViewById(R.id.tvOpen);
        tvUnavailable = findViewById(R.id.tvUnavailable);
        tvClosed = findViewById(R.id.tvClosed);
        tvCompleteReg = findViewById(R.id.tvCompleteReg);

        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
        tvUnavailable.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));

        if (!getNewShop().isActive()){
            tvCompleteReg.setVisibility(View.VISIBLE);
        }

        tvCompleteReg.setOnClickListener(view -> {
            startActivity(new Intent(this,RegisterShopActivity.class));
        });
        OrderItems.add(new AdminOrderItem(1, 73, "13:24", 17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1, 74, "13:45", 17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1, 75, "13:52", 17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1, 76, "14:15", 17.50,
                "Chips, Burger, French, egg"));
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
}
