package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import Adapter.SectionsPageAdapter;

import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class OrdersFragment extends Fragment {
    private static final String TAG = "OrdersFragment";
    private ViewPager viewPager;
    private SectionsPageAdapter sectionsPageAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_orders, container, false);
        sectionsPageAdapter = new SectionsPageAdapter(Objects.requireNonNull(getChildFragmentManager()),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        TabLayout tabLayout = v.findViewById(R.id.tabs);
        viewPager = v.findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private void setupViewPager(ViewPager pager){
        SectionsPageAdapter pageAdapter = new SectionsPageAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pageAdapter.addFragment(new UpcomingOrderFragmentCustomer(), "Upcoming orders");
        pageAdapter.addFragment(new PastOrderFragmentCustomer(), "Past orders");
        pager.setAdapter(pageAdapter);
    }
}
