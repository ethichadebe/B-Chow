package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.PastOrderItemAdapter;
import Adapter.UpcomingOrderItemAdapter;
import SingleItem.PastOrderItem;
import SingleItem.UpcomingOrderItem;

public class OrdersFragment extends Fragment {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    private RelativeLayout rlLeft, rlRight;

    private RecyclerView mPastRecyclerView;
    private PastOrderItemAdapter mPastAdapter;
    private RecyclerView.LayoutManager mPastLayoutManager;

    private UpcomingOrderItemAdapter mUpcomingAdapter;
    private RecyclerView.LayoutManager mUpcomingLayoutManager;
    private RecyclerView mUpcomingRecyclerView;

    private Dialog myDialog;

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
        myDialog = new Dialog(getActivity());

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

    public void ShowRatePopup(final PastOrderItem pastOrderItem) {
        TextView tvCancel;
        final ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, testStar;
        CardView cvRate;
        myDialog.setContentView(R.layout.rate_popup);

        ivStar1 = myDialog.findViewById(R.id.ivStar1);
        ivStar2 = myDialog.findViewById(R.id.ivStar2);
        ivStar3 = myDialog.findViewById(R.id.ivStar3);
        ivStar4 = myDialog.findViewById(R.id.ivStar4);
        testStar = myDialog.findViewById(R.id.testStar);
        ivStar5 = myDialog.findViewById(R.id.ivStar5);
        tvCancel = myDialog.findViewById(R.id.tvCancel);
        cvRate = myDialog.findViewById(R.id.cvRate);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        ivStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivStar1.setImageResource(R.drawable.star);
                ivStar2.setImageResource(R.drawable.star_empty);
                ivStar3.setImageResource(R.drawable.star_empty);
                ivStar4.setImageResource(R.drawable.star_empty);
                ivStar5.setImageResource(R.drawable.star_empty);
                pastOrderItem.setIntRating(1);
            }
        });
        ivStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivStar1.setImageResource(R.drawable.star);
                ivStar2.setImageResource(R.drawable.star);
                ivStar3.setImageResource(R.drawable.star_empty);
                ivStar4.setImageResource(R.drawable.star_empty);
                ivStar5.setImageResource(R.drawable.star_empty);
                pastOrderItem.setIntRating(2);
            }
        });

        ivStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivStar1.setImageResource(R.drawable.star);
                ivStar2.setImageResource(R.drawable.star);
                ivStar3.setImageResource(R.drawable.star);
                ivStar4.setImageResource(R.drawable.star_empty);
                ivStar5.setImageResource(R.drawable.star_empty);
                pastOrderItem.setIntRating(3);
            }
        });

        ivStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivStar1.setImageResource(R.drawable.star);
                ivStar2.setImageResource(R.drawable.star);
                ivStar3.setImageResource(R.drawable.star);
                ivStar4.setImageResource(R.drawable.star);
                ivStar5.setImageResource(R.drawable.star_empty);
                pastOrderItem.setIntRating(4);
            }
        });

        ivStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivStar1.setImageResource(R.drawable.star);
                ivStar2.setImageResource(R.drawable.star);
                ivStar3.setImageResource(R.drawable.star);
                ivStar4.setImageResource(R.drawable.star);
                ivStar5.setImageResource(R.drawable.star);
                pastOrderItem.setIntRating(5);
            }
        });


        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pastOrderItem.getIntRating() == -1) {
                    ivStar1.setImageResource(R.drawable.star_empty_err);
                    ivStar2.setImageResource(R.drawable.star_empty_err);
                    ivStar3.setImageResource(R.drawable.star_empty_err);
                    ivStar4.setImageResource(R.drawable.star_empty_err);
                    ivStar5.setImageResource(R.drawable.star_empty_err);
                } else {
                    myDialog.dismiss();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void DisplayPastOrders(){
        final ArrayList<PastOrderItem> pastOrderItems = new ArrayList<>();
        //Loads past orders
        pastOrderItems.add(new PastOrderItem(1, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(3, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(5, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(4, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(2, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(-1, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        mPastRecyclerView.setHasFixedSize(true);
        mPastLayoutManager = new LinearLayoutManager(getActivity());
        mPastAdapter = new PastOrderItemAdapter(pastOrderItems);

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
                ShowRatePopup(pastOrderItems.get(position));
            }
        });
    }

    private void DisplayUpcomingOrders(){
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
}
