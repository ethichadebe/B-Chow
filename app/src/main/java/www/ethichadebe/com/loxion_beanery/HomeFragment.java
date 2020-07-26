package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Adapter.ShopItemAdapter;
import SingleItem.ShopItem;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.getError;
import static util.HelperMethods.handler;
import static util.HelperMethods.ismLocationGranted;
import static util.HelperMethods.randomNumber;
import static util.HelperMethods.saveData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class HomeFragment extends Fragment {
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Dialog myDialog;
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmpty, tvAddress, tvError;
    private CardView cvRetry, cvAddress;
    private static ArrayList<ShopItem> shopItems;
    private RelativeLayout rlLoad, rlError;
    private static ShopItem shopItem;

    private View[] vLoaders;
    private static final String TAG = "HomeFragment";
    private RequestQueue requestQueue;

    static ShopItem getShopItem() {
        return shopItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);
        shopItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        mRecyclerView = v.findViewById(R.id.recyclerView);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        tvError = v.findViewById(R.id.tvError);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvAddress = v.findViewById(R.id.tvAddress);
        cvAddress = v.findViewById(R.id.cvAddress);
        cvRetry = v.findViewById(R.id.cvRetry);
        vLoaders = new View[]{v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey)};
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

        tvAddress.setText(getUser().getuAddress());
        //Search for nearby shops
        GETShops(getUser().getuLocation().latitude, getUser().getuLocation().longitude);

        Places.initialize(Objects.requireNonNull(getActivity()), getResources().getString(R.string.google_maps_api_key));

        //Retry button when network error occurs
        cvRetry.setOnClickListener(view -> GETShops(getUser().getuLocation().latitude, getUser().getuLocation().longitude));

        //Initialize maps places
        Places.initialize(getActivity(), getResources().getString(R.string.google_maps_api_key));

        cvAddress.setOnClickListener(view -> {
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME);

            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,
                    fieldList).build(getActivity());
            startActivityForResult(intent, 100);

        });

        return v;
    }

    private void GETShops(Double latitude, Double longitude) {
        Log.d(TAG, "save: LocationData Latitude: " + latitude + "Longitude: " + longitude);
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLoaders[0], vLoaders[1]);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/" + getUser().getuID() + "/" + latitude + "/" + longitude, null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
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
                        e.printStackTrace();
                    }
                },
                error -> {
                    rlError.setVisibility(View.VISIBLE);
                    rlLoad.setVisibility(View.GONE);
                    tvError.setText(getError(error));
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);
    }

    private void getDeviceLocation() {
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

                        GETShops(Objects.requireNonNull(currentLocation).getLatitude(),
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

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private void PUTUserLocation(Place place) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/users/EditLocation",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);

                            getUser().setuAddress(JSONResponse.getString("uAddress"));
                            getUser().setuLocation(new LatLng(JSONResponse.getDouble("uLatitude"), JSONResponse.getDouble("uLongitude")));
                            saveData(Objects.requireNonNull(this.getActivity()).getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), getUser(), true);
                            GETShops(getUser().getuLocation().latitude, getUser().getuLocation().longitude);
                            tvAddress.setText(place.getAddress());
                            getUser().setuLocation(place.getLatLng());

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(getActivity(), getError(error), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uAddress", Objects.requireNonNull(place.getAddress()));
                params.put("uLongitude", String.valueOf(Objects.requireNonNull(place.getLatLng()).longitude));
                params.put("uLatitude", String.valueOf(place.getLatLng().latitude));
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 100) && (resultCode == RESULT_OK)) {
            Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(data));
            PUTUserLocation(place);
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(Objects.requireNonNull(data));
            Toast.makeText(getActivity(), status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}