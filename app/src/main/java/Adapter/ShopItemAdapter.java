package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;

import SingleItem.ShopItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.HelperMethods.roundOf;
import static util.HelperMethods.setOHVISIBILITY;
import static util.HelperMethods.setStarRating;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ShopViewHolder> {

    private ArrayList<ShopItem> ShopList;
    private OnItemClickListener mListener;
    private Context context;
    private String nativeAd_key;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView tvShopName, tvShortDescript, tvDistance, tvAveTime, tvMore, tvClosed;
        private TextView[] tvDays = new TextView[8];
        private ImageView ivLogo, ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private LinearLayout llOpHours, llDropDown;
        private RelativeLayout rlStatus;
        private UnifiedNativeAdView unifiedNativeAdView;

        ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener, Context context, String nativeAd_key) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            llOpHours = itemView.findViewById(R.id.llOpHours);
            llDropDown = itemView.findViewById(R.id.llDropDown);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            rlStatus = itemView.findViewById(R.id.rlStatus);
            tvClosed = itemView.findViewById(R.id.tvClosed);
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

            unifiedNativeAdView = itemView.findViewById(R.id.unavAd);

            AdLoader adLoader = new AdLoader.Builder(context, nativeAd_key)
                    .forUnifiedNativeAd(unifiedNativeAd -> {
                        // Show the ad.
                        MapUnifiedNativeAdToView(itemView, unifiedNativeAd, unifiedNativeAdView);
                    }).build();

            adLoader.loadAd(new AdRequest.Builder().build());


            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        private void MapUnifiedNativeAdToView(@NonNull View itemView, UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView unifiedNativeAdView) {
            unifiedNativeAdView.setMediaView(itemView.findViewById(R.id.mediaView));
            unifiedNativeAdView.setHeadlineView(itemView.findViewById(R.id.tvHeadName));
            unifiedNativeAdView.setBodyView(itemView.findViewById(R.id.tvBody));
            unifiedNativeAdView.setCallToActionView(itemView.findViewById(R.id.tvLearnMore));

            ((TextView) unifiedNativeAdView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());

            if (unifiedNativeAd.getBody() != null) {
                ((TextView) unifiedNativeAdView.getBodyView()).setText(unifiedNativeAd.getBody());
            } else {
                unifiedNativeAdView.getBodyView().setVisibility(View.GONE);
            }

            if (unifiedNativeAd.getMediaContent() != null) {
                unifiedNativeAdView.getMediaView().setMediaContent(unifiedNativeAd.getMediaContent());
            } else {
                unifiedNativeAdView.getMediaView().setVisibility(View.GONE);
            }

            if (unifiedNativeAd.getCallToAction() != null) {
                ((TextView) unifiedNativeAdView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            } else {
                unifiedNativeAdView.getCallToActionView().setVisibility(View.GONE);
            }

            unifiedNativeAdView.setNativeAd(unifiedNativeAd);
        }
    }

    public ShopItemAdapter(ArrayList<ShopItem> shopList, Context context, String nativeAd_key) {
        this.ShopList = shopList;
        this.context = context;
        this.nativeAd_key = nativeAd_key;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);

        return new ShopViewHolder(v, mListener, context, nativeAd_key);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem item = ShopList.get(position);

        holder.tvShopName.setText(item.getStrShopName());
        //holder.ivLogo.setImageResource(item.getIntLogoSmall());
        holder.tvShortDescript.setText(item.getStrShortDescript());
        //Calculate distance
        holder.tvDistance.setText(roundOf(item.getDblDistance(), 1) + "Km");
        holder.tvAveTime.setText(item.getStrAveTime());

        if (!item.isShowAd()){
            holder.unifiedNativeAdView.setVisibility(View.GONE);
        }

        if (item.getIntStatus() == 0) {
            holder.tvClosed.setVisibility(View.VISIBLE);
        } else {
            holder.tvClosed.setVisibility(View.GONE);
        }

        setStarRating(item.getIntRating(), holder.ivStar1, holder.ivStar2, holder.ivStar3, holder.ivStar4, holder.ivStar5);

        holder.llDropDown.setOnClickListener(view -> {
            setOHVISIBILITY(holder.llOpHours, holder.tvMore, holder.tvDays, item.getStrOperatingHRS());
        });

        holder.tvAveTime.setBackgroundColor(item.getIntAveTimeColor());
        holder.rlStatus.setBackground(item.getDraStatus());
    }

    @Override
    public int getItemCount() {
        return ShopList.size();
    }
}


