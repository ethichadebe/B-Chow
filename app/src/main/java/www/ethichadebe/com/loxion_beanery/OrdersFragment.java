package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.PastOrderItemAdapter;
import Adapter.UpcomingOrderItemAdapter;
import SingleItem.UpcomingOrderItem;

import static www.ethichadebe.com.loxion_beanery.HomeFragment.DisplayPastOrders;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getPastOrderItems;

public class OrdersFragment extends Fragment {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    private RelativeLayout rlLeft, rlRight;

    private RecyclerView mPastRecyclerView;
    private PastOrderItemAdapter mPastAdapter;
    private RecyclerView.LayoutManager mPastLayoutManager;

    private UpcomingOrderItemAdapter mUpcomingAdapter;
    private RecyclerView.LayoutManager mUpcomingLayoutManager;
    private RecyclerView mUpcomingRecyclerView;
    private static int Position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_orders, container, false);
        vBottomLeft = v.findViewById(R.id.vBottomLeft);
        vBottomRight = v.findViewById(R.id.vBottomRight);
        vLeft = v.findViewById(R.id.vLeft);
        vRight = v.findViewById(R.id.vRight);
        rlLeft = v.findViewById(R.id.rlLeft);
        rlRight = v.findViewById(R.id.rlRight);
        mUpcomingRecyclerView = v.findViewById(R.id.upcomingRecyclerView);
        mPastRecyclerView = v.findViewById(R.id.pastRecyclerView);

        setVisibility(View.VISIBLE, View.GONE, mUpcomingRecyclerView, mPastRecyclerView);
        DisplayPastOrders();
        rlLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.VISIBLE, View.GONE, mUpcomingRecyclerView, mPastRecyclerView);
                DisplayPastOrders();
            }
        });

        rlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE, View.VISIBLE, mPastRecyclerView, mUpcomingRecyclerView);
                DisplayUpcomingOrders();
            }
        });
        mPastRecyclerView.setHasFixedSize(true);
        mPastLayoutManager = new LinearLayoutManager(getActivity());
        mPastAdapter = new PastOrderItemAdapter(getPastOrderItems());

        mPastRecyclerView.setLayoutManager(mPastLayoutManager);
        mPastRecyclerView.setAdapter(mPastAdapter);

        mPastAdapter.setOnItemClickListener(new PastOrderItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*shopItems.get(position)*/
                startActivity(new Intent(getActivity(), ShopHomeActivity.class));
            }

            @Override
            public void OnItemClickRate(int position) {
                Position = position;
                startActivity(new Intent(getActivity(), RatingActivity.class));
            }
        });
        return v;
    }

    private void setVisibility(int Left, int Right, RecyclerView recyclerViewGONE,
                               RecyclerView recyclerViewVISIBLE) {
        vRight.setVisibility(Right);
        vLeft.setVisibility(Left);
        vBottomLeft.setVisibility(Left);
        vBottomRight.setVisibility(Right);

        recyclerViewGONE.setVisibility(View.GONE);
        recyclerViewVISIBLE.setVisibility(View.VISIBLE);
    }


    private void DisplayUpcomingOrders() {
        final ArrayList<UpcomingOrderItem> upcomingOrderItems = new ArrayList<>();

        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        upcomingOrderItems.add(new UpcomingOrderItem(1, "Shop name", 315,
                "15:45", "French, bacon, egg, russian", 19.50));
        mUpcomingRecyclerView.setHasFixedSize(true);
        mUpcomingLayoutManager = new LinearLayoutManager(getActivity());
        mUpcomingAdapter = new UpcomingOrderItemAdapter(upcomingOrderItems);

        mUpcomingRecyclerView.setLayoutManager(mUpcomingLayoutManager);
        mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);

        mUpcomingAdapter.setOnItemClickListener(new UpcomingOrderItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickTrack(int position) {
                startActivity(new Intent(getActivity(), OrderConfirmationActivity.class));
            }
        });
    }

    public static int getPosition() {
        return Position;
    }
}
