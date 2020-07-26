package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import Adapter.PagerViewAdapter;

import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;

public class ReportsActivity extends AppCompatActivity {
    private static final String TAG = "OrdersActivity";
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        viewPager = findViewById(R.id.container);

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
}
