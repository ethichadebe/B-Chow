package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import SingleItem.ShopItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.HelperMethods.setOHVISIBILITY;
import static util.HelperMethods.setStarRating;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopViewHolder> {

    private ArrayList<ShopItem> ShopList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvShortDescript, tvDistance, tvAveTime,tvMore;
        TextView[] tvDays = new TextView[8];
        private ImageView ivLogo, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private LinearLayout llOpHours,llDropDown;

        ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            llOpHours = itemView.findViewById(R.id.llOpHours);
            llDropDown = itemView.findViewById(R.id.llDropDown);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvShortDescript = itemView.findViewById(R.id.tvShortDescript);
            ivStar1 = itemView.findViewById(R.id.ivStar1);
            ivStar2 = itemView.findViewById(R.id.ivStar2);
            ivStar3 = itemView.findViewById(R.id.ivStar3);
            ivStar4 = itemView.findViewById(R.id.ivStar4);
            ivStar5 = itemView.findViewById(R.id.ivStar5);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvAveTime = itemView.findViewById(R.id.tvAveTime);
            tvMore = itemView.findViewById(R.id.tvMore);

            tvDays[0] = itemView.findViewById(R.id.tvMon);
            tvDays[1] = itemView.findViewById(R.id.tvTue);
            tvDays[2] = itemView.findViewById(R.id.tvWed);
            tvDays[3] = itemView.findViewById(R.id.tvThu);
            tvDays[4] = itemView.findViewById(R.id.tvFri);
            tvDays[5] = itemView.findViewById(R.id.tvSat);
            tvDays[6] = itemView.findViewById(R.id.tvSun);
            tvDays[7] = itemView.findViewById(R.id.tvPH);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public ShopItemAdapter(ArrayList<ShopItem> shopList) {
        this.ShopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);

        return new ShopViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = ShopList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.ivLogo.setImageResource(item.getIntLogoSmall());
        holder.tvShortDescript.setText(item.getStrShortDescript());
        //Calculate distance
        holder.tvDistance.setText("2Km");
        holder.tvAveTime.setText(item.getStrAveTime());

        setStarRating(item.getIntRating(), holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5);

        holder.llDropDown.setOnClickListener(view -> {
            setOHVISIBILITY(holder.llOpHours, holder.tvMore,holder.tvDays, item.getStrOperatingHRS());
        });

        holder.tvAveTime.setBackgroundColor(item.getIntAveTimeColor());
    }

    @Override
    public int getItemCount() {
        return ShopList.size();
    }
}


