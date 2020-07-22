package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import SingleItem.ShopItem;
import www.ethichadebe.com.loxion_beanery.R;

import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.roundOf;
import static util.HelperMethods.setOHVISIBILITY;

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

        private TextView tvShopName, tvShortDescript, tvDistance, tvAveTime, tvMore;
        private TextView[] tvDays = new TextView[8];
        private ImageView ivLogo;
        private LinearLayout llOpHours, llDropDown,llClosed;
        private CardView cdAd;
        private RatingBar rbRating;
        private UnifiedNativeAdView unifiedNativeAdView;
        private ExpandableLayout expandableLayout;

        ShopViewHolder(@NonNull View itemView, final OnItemClickListener listener, Context context, String nativeAd_key) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            rbRating = itemView.findViewById(R.id.rbRating);
            llOpHours = itemView.findViewById(R.id.llOpHours);
            llDropDown = itemView.findViewById(R.id.llDropDown);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            llClosed = itemView.findViewById(R.id.llClosed);
            tvShortDescript = itemView.findViewById(R.id.tvShortDescript);
            cdAd = itemView.findViewById(R.id.cdAd);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvAveTime = itemView.findViewById(R.id.tvAveTime);
            tvMore = itemView.findViewById(R.id.tvMore);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());

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
            holder.cdAd.setVisibility(View.GONE);
        }

        if (item.getIntStatus() == 0) {
            holder.llClosed.setVisibility(View.VISIBLE);
        } else {
            holder.llClosed.setVisibility(View.GONE);
        }


        holder.llDropDown.setOnClickListener(view -> {
            setOHVISIBILITY(holder.expandableLayout, holder.tvMore, holder.tvDays, item.getStrOperatingHRS());
        });

        holder.tvAveTime.setBackgroundColor(item.getIntAveTimeColor());
        holder.rbRating.setRating(Float.parseFloat(String.valueOf(item.getIntRating())));
        DisplayImage(holder.ivLogo, item.getStrLogoSmall());
    }


    @Override
    public int getItemCount() {
        return ShopList.size();
    }
}


