package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import Adapter.MyShopItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import SingleItem.MyShopItem;

import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.isNew;
import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.setIsNew;

public class MyShopsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior bsbBottomSheetBehavior;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    };
    private LinearLayout llEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);

        llEdit = findViewById(R.id.llEdit);
        View bsbBottomSheet= findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        if (isNew()){
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            handler.postDelayed(runnable, 1500);
            setIsNew(false);
        }

        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyShopsActivity.this, RegisterShopActivity.class));
            }
        });

        final ArrayList<MyShopItem> shopItems = new ArrayList<>();

        Location location = new Location("");
        location.setLatitude(0.0);
        location.setLongitude(0.0);

        StringBuilder strOperatingHrs = new StringBuilder();

        ArrayList<IngredientItem> ingredientItems = new ArrayList<>();

        ArrayList<MenuItem> menuItems = new ArrayList<>();
        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,R.drawable.biglogo,
                "This is a short descrption","This a full shop description for the specific shop",
                location,"10-15 mins",3,strOperatingHrs,ingredientItems,menuItems));

        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,R.drawable.biglogo,
                "This is a short descrption","This a full shop description for the specific shop",
                location,"10-15 mins",3,strOperatingHrs,ingredientItems,menuItems));

        shopItems.add(new MyShopItem(1,"Shop name","Owner",R.drawable.food,R.drawable.biglogo,
                "This is a short descrption","This a full shop description for the specific shop",
                location,"10-15 mins",3,strOperatingHrs,ingredientItems,menuItems));

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyShopsActivity.this, MainActivity.class));
    }

    public void back(View view) {
        startActivity(new Intent(MyShopsActivity.this, MainActivity.class));
    }
}
