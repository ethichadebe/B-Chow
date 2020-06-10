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

import SingleItem.PastOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

public class PastOrderItemAdapter extends RecyclerView.Adapter<PastOrderItemAdapter.OrderViewHolder> {

    private ArrayList<PastOrderItem> orderList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemReorderClick(int position);
        void OnItemClickRate(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvPrice, tvMenu, tvExtras, tvOrderNum, tvTime, tvStatus;
        private CardView cvRate, cvReorder;
        private RatingBar rbRating;

        OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvExtras = itemView.findViewById(R.id.tvExtras);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            cvRate = itemView.findViewById(R.id.cvRate);
            cvReorder = itemView.findViewById(R.id.cvReorder);

            cvReorder.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnItemReorderClick(position);
                    }
                }
            });

            cvRate.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnItemClickRate(position);
                    }
                }
            });
        }
    }

    public PastOrderItemAdapter(ArrayList<PastOrderItem> shopList) {
        this.orderList = shopList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_past_order_item, parent, false);

        return new OrderViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PastOrderItem item = orderList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.tvPrice.setText("R" + item.getDblPrice() + "0");
        holder.tvMenu.setText(item.getStrMenu());
        holder.tvExtras.setText(item.getStrExtras());
        holder.tvStatus.setText(item.getStrStatus());
        holder.tvOrderNum.setText("Order number: " + item.getIntOderNum());
        holder.tvTime.setText(item.getStrTime());
        holder.tvStatus.setVisibility(View.GONE);
        if(item.getStrStatus().equals("Cancelled")){
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.cvRate.setVisibility(View.GONE);
            holder.rbRating.setVisibility(View.GONE);
        } else if (item.getIntRating() == 0) {
            holder.tvStatus.setVisibility(View.GONE);
            holder.cvRate.setVisibility(View.VISIBLE);
            holder.rbRating.setVisibility(View.GONE);
        }else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.cvRate.setVisibility(View.GONE);
            holder.rbRating.setVisibility(View.VISIBLE);
            holder.rbRating.setRating(item.getIntRating());
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
