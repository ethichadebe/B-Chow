package www.ethichadebe.com.loxion_beanery;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
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
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 1234;
    private static boolean mLocationGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    static boolean ismLocationGranted() {
        return mLocationGranted;
    }

    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmpty, tvSearch;
    private MaterialEditText etSearch;
    private CardView cvRetry;
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
        etSearch = v.findViewById(R.id.etSearch);
        cvRetry = v.findViewById(R.id.cvRetry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Search for nearby shops
        getDeviceLocation(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
        //ShopItem on click
        mAdapter.setOnItemClickListener(position -> {
            shopItem = shopItems.get(position);
            startActivity(new Intent(getActivity(), ShopHomeActivity.class));
        });

        //Get user location
        if (isServicesOk()) {
            getLocationPermissions();
        }

        Places.initialize(Objects.requireNonNull(getActivity()), getResources().getString(R.string.google_maps_api_key));

        //Retry button when network error occurs
        cvRetry.setOnClickListener(view -> getDeviceLocation(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey)));

        //Search Button on click
        tvSearch.setOnClickListener(view -> {
            if (etSearch.getVisibility() == View.GONE) {
                tvSearch.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
                etSearch.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight)
                        .duration(1000)
                        .repeat(0)
                        .playOn(etSearch);
            } else {
                tvSearch.setBackground(getResources().getDrawable(R.drawable.ic_search_black_24dp));
                etSearch.setVisibility(View.GONE);
            }
        });

        return v;
    }

    private void GETShops(View vLine, View vLineGrey, Double latitude, Double longitude) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/" + getUser().getuID()+"/"+latitude+"/"+longitude, null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
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
                                        ""/*R.drawable.food*/, ""/*R.drawable.biglogo*/,
                                        Shops.getString("sShortDescrption"),
                                        Shops.getString("sFullDescription"),
                                        new LatLng(Shops.getDouble("sLatitude"),
                                                Shops.getDouble("sLongitude")),
                                        Shops.getString("sAddress"),
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationGranted = false;
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: Permissions failed");
                        return;
                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: Permissions granted");
                mLocationGranted = true;
            }
        }
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

    private void getLocationPermissions() {
        Log.d(TAG, "getLocationPermissions: Getting location permissions");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermissions: Location granted");
                mLocationGranted = true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_REQUEST_CODE);
        }

    }

    private void getDeviceLocation(View vLine, View vLineGrey) {
        Log.d(TAG, "getDeviceLocation: getting current location");
        mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        try {
            if (ismLocationGranted()) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: location found");
                        Location currentLocation = (Location) task.getResult();

                        GETShops(vLine,vLineGrey, Objects.requireNonNull(currentLocation).getLatitude(),
                                currentLocation.getLongitude());

                    } else {
                        Log.d(TAG, "onComplete: Unable to get location");
                        Toast.makeText(getActivity(), "Unable to get current location",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security exception " + e.getMessage());
        }
    }

}
