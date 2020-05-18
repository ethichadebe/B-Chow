package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.AdminOrderItemPast;
import SingleItem.PastOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.HelperMethods.setStarRating;

public class AdminOrderItemPastAdapter extends RecyclerView.Adapter<AdminOrderItemPastAdapter.OrderViewHolder> {

    private ArrayList<AdminOrderItemPast> orderList;

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPrice, tvMenu, tvExtras, tvOrderNum, tvTime, tvFeedback;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private LinearLayout llStars;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvExtras = itemView.findViewById(R.id.tvExtras);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            llStars = itemView.findViewById(R.id.llStars);

            ivStar1 = itemView.findViewById(R.id.ivStar1);
            ivStar2 = itemView.findViewById(R.id.ivStar2);
            ivStar3 = itemView.findViewById(R.id.ivStar3);
            ivStar4 = itemView.findViewById(R.id.ivStar4);
            ivStar5 = itemView.findViewById(R.id.ivStar5);

        }
    }

    public AdminOrderItemPastAdapter(ArrayList<AdminOrderItemPast> shopList) {
        this.orderList = shopList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_item_past, parent, false);

        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        AdminOrderItemPast item = orderList.get(position);

        holder.tvPrice.setText("R" + item.getDblPrice() + "0");
        holder.tvMenu.setText(item.getStrMenu());
        holder.tvExtras.setText(item.getStrExtras());
        holder.tvOrderNum.setText("Order number: " + item.getIntOderNum());
        holder.tvTime.setText(item.getStrTime());
        holder.tvFeedback.setText(item.getStrFeedback());
        setStarRating(item.getIntRating(), holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5);


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
