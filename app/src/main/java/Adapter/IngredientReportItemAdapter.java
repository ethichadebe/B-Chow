package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.CancelledOrderItem;
import SingleItem.IngredientReportItem;
import www.ethichadebe.com.loxion_beanery.R;

public class IngredientReportItemAdapter extends RecyclerView.Adapter<IngredientReportItemAdapter.MenuViewHolder> {

    private ArrayList<IngredientReportItem> menuList;

    static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCount, tvName;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    public IngredientReportItemAdapter(ArrayList<IngredientReportItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_report_item, parent, false);

        return new MenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        IngredientReportItem item = menuList.get(position);

        holder.tvName.setText(item.getStrName());
        holder.tvCount.setText(String.valueOf(item.getIntCount()));
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
