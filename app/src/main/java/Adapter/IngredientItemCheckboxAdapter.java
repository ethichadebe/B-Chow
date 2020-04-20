package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.IngredientItemCheckbox;
import www.ethichadebe.com.loxion_beanery.R;

public class IngredientItemCheckboxAdapter extends RecyclerView.Adapter<IngredientItemCheckboxAdapter.IngredientCheckboxViewHolder> {

    private ArrayList<IngredientItemCheckbox> ingredientList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;

    }

    static class IngredientCheckboxViewHolder extends RecyclerView.ViewHolder {

        private CheckBox tvIngredientName;
        private TextView tvPrice;

        IngredientCheckboxViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            tvIngredientName.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(position);
                    }
                }
            });

        }
    }

    public IngredientItemCheckboxAdapter(ArrayList<IngredientItemCheckbox> shopList) {
        this.ingredientList = shopList;
    }

    @NonNull
    @Override
    public IngredientCheckboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_checkbox, parent, false);

        return new IngredientCheckboxViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientCheckboxViewHolder holder, int position) {
        final IngredientItemCheckbox item = ingredientList.get(position);

        holder.tvIngredientName.setText(item.getStrIngredientName());
        holder.tvIngredientName.setId(item.getIntID());
        holder.tvIngredientName.setChecked(item.getChecked());
        holder.tvPrice.setText("R" + item.getDblPrice()+"0");

        if (!item.getVisible()){
            holder.tvPrice.setVisibility(View.GONE);
        }

        if (holder.tvIngredientName.getText().toString().toLowerCase().equals("chips")) {
            holder.tvIngredientName.setChecked(true);
            holder.tvIngredientName.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
