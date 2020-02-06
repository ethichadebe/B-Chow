package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.IngredientItem;
import SingleItem.IngredientItemCheckbox;
import www.kicknbhoboza.com.emakoteni.R;

public class IngredientItemCheckboxAdapter extends RecyclerView.Adapter<IngredientItemCheckboxAdapter.IngredientCheckboxViewHolder> {

    private ArrayList<IngredientItemCheckbox> ingredientList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class IngredientCheckboxViewHolder extends RecyclerView.ViewHolder{

        private CheckBox tvIngredientName;

        public IngredientCheckboxViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);

            itemView.setOnClickListener(new View.OnClickListener() {
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

        }
    }

    public IngredientItemCheckboxAdapter(ArrayList<IngredientItemCheckbox> shopList){
        this.ingredientList = shopList;
    }

    @NonNull
    @Override
    public IngredientCheckboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_checkbox, parent, false);
        IngredientCheckboxViewHolder svh = new IngredientCheckboxViewHolder(v, mListener);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientCheckboxViewHolder holder, int position) {
        IngredientItemCheckbox item = ingredientList.get(position);

        holder.tvIngredientName.setText(item.getStrIngredientName());
        holder.tvIngredientName.setId(item.getStrID());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
