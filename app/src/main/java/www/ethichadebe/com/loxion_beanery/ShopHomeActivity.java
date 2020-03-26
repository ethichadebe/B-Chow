package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;

import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;

public class ShopHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private static String[] ingredients;
    private TextView tvName, tvDistance, tvAveTime, tvFullDescrpit;
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);

        tvName = findViewById(R.id.tvName);
        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        tvDistance = findViewById(R.id.tvDistance);
        tvAveTime = findViewById(R.id.tvAveTime);
        tvFullDescrpit = findViewById(R.id.tvFullDescrpit);

        tvName.setText(getShopItem().getStrShopName());
        switch (getShopItem().getIntRating()) {
            case 0:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setImageResource(0);
                ivStar5.setImageResource(0);
                break;
            case 1:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setImageResource(0);
                ivStar5.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 2:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setImageResource(0);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 3:
                ivStar1.setImageResource(0);
                ivStar2.setImageResource(0);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 4:
                ivStar1.setImageResource(0);
                ivStar2.setVisibility(View.VISIBLE);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
            case 5:
                ivStar1.setVisibility(View.VISIBLE);
                ivStar2.setVisibility(View.VISIBLE);
                ivStar3.setVisibility(View.VISIBLE);
                ivStar4.setVisibility(View.VISIBLE);
                ivStar5.setVisibility(View.VISIBLE);
                break;
        }
        tvDistance.setText("5km");//getShopItem().getDistance);
        tvAveTime.setText(getShopItem().getStrAveTime());
        tvFullDescrpit.setText(getShopItem().getStrFullDescript());

        MenuItems = new ArrayList<>();

        //Load menu items starting with the Cheapest
        MenuItems.add(new MenuItem(1, 10.5, "Chips, French, Eggs", R.drawable.ic_edit_black_24dp,
                R.drawable.ic_delete_black_24dp, View.GONE));
        MenuItems.add(new MenuItem(1, 10.0, "Chips, French, Eggs", R.drawable.ic_edit_black_24dp,
                R.drawable.ic_delete_black_24dp, View.GONE));
        MenuItems.add(new MenuItem(1, 17.0, "Chips, French, Eggs", R.drawable.ic_edit_black_24dp,
                R.drawable.ic_delete_black_24dp, View.GONE));
        MenuItems.add(new MenuItem(1, 10.5, "Chips, French, Eggs, Burger", R.drawable.ic_edit_black_24dp,
                R.drawable.ic_delete_black_24dp, View.GONE));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(MenuItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ingredients = MenuItems.get(position).getStrMenu().split(", ");
                startActivity(new Intent(ShopHomeActivity.this, OrderActivity.class));
            }

            @Override
            public void onEditClick(int position) {
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
    }

    public static String[] getIngredients() {
        return ingredients;
    }

    public void back(View view) {
        finish();
    }
}
