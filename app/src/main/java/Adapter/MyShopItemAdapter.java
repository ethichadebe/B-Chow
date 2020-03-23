package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.MyShopItem;
import www.ethichadebe.com.loxion_beanery.R;

public class MyShopItemAdapter extends RecyclerView.Adapter<MyShopItemAdapter.ShopViewHolder> {

    private ArrayList<MyShopItem> myShopList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListerner(OnItemClickListener listerner) {
        mListener = listerner;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName;
        private TextView tvPosition;
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
            tvPosition = itemView.findViewById(R.id.tvPosition);
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
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public MyShopItemAdapter(ArrayList<MyShopItem> shopList) {
        this.myShopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_item, parent, false);
        ShopViewHolder svh = new ShopViewHolder(v, mListener);

        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        MyShopItem item = myShopList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.tvPosition.setText(item.getStrPosition());
        holder.ivLogo.setImageResource(item.getIntLogoSmall());
        holder.tvShortDescript.setText(item.getStrShortDescript());
        //Calculate distance
        holder.tvDistance.setText("2Km");
        holder.tvAveTime.setText(item.getStrAveTime());
        switch (item.getIntRating()) {
            case 0:
                holder.ivStar1.setImageResource(0);
                holder.ivStar2.setImageResource(0);
                holder.ivStar3.setImageResource(0);
                holder.ivStar4.setImageResource(0);
                holder.ivStar5.setImageResource(0);
                break;
            case 1:
                holder.ivStar1.setImageResource(0);
                holder.ivStar2.setImageResource(0);
                holder.ivStar3.setImageResource(0);
                holder.ivStar4.setImageResource(0);
                holder.ivStar5.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.ivStar1.setImageResource(0);
                holder.ivStar2.setImageResource(0);
                holder.ivStar3.setImageResource(0);
                holder.ivStar4.setVisibility(View.VISIBLE);
                holder.ivStar5.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.ivStar1.setImageResource(0);
                holder.ivStar2.setImageResource(0);
                holder.ivStar3.setVisibility(View.VISIBLE);
                holder.ivStar4.setVisibility(View.VISIBLE);
                holder.ivStar5.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.ivStar1.setImageResource(0);
                holder.ivStar2.setVisibility(View.VISIBLE);
                holder.ivStar3.setVisibility(View.VISIBLE);
                holder.ivStar4.setVisibility(View.VISIBLE);
                holder.ivStar5.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.ivStar1.setVisibility(View.VISIBLE);
                holder.ivStar2.setVisibility(View.VISIBLE);
                holder.ivStar3.setVisibility(View.VISIBLE);
                holder.ivStar4.setVisibility(View.VISIBLE);
                holder.ivStar5.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return myShopList.size();
    }
}


