package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;

import Adapter.MyShopItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.MyShopItem;
import SingleItem.ShopItem;

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        final SpaceNavigationView spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("ORDERs", R.drawable.ic_reorder_black_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("PROFILE", R.drawable.ic_person_black_24dp));

        spaceNavigationView.setCentreButtonSelectable(true);
        spaceNavigationView.setCentreButtonSelected();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if (itemIndex == 1){
                    startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });

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
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*shopItems.get(position)*/
                startActivity(new Intent(HomePageActivity.this, ShopHomeActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
