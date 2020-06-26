package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.IngredientItem;
import www.ethichadebe.com.loxion_beanery.R;

public class IngreRateItemAdapter extends RecyclerView.Adapter<IngreRateItemAdapter.IngredientViewHolder> {

    private ArrayList<IngredientItem> ingredientList;

    private IngreRateItemAdapter.OnIngredientClickListener mListerner;

    public interface OnIngredientClickListener {
        void OnRatingBar(int position);
    }

    public void setOnIngredientClickListener(IngreRateItemAdapter.OnIngredientClickListener listener) {
        mListerner = listener;
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder{
        private TextView tvIngredient;
        private RatingBar rbRating;

        IngredientViewHolder(@NonNull View itemView, final IngreRateItemAdapter.OnIngredientClickListener listener) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tvIngredient);
            rbRating = itemView.findViewById(R.id.rbRating);

            rbRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.OnRatingBar(position);
                    }
                }
            });
        }
    }

    public IngreRateItemAdapter(ArrayList<IngredientItem> shopList){
        this.ingredientList = shopList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingre_rate_item, parent, false);

        return new IngredientViewHolder(v, mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientItem item = ingredientList.get(position);


        holder.tvIngredient.setText(item.getStrIngredientName());
        holder.rbRating.setOnRatingBarChangeListener((ratingBar, v, b) -> {
            item.setDblPrice((double) ratingBar.getRating());
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
