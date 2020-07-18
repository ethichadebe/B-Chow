package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.ShopItemAdapter;
import SingleItem.ShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static util.HelperMethods.randomNumber;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class SearchFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmpty;
    private MaterialSearchBar searchBar;
    private CardView cvSearch;
    private static ArrayList<ShopItem> shopItems;
    private RelativeLayout rlLoad, rlError;
    private static ShopItem shopItem;

    private static final String TAG = "HomeFragment";
    private RequestQueue requestQueue;

    static ShopItem getShopItem() {
        return shopItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_search, container, false);
        shopItems = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.recyclerView);
        rlLoad = v.findViewById(R.id.rlLoad);
        cvSearch = v.findViewById(R.id.cvSearch);
        rlError = v.findViewById(R.id.rlError);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        searchBar = v.findViewById(R.id.searchBar);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems, getActivity(), getString(R.string.AdMob_Native_ID));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Ads
        MobileAds.initialize(getActivity());

        //ShopItem on click
        mAdapter.setOnItemClickListener(position -> {
            shopItem = shopItems.get(position);
            startActivity(new Intent(getActivity(), ShopHomeActivity.class));
        });

        //Search for nearby shops
        Places.initialize(Objects.requireNonNull(getActivity()), getResources().getString(R.string.google_maps_api_key));

        //Search Button on click
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBar.getText().length() > 3) {
                    GETShops(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Initialize maps places
        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_api_key));

        return v;
    }

    private void GETShops(View vLine, View vLineGrey) {
        //requestQueue.cancelAll(TAG);
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/searchShop/" + getUser().getuID() + "/" + getUser().getuLocation().latitude + "/" + getUser().getuLocation().longitude
                        + "/" + searchBar.getText().toLowerCase(), null,
                response -> {
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("shops");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Shops = jsonArray.getJSONObject(i);
                                shopItems.add(new ShopItem(getResources(), Shops.getInt("sID"), Shops.getString("sName"),
                                        Shops.getString("sSmallPicture"), Shops.getString("sBigPicture"),
                                        Shops.getString("sShortDescrption"), Shops.getString("sFullDescription"),
                                        new LatLng(Shops.getDouble("sLatitude"), Shops.getDouble("sLongitude")),
                                        Shops.getString("sAddress"), Shops.getDouble("distance"),
                                        Shops.getDouble("sAveTime"), Shops.getDouble("sRating"),
                                        Shops.getString("sOperatingHrs"), Shops.getInt("sLikes"),
                                        Shops.getInt("isLiked"), Shops.getInt("sStatus"),
                                        randomNumber(10)));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "GETShops: " + e.toString());
                    }
                },
                error -> {
                    rlError.setVisibility(View.VISIBLE);
                    rlLoad.setVisibility(View.GONE);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

}