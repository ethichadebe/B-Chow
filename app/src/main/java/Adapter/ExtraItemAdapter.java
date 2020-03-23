package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.ExtraItem;
import SingleItem.IngredientItem;
import www.ethichadebe.com.loxion_beanery.R;

public class ExtraItemAdapter extends RecyclerView.Adapter<ExtraItemAdapter.ExtraViewHolder> {

    private ArrayList<ExtraItem> ExtraList;

    private ExtraItemAdapter.OnExtraClickListener mListerner;

    public interface OnExtraClickListener {
        void onRemoveClick(int position);
    }

    public void setOnExtraClickListener(ExtraItemAdapter.OnExtraClickListener listener) {
        mListerner = listener;
    }

    public static class ExtraViewHolder extends RecyclerView.ViewHolder{

        private TextView tvExtraName, tvRemove;

        public ExtraViewHolder(@NonNull View itemView, final ExtraItemAdapter.OnExtraClickListener listener) {
            super(itemView);
            tvExtraName = itemView.findViewById(R.id.tvIngredientName);
            tvRemove = itemView.findViewById(R.id.tvRemove);

            tvRemove.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveClick(position);
                    }
                }
            });
        }
    }

    public ExtraItemAdapter(ArrayList<ExtraItem> extraList){
        this.ExtraList = extraList;
    }

    @NonNull
    @Override
    public ExtraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        ExtraViewHolder svh = new ExtraViewHolder(v, mListerner);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraViewHolder holder, int position) {
        ExtraItem item = ExtraList.get(position);

        holder.tvExtraName.setText(item.getStrIngredientName());
        holder.tvExtraName.setId(item.getIntID());
    }

    @Override
    public int getItemCount() {
        return ExtraList.size();
    }
}
