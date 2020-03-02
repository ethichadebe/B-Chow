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

import Adapter.OrderItemAdapter;
import SingleItem.OrderItem;

public class OrdersFragment extends Fragment {
    private View vLeft, vRight, vBottomRight, vBottomLeft;
    private RelativeLayout rlLeft, rlRight;
    private RecyclerView mRecyclerView;
    private OrderItemAdapter mAdapter;
    private Dialog myDialog;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_orders, container, false);
        vBottomLeft = v.findViewById(R.id.vBottomLeft);
        vBottomRight = v.findViewById(R.id.vBottomRight);
        vLeft = v.findViewById(R.id.vLeft);
        vRight = v.findViewById(R.id.vRight);
        rlLeft = v.findViewById(R.id.rlLeft);
        rlRight = v.findViewById(R.id.rlRight);
        myDialog = new Dialog(getActivity());

        final ArrayList<OrderItem> orderItems = new ArrayList<>();

        //Loads shops starting with the one closest to user
        orderItems.add(new OrderItem(1, "Shop name", 315, "13 Jan 2020,15:45", "French, bacon, egg, russian",
                19.50, 3));
        orderItems.add(new OrderItem(1, "Shop name", 315, "03 Feb 2020, 15:45", "French, bacon, egg, russian",
                19.50, -1));
        orderItems.add(new OrderItem(1, "Shop name", 315, "13 Feb 2020, 15:45", "French, bacon, egg, russian",
                19.50, 1));
        orderItems.add(new OrderItem(1, "Shop name", 315, "13 Feb 2020, 15:45", "French, bacon, egg, russian",
                19.50, 2));
        orderItems.add(new OrderItem(1, "Shop name", 315, "13 Feb 2020, 15:45", "French, bacon, egg, russian",
                19.50, 4));
        orderItems.add(new OrderItem(1, "Shop name", 315, "13 Feb 2020, 15:45", "French, bacon, egg, russian",
                19.50, 5));
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

            @Override
            public void OnItemClickRate(int position) {
                ShowRatePopup(orderItems.get(position));
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

    public void ShowRatePopup(final OrderItem orderItem) {
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
                orderItem.setIntRating(1);
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
                orderItem.setIntRating(2);
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
                orderItem.setIntRating(3);
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
                orderItem.setIntRating(4);
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
                orderItem.setIntRating(5);
            }
        });



        cvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderItem.getIntRating() == -1){
                    ivStar1.setImageResource(R.drawable.star_empty_err);
                    ivStar2.setImageResource(R.drawable.star_empty_err);
                    ivStar3.setImageResource(R.drawable.star_empty_err);
                    ivStar4.setImageResource(R.drawable.star_empty_err);
                    ivStar5.setImageResource(R.drawable.star_empty_err);
                }else {
                    myDialog.dismiss();
                }
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}
