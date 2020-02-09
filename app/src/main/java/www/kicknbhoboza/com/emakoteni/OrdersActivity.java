package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.AdminOrderItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.AdminOrderItem;
import SingleItem.ShopItem;

public class OrdersActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    LinearLayout llBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        llBack = findViewById(R.id.llBack);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ArrayList<AdminOrderItem> OrderItems = new ArrayList<>();

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
                OrderItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onDoneClick(int position) {

                OrderItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });


    }
}
