package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.MyShopItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.MyShopItem;
import SingleItem.ShopItem;

public class MyShopsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    LinearLayout llEdit, llBack;//, llShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);

        llBack = findViewById(R.id.llBack);
        llEdit = findViewById(R.id.llEdit);
        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyShopsActivity.this, REegisterShopActivity.class));
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final ArrayList<MyShopItem> shopItems = new ArrayList<>();

        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));
        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,"This is a short description about my shop to attract custommers",
                R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,R.drawable.star,"2km","10-15min"));

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListerner(new MyShopItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //shopItems.get(position).
                startActivity(new Intent(MyShopsActivity.this, OrdersActivity.class));
            }
        });
    }
}
