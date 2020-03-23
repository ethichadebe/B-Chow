package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.MenuItem;
import www.ethichadebe.com.loxion_beanery.R;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder> {

    private ArrayList<MenuItem> menuList;

    private OnItemClickListener mListerner;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListerner = listener;
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPrice, tvIngredients;
        private ImageView ivEdit, ivDelete;

        public MenuViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public MenuItemAdapter(ArrayList<MenuItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        MenuViewHolder svh = new MenuViewHolder(v, mListerner);

        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        holder.tvPrice.setText("R"+String.valueOf(item.getDblPrice()));
        holder.tvIngredients.setText(item.getStrMenu());
        holder.ivEdit.setImageResource(item.getIntEdit());
        holder.ivDelete.setImageResource(item.getIntDelete());
        holder.ivEdit.setVisibility(item.getIntVisibility());
        holder.ivDelete.setVisibility(item.getIntVisibility());


    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
