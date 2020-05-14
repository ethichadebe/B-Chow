package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import Adapter.ShopItemAdapter;
import SingleItem.ShopItem;

import static android.app.Activity.RESULT_OK;
import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmpty, tvSearch,tvLocationName;
    private MaterialEditText etSearch;
    private CardView cvRetry, cvLocation;
    private static ArrayList<ShopItem> shopItems;
    private RelativeLayout rlLoad, rlError;
    private static ShopItem shopItem;

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String TAG = "HomeFragment";

    static ShopItem getShopItem() {
        return shopItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);
        if (getUser() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }//heck if user is logged in
        shopItems = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.recyclerView);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvSearch = v.findViewById(R.id.tvSearch);
        tvLocationName = v.findViewById(R.id.tvLocationName);
        etSearch = v.findViewById(R.id.etSearch);
        cvRetry = v.findViewById(R.id.cvRetry);
        cvLocation = v.findViewById(R.id.cvLocation);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Search for nearby shops
        GETShops(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));

        //ShopItem on click
        mAdapter.setOnItemClickListener(position -> {
            shopItem = shopItems.get(position);
            startActivity(new Intent(getActivity(), ShopHomeActivity.class));
        });

        Places.initialize(Objects.requireNonNull(getActivity()), getResources().getString(R.string.google_maps_api_key));


        cvLocation.setOnClickListener(view -> {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                        fieldList).build(getActivity());
                startActivityForResult(intent, 100);
        });

        //Retry button when network error occurs
        cvRetry.setOnClickListener(view -> GETShops(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey)));

        //Search Button on click
        tvSearch.setOnClickListener(view -> {
            if (etSearch.getVisibility() == View.GONE) {
                tvSearch.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
                etSearch.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight)
                        .duration(1000)
                        .repeat(0)
                        .playOn(etSearch);
            }else {
                tvSearch.setBackground(getResources().getDrawable(R.drawable.ic_search_black_24dp));
                etSearch.setVisibility(View.GONE);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 100) &&(resultCode== RESULT_OK)){
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));

            tvLocationName.setText(place.getAddress());
        }else if(resultCode == AutocompleteActivity.RESULT_ERROR){
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void GETShops(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/" + getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    Location location = new Location("");
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("shops");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Shops = jsonArray.getJSONObject(i);
                                String Avetime = "";
                                int AveTimeColor = 0;

                                switch (Shops.getString("sAveTime")) {
                                    case "<10":
                                        Avetime = "less than 10 mins";
                                        AveTimeColor = getResources().getColor(R.color.Green);
                                        break;
                                    case "10-15":
                                        Avetime = "10 - 15 mins";
                                        AveTimeColor = getResources().getColor(R.color.Yellow);
                                        break;
                                    case ">20":
                                        Avetime = "Longer than 20 mins";
                                        AveTimeColor = getResources().getColor(R.color.Red);
                                        break;
                                }//Set ave time

                                Drawable bgStatus = null;
                                switch (Shops.getInt("sStatus")) {
                                    case 1:
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open);
                                        break;
                                    case 0:
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open_closed);
                                        break;
                                }
                                shopItems.add(new ShopItem(Shops.getInt("sID"), Shops.getString("sName"),
                                        ""/*R.drawable.food*/, ""/*R.drawable.biglogo*/, Shops.getString("sShortDescrption"),
                                        Shops.getString("sFullDescription"), Shops.getString("sLocation"),
                                        Avetime, Shops.getInt("sRating"),
                                        Shops.getString("sOperatingHrs"), Shops.getInt("sLikes"),
                                        Shops.getInt("isLiked"), AveTimeColor, Shops.getInt("sStatus")
                                        , bgStatus));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        requestQueue.add(objectRequest);

    }

    private boolean isServicesOk() {
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Objects.requireNonNull(getActivity()));
        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOk: google services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOk: error occurred but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "You cant make map requests", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}
