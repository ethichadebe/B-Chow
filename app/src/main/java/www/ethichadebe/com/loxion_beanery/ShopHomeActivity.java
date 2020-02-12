package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;

public class ShopHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private static String[] ingredients;

    LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);

        llBack = findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
}
