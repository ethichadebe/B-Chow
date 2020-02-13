package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.OrderItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.OrderItem;
import SingleItem.ShopItem;

public class OrdersFragment extends Fragment {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    private RelativeLayout rlLeft, rlRight;
    private RecyclerView mRecyclerView;
    private OrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_orders,container, false);
        vBottomLeft = v.findViewById(R.id.vBottomLeft);
        vBottomRight = v.findViewById(R.id.vBottomRight);
        vLeft = v.findViewById(R.id.vLeft);
        vRight = v.findViewById(R.id.vRight);
        rlLeft = v.findViewById(R.id.rlLeft);
        rlRight = v.findViewById(R.id.rlRight);

        final ArrayList<OrderItem> orderItems = new ArrayList<>();

        //Loads shops starting with the one closest to user
        orderItems.add(new OrderItem(1, "Shop name",315,"13 Jan 2020,15:45","French, bacon, egg, russian",
                19.50,3));
        orderItems.add(new OrderItem(1, "Shop name",315,"03 Feb 2020, 15:45","French, bacon, egg, russian",
                19.50,-1));
        orderItems.add(new OrderItem(1, "Shop name",315,"13 Feb 2020, 15:45","French, bacon, egg, russian",
                19.50,1));
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new OrderItemAdapter(orderItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OrderItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*shopItems.get(position)*/
                startActivity(new Intent(getActivity(), ShopHomeActivity.class));
            }
        });


        setVisibility(View.VISIBLE, View.GONE);

        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.VISIBLE, View.GONE);
            }
        });
        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE, View.VISIBLE);
            }
        });


        return v;
    }
    private void setVisibility(int Left, int Right) {
        vRight.setVisibility(Right);
        vLeft.setVisibility(Left);
        vBottomLeft.setVisibility(Left);
        vBottomRight.setVisibility(Right);
    }
}
