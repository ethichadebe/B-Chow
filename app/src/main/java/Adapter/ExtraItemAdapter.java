package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.ExtraItem;
import www.ethichadebe.com.loxion_beanery.R;

public class ExtraItemAdapter extends RecyclerView.Adapter<ExtraItemAdapter.ExtraViewHolder> {

    private ArrayList<ExtraItem> ExtraList;

    private ExtraItemAdapter.OnExtraClickListener mListerner;

    public interface OnExtraClickListener {
        void onRemoveClick(int position);
        void onEditClick(int position);
    }

    public void setOnExtraClickListener(ExtraItemAdapter.OnExtraClickListener listener) {
        mListerner = listener;
    }

    static class ExtraViewHolder extends RecyclerView.ViewHolder{

        private TextView tvIngredients;
        private ImageView ivDelete, ivEdit;

        ExtraViewHolder(@NonNull View itemView, final ExtraItemAdapter.OnExtraClickListener listener) {
            super(itemView);
            tvIngredients = itemView.findViewById(R.id.tvIngredients);
            ivDelete = itemView.findViewById(R.id.ivDelete);
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

    public ExtraItemAdapter(ArrayList<ExtraItem> extraList){
        this.ExtraList = extraList;
    }

    @NonNull
    @Override
    public ExtraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new ExtraViewHolder(v, mListerner);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraViewHolder holder, int position) {
        ExtraItem item = ExtraList.get(position);

        holder.tvIngredients.setText(item.getStrIngredientName());
        holder.tvIngredients.setId(item.getIntID());
    }

    @Override
    public int getItemCount() {
        return ExtraList.size();
    }
}
