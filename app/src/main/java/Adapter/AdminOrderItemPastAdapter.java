package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.AdminOrderItemPast;
import SingleItem.PastOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

public class AdminOrderItemPastAdapter extends RecyclerView.Adapter<AdminOrderItemPastAdapter.OrderViewHolder> {

    private ArrayList<AdminOrderItemPast> orderList;

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPrice, tvMenu, tvExtras, tvOrderNum, tvTime, tvFeedback;
        private RatingBar rbRating;
        private LinearLayout llOrderItem;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvExtras = itemView.findViewById(R.id.tvExtras);
            tvFeedback = itemView.findViewById(R.id.tvFeedback);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            llOrderItem = itemView.findViewById(R.id.llOrderItem);

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

        holder.rbRating.setRating(item.getIntRating());
        holder.llOrderItem.setBackground(item.isSelected());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
