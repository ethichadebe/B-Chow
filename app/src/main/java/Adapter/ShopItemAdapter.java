package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.ShopItem;
import www.ethichadebe.com.loxion_beanery.R;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopViewHolder> {

    private ArrayList<ShopItem> shopList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        private TextView tvShopName;
        private ImageView ivLogo;
        private TextView tvShortDescript;
        private ImageView ivStar1;
        private ImageView ivStar2;
        private ImageView ivStar3;
        private ImageView ivStar4;
        private ImageView ivStar5;
        private TextView tvDistance;
        private TextView tvAveTime;

        public ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvShortDescript = itemView.findViewById(R.id.tvShortDescript);
            ivStar1 = itemView.findViewById(R.id.ivStar1);
            ivStar2 = itemView.findViewById(R.id.ivStar2);
            ivStar3 = itemView.findViewById(R.id.ivStar3);
            ivStar4 = itemView.findViewById(R.id.ivStar4);
            ivStar5 = itemView.findViewById(R.id.ivStar5);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvAveTime = itemView.findViewById(R.id.tvAveTime);

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

    public ShopItemAdapter(ArrayList<ShopItem> shopList){
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        ShopViewHolder svh = new ShopViewHolder(v, mListener);

        return  svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = shopList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.ivLogo.setImageResource(item.getIntLogo());
        holder.tvShortDescript.setText(item.getStrShortDescript());
        holder.ivStar1.setImageResource(item.getInt1Star());
        holder.ivStar2.setImageResource(item.getInt2Star());
        holder.ivStar3.setImageResource(item.getInt3Star());
        holder.ivStar4.setImageResource(item.getInt4Star());
        holder.ivStar5.setImageResource(item.getInt5Star());
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
