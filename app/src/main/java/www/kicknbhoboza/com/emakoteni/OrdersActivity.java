package www.kicknbhoboza.com.emakoteni;

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

import org.w3c.dom.Text;

import java.util.ArrayList;

import Adapter.AdminOrderItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.AdminOrderItem;
import SingleItem.ShopItem;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final ArrayList<AdminOrderItem> OrderItems = new ArrayList<>();;
    private Dialog myDialog;
    private LinearLayout llBack, llSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        myDialog = new Dialog(this);
        llBack = findViewById(R.id.llBack);
        llSettings = findViewById(R.id.llSettings);
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrdersActivity.this, ShopSettingsActivity.class));
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        OrderItems.add(new AdminOrderItem(1,73,"13:24",17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1,74,"13:45",17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1,75,"13:52",17.50,
                "Chips, Burger, French, egg"));
        OrderItems.add(new AdminOrderItem(1,76,"14:15",17.50,
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
                ShowPopup(position);
            }

            @Override
            public void onDoneClick(int position) {

                OrderItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });


    }

    public void ShowPopup(final int position){
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.confirmation_popup);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("Are you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                OrderItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

        cvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
