package www.ethichadebe.com.loxion_beanery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import SingleItem.UpcomingOrderItem;
import util.MyFirebaseMessagingService;

import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static util.MyFirebaseMessagingService.O_ID;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static int intFragment;
    private BottomNavigationView bottomNav, bottomNavAdmin;
    private static UpcomingOrderItem upcomingOrderItem;

    static UpcomingOrderItem getUpcomingOrderItem() {
        return upcomingOrderItem;
    }

    static void setUpcomingOrderItem(UpcomingOrderItem upcomingOrderItem) {
        MainActivity.upcomingOrderItem = upcomingOrderItem;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNavAdmin = findViewById(R.id.bottom_navigation_admin);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        //Get notification data
        Intent intent = getIntent();
        int oID = intent.getIntExtra(O_ID, -1);

        Log.d(TAG, "onCreate: Check bundle " + oID);
        if (oID != -1) {
            Log.d(TAG, "onCreate: bundle is not empty " + oID);
            upcomingOrderItem = new UpcomingOrderItem(oID, "", 1, "", "", "", 0.0, "",
                    null, null);
            intFragment = 1;
        }

        if (getUser().getuType() == 3) {
            bNav();
        } else {
            bNavAdmin();
        }

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public static void setIntFragment(int intFragment) {
        MainActivity.intFragment = intFragment;
    }

    private void bNav() {
        bottomNavAdmin.setVisibility(View.GONE);
        //On Back Press
        switch (intFragment) {
            case 1:
                intFragment = -1;
                bottomNav.setSelectedItemId(R.id.nav_orders);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OrdersFragment()).commit();

                break;
            case 2:
                intFragment = -1;
                bottomNav.setSelectedItemId(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            default:
                //Start Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
        }

        bottomNav.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_orders:
                    selectedFragment = new OrdersFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            //First fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    Objects.requireNonNull(selectedFragment)).commit();

            return true;
        });
    }

    private void bNavAdmin() {
        bottomNav.setVisibility(View.GONE);
        //On Back Press
        switch (intFragment) {
            case 1:
                intFragment = -1;
                bottomNavAdmin.setSelectedItemId(R.id.nav_orders);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OrdersFragment()).commit();
                break;
            case 2:
                intFragment = -1;
                bottomNavAdmin.setSelectedItemId(R.id.nav_profile);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case 3:
                intFragment = -1;
                bottomNavAdmin.setSelectedItemId(R.id.nav_shops);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyShopsFragment()).commit();
                break;
            default:
                //Start Fragment
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
        }


        bottomNavAdmin.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_orders:
                    selectedFragment = new OrdersFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.nav_shops:
                    selectedFragment = new MyShopsFragment();
                    break;
            }

            //First fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    Objects.requireNonNull(selectedFragment)).commit();

            return true;
        });
    }
}