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

import SingleItem.PastOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.HelperMethods.setStarRating;

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
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private CardView cvRate, cvReorder;
        private LinearLayout llStars;

        OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvExtras = itemView.findViewById(R.id.tvExtras);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            cvRate = itemView.findViewById(R.id.cvRate);
            cvReorder = itemView.findViewById(R.id.cvReorder);
            llStars = itemView.findViewById(R.id.llStars);

            ivStar1 = itemView.findViewById(R.id.ivStar1);
            ivStar2 = itemView.findViewById(R.id.ivStar2);
            ivStar3 = itemView.findViewById(R.id.ivStar3);
            ivStar4 = itemView.findViewById(R.id.ivStar4);
            ivStar5 = itemView.findViewById(R.id.ivStar5);

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
        holder.tvOrderNum.setText("Order number: " + item.getIntOrderNum());
        holder.tvTime.setText(item.getStrTime());
        if(item.getStrStatus().equals("Cancelled")){
            holder.cvRate.setVisibility(View.GONE);
            holder.llStars.setVisibility(View.GONE);
        } else if (item.getIntRating() == 0) {
            holder.cvRate.setVisibility(View.VISIBLE);
            holder.llStars.setVisibility(View.GONE);
        }else {
            holder.cvRate.setVisibility(View.GONE);
            holder.llStars.setVisibility(View.VISIBLE);

            setStarRating(item.getIntRating(), holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5);
        }


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
