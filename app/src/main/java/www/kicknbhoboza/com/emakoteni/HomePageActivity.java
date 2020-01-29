package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.MyShopItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.MyShopItem;
import SingleItem.ShopItem;

public class HomePageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ArrayList<ShopItem> shopItems = new ArrayList<>();

        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new ShopItem("Shop name",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ivProfile = findViewById(R.id.ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePageActivity.this, UserProfileActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}
