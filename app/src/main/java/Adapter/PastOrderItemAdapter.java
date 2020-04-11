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

public class PastOrderItemAdapter extends RecyclerView.Adapter<PastOrderItemAdapter.OrderViewHolder> {

    private ArrayList<PastOrderItem> orderList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
        void OnItemClickRate(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView tvShopName, tvPrice, tvMenu, tvOrderNum, tvTime;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private CardView cvRate, cvReorder;
        private LinearLayout llStars;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
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

            cvReorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(position);
                        }
                    }
                }
            });

            cvRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClickRate(position);
                        }
                    }
                }
            });
        }
    }

    public PastOrderItemAdapter(ArrayList<PastOrderItem> shopList){
        this.orderList = shopList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_past_order_item, parent, false);
        OrderViewHolder svh = new OrderViewHolder(v, mListener);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        PastOrderItem item = orderList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.tvPrice.setText("R" + item.getDblPrice());
        holder.tvMenu.setText(item.getStrMenu());
        holder.tvOrderNum.setText("Order number: " + item.getIntOrderNum());
        holder.tvTime.setText(item.getStrTime());
        if (item.getIntRating() == 0){
            holder.cvRate.setVisibility(View.VISIBLE);
            holder.llStars.setVisibility(View.GONE);
        }else {
            holder.cvRate.setVisibility(View.GONE);
            holder.llStars.setVisibility(View.VISIBLE);

            switch (item.getIntRating()){
                case 1:
                    holder.ivStar1.setImageResource(0);
                    holder.ivStar2.setImageResource(0);
                    holder.ivStar3.setImageResource(0);
                    holder.ivStar4.setImageResource(0);
                    holder.ivStar5.setVisibility(View.VISIBLE);
                case 2:
                    holder.ivStar1.setImageResource(0);
                    holder.ivStar2.setImageResource(0);
                    holder.ivStar3.setImageResource(0);
                    holder.ivStar4.setVisibility(View.VISIBLE);
                    holder.ivStar5.setVisibility(View.VISIBLE);
                case 3:
                    holder.ivStar1.setImageResource(0);
                    holder.ivStar2.setImageResource(0);
                    holder.ivStar3.setVisibility(View.VISIBLE);
                    holder.ivStar4.setVisibility(View.VISIBLE);
                    holder.ivStar5.setVisibility(View.VISIBLE);
                case 4:
                    holder.ivStar1.setImageResource(0);
                    holder.ivStar2.setVisibility(View.VISIBLE);
                    holder.ivStar3.setVisibility(View.VISIBLE);
                    holder.ivStar4.setVisibility(View.VISIBLE);
                    holder.ivStar5.setVisibility(View.VISIBLE);
                case 5:
                    holder.ivStar1.setVisibility(View.VISIBLE);
                    holder.ivStar2.setVisibility(View.VISIBLE);
                    holder.ivStar3.setVisibility(View.VISIBLE);
                    holder.ivStar4.setVisibility(View.VISIBLE);
                    holder.ivStar5.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
