package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.IngredientItem;
import www.kicknbhoboza.com.emakoteni.R;

public class IngredientItemAdapter extends RecyclerView.Adapter<IngredientItemAdapter.IngredientViewHolder> {

    private ArrayList<IngredientItem> ingredientList;

    public static class IngredientViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
        }
    }

    public IngredientItemAdapter(ArrayList<IngredientItem> shopList){
        this.ingredientList = shopList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        IngredientViewHolder svh = new IngredientViewHolder(v);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientItem item = ingredientList.get(position);

        holder.tvIngredientName.setText(item.getStrIngredientName());
        holder.tvIngredientName.setId(item.getStrID());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
