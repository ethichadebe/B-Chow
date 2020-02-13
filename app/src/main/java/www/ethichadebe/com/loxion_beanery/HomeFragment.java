package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Adapter.ShopItemAdapter;
import SingleItem.ShopItem;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home,container, false);

        final ArrayList<ShopItem> shopItems = new ArrayList<>();

        //Loads shops starting with the one closest to user
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*shopItems.get(position)*/
                startActivity(new Intent(getActivity(), ShopHomeActivity.class));
            }
        });


        return v;
    }
}
