package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import www.kicknbhoboza.com.emakoteni.R;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder> {

    private ArrayList<MenuItem> menuList;

    public static class MenuViewHolder extends RecyclerView.ViewHolder{

        private TextView tvPrice;
        private TextView tvIngredients;
        private ImageView ivEdit;
        private ImageView tvDelete;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
        }
    }

    public MenuItemAdapter(ArrayList<MenuItem> menuList){
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        MenuViewHolder svh = new MenuViewHolder(v);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        holder.tvPrice.setText(item.getStrPrice());
        holder.tvIngredients.setText(item.getStrMenu());
        holder.ivEdit.setImageResource(item.getIntEdit());
        holder.tvDelete.setImageResource(item.getIntDelete());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
