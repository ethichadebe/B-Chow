package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.CancelledOrderItem;
import SingleItem.MenuStatItem;
import www.ethichadebe.com.loxion_beanery.R;

public class CancelledOrderItemAdapter extends RecyclerView.Adapter<CancelledOrderItemAdapter.MenuViewHolder> {

    private ArrayList<CancelledOrderItem> menuList;

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPrice, tvMenu, tvReason, tvnItems;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvnItems = itemView.findViewById(R.id.tvnItems);
        }
    }

    public CancelledOrderItemAdapter(ArrayList<CancelledOrderItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancelled_order_item, parent, false);

        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        CancelledOrderItem item = menuList.get(position);

        holder.tvPrice.setText("R" + item.getDblPrice() + "0");
        holder.tvMenu.setText(item.getStrMenu());
        holder.tvReason.setText("Reason: " + item.getStrReason());
        holder.tvnItems.setText(String.valueOf(item.getIntnItems()));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
