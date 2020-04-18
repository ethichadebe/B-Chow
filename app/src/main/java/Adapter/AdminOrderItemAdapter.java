package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.AdminOrderItem;
import www.ethichadebe.com.loxion_beanery.R;

public class AdminOrderItemAdapter extends RecyclerView.Adapter<AdminOrderItemAdapter.AdminOrderViewHolder> {

    private ArrayList<AdminOrderItem> adminOrders;

    private AdminOrderItemAdapter.OnItemClickListener mListerner;

    public interface OnItemClickListener {
        void onCancelClick(int position);
        void onDoneClick(int position);
    }

    public void setOnItemClickListener(AdminOrderItemAdapter.OnItemClickListener listener) {
        mListerner = listener;
    }

    static class AdminOrderViewHolder extends RecyclerView.ViewHolder{

        private TextView tvMenu, tvTime, tvOrderNum, tvPrice;
        private CardView cvDone, cvCancel,cvReady;
        private RelativeLayout rlOptions;

        AdminOrderViewHolder(@NonNull View itemView, final AdminOrderItemAdapter.OnItemClickListener listener) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrderNum = itemView.findViewById(R.id.tvOrderNum);

            cvDone = itemView.findViewById(R.id.cvDone);
            cvCancel = itemView.findViewById(R.id.cvCancel);
            cvReady = itemView.findViewById(R.id.cvReady);

            rlOptions = itemView.findViewById(R.id.rlOptions);

            cvDone.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDoneClick(position);
                    }
                }
            });

            cvCancel.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCancelClick(position);
                    }
                }
            });
            cvReady.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCancelClick(position);
                    }
                }
            });
        }
    }

    public AdminOrderItemAdapter(ArrayList<AdminOrderItem> shopList){
        this.adminOrders = shopList;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_order_item, parent, false);
        AdminOrderViewHolder svh = new AdminOrderViewHolder(v, mListerner);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        AdminOrderItem item = adminOrders.get(position);

        holder.tvMenu.setText(item.getStrMenu());
        holder.tvOrderNum.setText("#"+item.getIntOderNum());
        holder.tvPrice.setText(String.valueOf(item.getDblPrice()));
        holder.tvTime.setText(item.getStrTrime());

        if(item.getStrStatus().equals("Waiting for collection")){
            holder.rlOptions.setVisibility(View.GONE);
            holder.cvReady.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return adminOrders.size();
    }
}
