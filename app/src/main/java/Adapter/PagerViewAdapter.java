package Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import www.ethichadebe.com.loxion_beanery.PastOrderFragment;
import www.ethichadebe.com.loxion_beanery.UpcomingOrderFragment;

public class PagerViewAdapter extends FragmentPagerAdapter {
    public PagerViewAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0){
            fragment = new UpcomingOrderFragment();
        }else {
            fragment  = new PastOrderFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
