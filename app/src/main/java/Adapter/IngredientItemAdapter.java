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
import www.ethichadebe.com.loxion_beanery.R;

public class IngredientItemAdapter extends RecyclerView.Adapter<IngredientItemAdapter.IngredientViewHolder> {

    private ArrayList<IngredientItem> ingredientList;

    private IngredientItemAdapter.OnIngredientClickListener mListerner;

    public interface OnIngredientClickListener {
        void onRemoveClick(int position);
        void onEditClick(int position);
    }

    public void setOnIngredientClickListener(IngredientItemAdapter.OnIngredientClickListener listener) {
        mListerner = listener;
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredients, tvPrice;
        private ImageView ivEdit, ivDelete;

        IngredientViewHolder(@NonNull View itemView, final IngredientItemAdapter.OnIngredientClickListener listener) {
            super(itemView);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivEdit = itemView.findViewById(R.id.ivEdit);

            ivDelete.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(position);
                    }
                }
            });

            ivEdit.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditClick(position);
                    }
                }
            });
        }
    }

    public IngredientItemAdapter(ArrayList<IngredientItem> shopList){
        this.ingredientList = shopList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new IngredientViewHolder(v, mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientItem item = ingredientList.get(position);

        holder.tvIngredients.setText(item.getStrIngredientName());
        holder.tvIngredients.setId(item.getIntID());
        holder.tvPrice.setText("R"+item.getDblPrice());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
