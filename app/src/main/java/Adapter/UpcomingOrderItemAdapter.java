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
import SingleItem.UpcomingOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

public class UpcomingOrderItemAdapter extends RecyclerView.Adapter<UpcomingOrderItemAdapter.OrderViewHolder> {

    private ArrayList<UpcomingOrderItem> orderList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClickTrack(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        private TextView tvShopName, tvPrice, tvMenu, tvOrderNum, tvTime;
        private CardView cvTrack;

        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);
            tvTime = itemView.findViewById(R.id.tvTime);
            cvTrack = itemView.findViewById(R.id.cvTrack);
            cvTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position =getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.OnItemClickTrack(position);
                        }
                    }
                }
            });
        }
    }

    public UpcomingOrderItemAdapter(ArrayList<UpcomingOrderItem> shopList){
        this.orderList = shopList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_upcoming_order_item, parent, false);
        OrderViewHolder svh = new OrderViewHolder(v, mListener);

        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        UpcomingOrderItem item = orderList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.tvPrice.setText("R" + item.getDblPrice());
        holder.tvMenu.setText(item.getStrMenu());
        holder.tvOrderNum.setText("Order number: " + item.getIntOrderNum());
        holder.tvTime.setText(item.getStrTime());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
