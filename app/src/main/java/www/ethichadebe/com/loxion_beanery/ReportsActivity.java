package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.PagerViewAdapter;
import SingleItem.MyShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static util.MyFirebaseMessagingService.IS_ACTIVE;
import static util.MyFirebaseMessagingService.O_ID;
import static util.MyFirebaseMessagingService.O_PAST;
import static util.MyFirebaseMessagingService.S_ADDRESS;
import static util.MyFirebaseMessagingService.S_BIG_PICTURE;
import static util.MyFirebaseMessagingService.S_FULL_DESCRIPT;
import static util.MyFirebaseMessagingService.S_ID;
import static util.MyFirebaseMessagingService.S_LATITUDE;
import static util.MyFirebaseMessagingService.S_LONGITUDE;
import static util.MyFirebaseMessagingService.S_NAME;
import static util.MyFirebaseMessagingService.S_OH;
import static util.MyFirebaseMessagingService.S_SHORT_DESCRIPT;
import static util.MyFirebaseMessagingService.S_SMALL_PICTURE;
import static util.MyFirebaseMessagingService.S_STATUS;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.setNewShop;

public class ReportsActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private ViewPager viewPager;
    private PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);
        viewPager = findViewById(R.id.container);


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
        pageAdapter.addFragment(new ReportSummaryFragment(), "Report Summary");
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
        startActivity(new Intent(this, ShopSettingsActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
