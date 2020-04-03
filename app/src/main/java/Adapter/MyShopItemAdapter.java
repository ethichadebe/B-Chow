package Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

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

    static class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvPosition, tvShortDescript, tvDistance, tvAveTime, tvMon,tvTue,tvWed,tvThu,tvFri,tvSat,tvSun,tvPH;
        private ImageView ivLogo, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private LinearLayout llOpHours,llDropDown;
        private View vUp, vDown;

        ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            llOpHours = itemView.findViewById(R.id.llOpHours);
            llDropDown = itemView.findViewById(R.id.llDropDown);
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
            vUp = itemView.findViewById(R.id.vUp);
            vDown = itemView.findViewById(R.id.vDown);

            tvMon = itemView.findViewById(R.id.tvMon);
            tvTue = itemView.findViewById(R.id.tvTue);
            tvWed = itemView.findViewById(R.id.tvWed);
            tvThu = itemView.findViewById(R.id.tvThu);
            tvFri = itemView.findViewById(R.id.tvFri);
            tvSat = itemView.findViewById(R.id.tvSat);
            tvSun = itemView.findViewById(R.id.tvSun);
            tvPH = itemView.findViewById(R.id.tvPH);

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

    public MyShopItemAdapter(ArrayList<MyShopItem> shopList) {
        this.myShopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_shop_item, parent, false);

        return new ShopViewHolder(v, mListener);
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

        String[] strOpHours = item.getStrOperatingHRS().split(", ");

        holder.tvMon.setText(strOpHours[0]);
        holder.tvTue.setText(strOpHours[1]);
        holder.tvWed.setText(strOpHours[2]);
        holder.tvThu.setText(strOpHours[3]);
        holder.tvFri.setText(strOpHours[4]);
        holder.tvSat.setText(strOpHours[5]);
        holder.tvSun.setText(strOpHours[6]);
        holder.tvPH.setText(strOpHours[7]);

        holder.llDropDown.setOnClickListener(view -> {
            if (holder.llOpHours.getVisibility() == View.GONE){
                holder.llOpHours.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInDown)
                        .duration(500)
                        .repeat(0)
                        .playOn(holder.llOpHours);
                holder.vDown.setVisibility(View.GONE);
                holder.vUp.setVisibility(View.VISIBLE);
            }else {
                YoYo.with(Techniques.SlideOutUp)
                        .duration(500)
                        .repeat(0)
                        .playOn(holder.llOpHours);
                holder.llOpHours.setVisibility(View.GONE);
                holder.vDown.setVisibility(View.VISIBLE);
                holder.vUp.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myShopList.size();
    }
}


