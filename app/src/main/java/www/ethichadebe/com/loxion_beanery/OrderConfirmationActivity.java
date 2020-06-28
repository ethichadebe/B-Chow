package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;
import util.FetchURL;
import util.TaskLoadedCallback;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.ismLocationGranted;
import static util.HelperMethods.loadData;
import static util.HelperMethods.randomNumber;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.getUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.PastOrderFragmentCustomer.getPastOrderItem;
import static www.ethichadebe.com.loxion_beanery.PastOrderFragmentCustomer.setPastOrderItem;

public class OrderConfirmationActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final String TAG = "OrderConfirmationActivi";
    private RequestQueue requestQueue;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;

        if (ismLocationGranted()) {
            getDeviceLocation();
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
        }
    }

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Polyline currentPolyline;
    private View vBackground;
    private ImageView ivCenter, ivGoogle;
    private LinearLayout llOptions;
    private TextView tvUpdate, tvUpdateSmall, tvOkay, tvCancel, tvShopName, tvMenuList, tvExtrasList, tvOrderNum;
    private View[] vLineGrey = new View[3];
    private View[] vLineLoad = new View[4];
    private View[] vLine = new View[4];
    private Handler handler = new Handler();
    private Dialog myDialog;
    private GifImageView gifIcon;
    private CardView cvNavigate, cvBottom;
    private BottomSheetBehavior bottomSheetBehavior;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.VISIBLE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.VISIBLE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.GONE);

            vBackground.setVisibility(View.GONE);
            vLine[0].setVisibility(View.GONE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            tvUpdate.setText("Order has been placed");
            tvUpdateSmall.setText("Make sure you've arrived at the shop before and hour");
            gifIcon.setImageResource(R.drawable.driving);
            YoyoSlideRight(5000, R.id.vLine1Load);
        }
    };

    private Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.VISIBLE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.VISIBLE);
            vLineLoad[2].setVisibility(View.GONE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.GONE);
            vLine[2].setVisibility(View.GONE);

            vBackground.setVisibility(View.VISIBLE);
            ivCenter.setVisibility(View.GONE);
            ivGoogle.setVisibility(View.GONE);
            cvNavigate.setVisibility(View.GONE);
            tvOkay.setVisibility(View.VISIBLE);
            llOptions.setVisibility(View.GONE);

            tvUpdate.setText("The Shop has been notified on your arrival");
            tvUpdateSmall.setText("and now lets patiently wait for the shop to complete the order");
            gifIcon.setImageResource(R.drawable.waiting);
            YoyoSlideRight(5000, R.id.vLine2Load);
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vLineGrey[0].setVisibility(View.GONE);
            vLineGrey[1].setVisibility(View.GONE);

            vLineLoad[0].setVisibility(View.GONE);
            vLineLoad[1].setVisibility(View.GONE);
            vLineLoad[2].setVisibility(View.VISIBLE);

            vLine[0].setVisibility(View.VISIBLE);
            vLine[1].setVisibility(View.VISIBLE);
            vLine[2].setVisibility(View.GONE);

            vBackground.setVisibility(View.VISIBLE);
            cvNavigate.setVisibility(View.GONE);
            ivCenter.setVisibility(View.GONE);
            ivGoogle.setVisibility(View.GONE);
            tvOkay.setVisibility(View.VISIBLE);
            llOptions.setVisibility(View.GONE);

            tvUpdate.setText("Your order is ready for collection");
            tvUpdateSmall.setText("Your order is complete, Please collect and enjoy");
            gifIcon.setImageResource(R.drawable.eating);
            YoyoSlideRight(500, R.id.vLine3Load);
        }
    };
    private Runnable runnableExpand = () -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    private Runnable runnableCollapse = () -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.AdMob_Interstitial_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        myDialog = new Dialog(this);
        tvOkay = findViewById(R.id.tvOkay);
        tvUpdateSmall = findViewById(R.id.tvUpdateSmall);
        tvUpdate = findViewById(R.id.tvUpdate);
        tvCancel = findViewById(R.id.tvCancel);
        tvMenuList = findViewById(R.id.tvMenuList);
        tvOrderNum = findViewById(R.id.tvOrderNum);
        llOptions = findViewById(R.id.llOptions);
        ivGoogle = findViewById(R.id.ivGoogle);
        tvShopName = findViewById(R.id.tvShopName);
        tvExtrasList = findViewById(R.id.tvExtrasList);
        cvNavigate = findViewById(R.id.cvNavigate);
        gifIcon = findViewById(R.id.gifIcon);
        cvBottom = findViewById(R.id.cvBottom);
        vBackground = findViewById(R.id.vBackground);
        ivCenter = findViewById(R.id.ivCenter);

        vLineGrey[0] = findViewById(R.id.vLine2Grey);
        vLineGrey[1] = findViewById(R.id.vLine3Grey);

        vLineLoad[0] = findViewById(R.id.vLine1Load);
        vLineLoad[1] = findViewById(R.id.vLine2Load);
        vLineLoad[2] = findViewById(R.id.vLine3Load);

        vLine[0] = findViewById(R.id.vLine1);
        vLine[1] = findViewById(R.id.vLine2);
        vLine[2] = findViewById(R.id.vLine3);

        bottomSheetBehavior = BottomSheetBehavior.from(cvBottom);
        handler.postDelayed(runnableExpand, 2000);
        handler.postDelayed(runnableCollapse, 5000);

        if (getUpcomingOrderItem() != null) {
            tvShopName.setText(getUpcomingOrderItem().getStrShopName());
            tvMenuList.setText(getUpcomingOrderItem().getStrMenu());
            tvExtrasList.setText(getUpcomingOrderItem().getStrExtras());
            tvOrderNum.setText(String.valueOf(getUpcomingOrderItem().getIntOderNum()));
            switch (getUpcomingOrderItem().getStrStatus()) {
                case "Waiting arrival":
                    handler.postDelayed(runnable, 0);
                    break;
                case "Waiting for order":
                    handler.postDelayed(runnable1, 0);
                    break;
                case "Ready for collection":
                    handler.postDelayed(runnable2, 0);
                    break;
            }

        } else {
            handler.postDelayed(runnable, 0);
        }

        tvCancel.setOnClickListener(view -> {
            if (getUpcomingOrderItem() != null) {
                ShowConfirmationPopup(getUpcomingOrderItem().getIntID());
            }
        });
        tvOkay.setOnClickListener(view -> {
            setUpcomingOrderItem(null);
            setIntFragment(1);
            showAd();
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                setIntFragment(1);
                startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                setIntFragment(1);
                startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
            }
        });

        initMap();
    }

    private void showAd() {
        //Show ad
        if (mInterstitialAd.isLoaded() && randomNumber(1)) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void YoyoSlideRight(int repeat, int vLine) {
        YoYo.with(Techniques.ZoomInRight)
                .duration(800)
                .repeat(repeat)
                .playOn(findViewById(vLine));
    }

    @Override
    public void onBackPressed() {
        setUpcomingOrderItem(null);
        setPastOrderItem(null);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void arrived(View view) {
        if (getUpcomingOrderItem() != null) {
            PUTArrived(getUpcomingOrderItem().getIntID());
        }
    }

    private void PUTArrived(int oID) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Arrived/" + oID,
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    ShowLoadingPopup(myDialog, false);
                    handler.postDelayed(runnable1, 0);
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this,    error.toString(), Toast.LENGTH_LONG).show();
        });

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void PUTCancel(int oID) {
        ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Cancel/" + oID,
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    ShowLoadingPopup(myDialog, false);
                    startActivity(new Intent(this, MainActivity.class));
                    showAd();
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void ShowConfirmationPopup(int ID) {
        TextView tvCancel, tvMessage, btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Are you sure?");

        btnYes.setOnClickListener(view -> PUTCancel(ID));

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void home(View view) {
        setUpcomingOrderItem(null);
        setPastOrderItem(null);
        startActivity(new Intent(this, MainActivity.class));
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (ismLocationGranted()) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: location found");
                        Location currentLocation = (Location) task.getResult();

                        //Add marker to shop
                        MarkerOptions markerOptions;
                        if (getUpcomingOrderItem() != null) {
                            markerOptions = new MarkerOptions().position(getUpcomingOrderItem().getLlShop())
                                    .title(getUpcomingOrderItem().getStrShopName());
                            mMap.addMarker(markerOptions);

                            new FetchURL(this).execute(getUrl(new
                                    LatLng(Objects.requireNonNull(currentLocation).getLatitude(),
                                    currentLocation.getLongitude()), getUpcomingOrderItem().getLlShop()), "driving");
                            moveCam(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    getUpcomingOrderItem().getLlShop());
                        }
                    } else {
                        Log.d(TAG, "onComplete: Unable to get location");
                        Toast.makeText(this, "Unable to get current location",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security exception " + e.getMessage());
        }
    }

    private void moveCam(LatLng me, LatLng shop) {
        //Log.d(TAG, "moveCam: Moving camera to Lat: " + latLng.latitude + "Long: " + latLng.longitude);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(me).include(shop);

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    private void initMap() {
        Log.d(TAG, "initMap: Initialising map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    public void center(View view) {
        getDeviceLocation();
    }

    private String getUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" +
                getString(R.string.google_maps_api_key);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void navigate(View view) {
        Uri gmmIntentUri;
        if (getUpcomingOrderItem() != null) {
            gmmIntentUri = Uri.parse("google.navigation:q=" + getUpcomingOrderItem().getLlShop().latitude + "," +
                    getUpcomingOrderItem().getLlShop().longitude);
        } else {
            gmmIntentUri = Uri.parse("google.navigation:q=" + getShopItem().getLlLocation().latitude + "," +
                    getShopItem().getLlLocation().longitude);
        }

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
