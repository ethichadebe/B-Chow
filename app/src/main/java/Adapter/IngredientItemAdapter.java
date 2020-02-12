package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    public void setOnIngredientClickListener(IngredientItemAdapter.OnIngredientClickListener listener) {
        mListerner = listener;
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredientName, tvRemove, tvPrice;

        public IngredientViewHolder(@NonNull View itemView, final IngredientItemAdapter.OnIngredientClickListener listener) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvRemove = itemView.findViewById(R.id.tvRemove);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRemoveClick(position);
                        }
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        IngredientViewHolder svh = new IngredientViewHolder(v, mListerner);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientItem item = ingredientList.get(position);

        holder.tvIngredientName.setText(item.getStrIngredientName());
        holder.tvIngredientName.setId(item.getIntID());
        holder.tvPrice.setText("R"+item.getDblPrice());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
