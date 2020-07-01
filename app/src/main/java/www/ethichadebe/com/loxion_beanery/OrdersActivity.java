package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

import Adapter.PagerViewAdapter;
import SingleItem.MyShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static util.MyFirebaseMessagingService.IS_ACTIVE;
import static util.MyFirebaseMessagingService.O_ID;
import static util.MyFirebaseMessagingService.S_ADDRESS;
import static util.MyFirebaseMessagingService.S_ID;
import static util.MyFirebaseMessagingService.S_LATITUDE;
import static util.MyFirebaseMessagingService.S_LONGITUDE;
import static util.MyFirebaseMessagingService.S_OH;
import static util.MyFirebaseMessagingService.S_STATUS;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.setNewShop;

public class OrdersActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private LinearLayout llSettings;
    private CardView cvOpen, cvClosed;
    private TextView tvOpen, tvClosed, tvCompleteReg;
    private ViewPager viewPager;
    private int sID;
    public static int oID = -1;
    private PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);
        cvOpen = findViewById(R.id.cvOpen);
        cvClosed = findViewById(R.id.cvClosed);
        llSettings = findViewById(R.id.llSettings);
        tvOpen = findViewById(R.id.tvOpen);
        tvClosed = findViewById(R.id.tvClosed);
        viewPager = findViewById(R.id.container);
        tvCompleteReg = findViewById(R.id.tvCompleteReg);

        //Get notification data
        Intent intent = getIntent();
        oID = intent.getIntExtra(O_ID, -1);
        Log.d(TAG, "onCreate: Check bundle " + oID);
        if (oID != -1) {
            int shopID = intent.getIntExtra(S_ID, -1);
            String name = intent.getStringExtra(S_ID);
            String smallLogo = intent.getStringExtra(S_ID);
            String bigLogo = intent.getStringExtra(S_ID);
            String shortDescript = intent.getStringExtra(S_ID);
            String longDescript = intent.getStringExtra(S_ID);
            LatLng location = new LatLng(intent.getDoubleExtra(S_LATITUDE, -1), intent.getDoubleExtra(S_LONGITUDE, -1));
            String address = intent.getStringExtra(S_ADDRESS);
            String oh = intent.getStringExtra(S_OH);
            boolean isActive = intent.getIntExtra(IS_ACTIVE, -1) == 1;
            int status = intent.getIntExtra(S_STATUS, -1);

            Log.d(TAG, "onCreate: bundle is not empty " + oID);
            setNewShop(new MyShopItem(shopID, name, "", smallLogo, bigLogo, shortDescript, longDescript, location, address, "", 0,
                    oh, isActive, status, 0));
        }
        if (getUser().getuType() == 2) {
            llSettings.setVisibility(View.GONE);
        }//If user is an employee then they cant Update shop details

        tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
        tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));

        if (getNewShop() != null) {
            sID = getNewShop().getIntID();
            switch (getNewShop().getIntStatus()) {
                case 1:
                    cvClosed.setClickable(true);
                    cvOpen.setClickable(false);

                    tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
                    tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
                    PUTStatus(1, getNewShop().getIntID());
                    break;
                case 0:
                    cvClosed.setClickable(false);
                    cvOpen.setClickable(true);

                    tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_red));
                    tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
                    PUTStatus(0, getNewShop().getIntID());
                    break;
            }
            //Check if Shop is fully registered
            if (getNewShop().isActive()) {
                tvCompleteReg.setVisibility(View.VISIBLE);
            }
        }

        tvCompleteReg.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterShopActivity.class));
        });

        cvOpen.setOnClickListener(view -> {
            cvClosed.setClickable(true);
            cvOpen.setClickable(false);

            tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
            PUTStatus(1, sID);
        });

        cvClosed.setOnClickListener(view -> {
            cvClosed.setClickable(false);
            cvOpen.setClickable(true);

            tvClosed.setBackground(getResources().getDrawable(R.drawable.ripple_effect_red));
            tvOpen.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            PUTStatus(0, sID);
        });


        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager pager) {
        PagerViewAdapter pageAdapter = new PagerViewAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pageAdapter.addFragment(new UpcomingOrderFragment(), "Upcoming orders");
        pageAdapter.addFragment(new PastOrderFragment(), "Past orders");
        pager.setAdapter(pageAdapter);
    }

    public void back(View view) {
        setIntFragment(3);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        setIntFragment(3);
        startActivity(new Intent(this, MainActivity.class));
    }

    public void settings(View view) {
        startActivity(new Intent(OrdersActivity.this, ShopSettingsActivity.class));
    }


    private void PUTStatus(int status, int sID) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/Status/" + sID,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("sStatus", String.valueOf(status));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
