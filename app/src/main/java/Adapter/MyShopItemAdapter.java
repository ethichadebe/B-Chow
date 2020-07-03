package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import SingleItem.MyShopItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.Constants.getIpAddress;
import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.setOHVISIBILITY;

public class MyShopItemAdapter extends RecyclerView.Adapter<MyShopItemAdapter.ShopViewHolder> {

    private ArrayList<MyShopItem> myShopList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemDelete(int position);

        void onItemClickResumeRegistration(int position);
    }

    public void setOnItemClickListerner(OnItemClickListener listerner) {
        mListener = listerner;
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvPosition, tvShortDescript, tvDistance, tvAveTime, tvMore, tvCompleteReg,
                tvnOrders;
        private TextView[] tvDays = new TextView[8];
        private ImageView ivLogo, ivDelete;
        private LinearLayout llOpHours, llDropDown;
        private RatingBar rbRating;

        ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            rbRating = itemView.findViewById(R.id.rbRating);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            llOpHours = itemView.findViewById(R.id.llOpHours);
            llDropDown = itemView.findViewById(R.id.llDropDown);
            tvnOrders = itemView.findViewById(R.id.tvnOrders);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            tvShortDescript = itemView.findViewById(R.id.tvShortDescript);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvAveTime = itemView.findViewById(R.id.tvAveTime);
            tvMore = itemView.findViewById(R.id.tvMore);
            tvCompleteReg = itemView.findViewById(R.id.tvCompleteReg);

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

            ivDelete.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemDelete(position);
                    }
                }
            });

            tvCompleteReg.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClickResumeRegistration(position);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_item, parent,
                false);

        return new ShopViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        MyShopItem item = myShopList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        holder.tvPosition.setText(item.getStrPosition());
        Picasso.get().load(getIpAddress() + "/" + item.getStrLogoSmall()).into(holder.ivLogo);
        holder.tvShortDescript.setText(item.getStrShortDescript());
        holder.tvnOrders.setText("" + item.getIntnOrders());
        //Calculate distance
        holder.tvDistance.setText(item.getStrAddress());
        holder.tvAveTime.setText(item.getStrAveTime());
        holder.rbRating.setRating(Float.parseFloat(String.valueOf(item.getIntRating())));

        if (item.isActive()) {
            holder.tvCompleteReg.setVisibility(View.VISIBLE);
            //Display delete button if shop registration isn't complete
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.tvnOrders.setVisibility(View.GONE);
        }

        holder.llDropDown.setOnClickListener(view -> setOHVISIBILITY(holder.llOpHours, holder.tvMore,
                holder.tvDays, item.getStrOperatingHRS()));

        DisplayImage(holder.ivLogo, item.getStrLogoSmall());


    }

    @Override
    public int getItemCount() {
        return myShopList.size();
    }
}


