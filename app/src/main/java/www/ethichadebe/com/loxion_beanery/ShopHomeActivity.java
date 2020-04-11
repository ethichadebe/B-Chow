package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;
import SingleItem.ShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ShopHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private static String[] ingredients;
    private RelativeLayout rlLoad, rlError;
    private TextView tvName, tvDistance, tvAveTime, tvFullDescrpit;
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    private static MenuItem menuItem;

    public static MenuItem getMenuItem() {
        return menuItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        if (getUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
        }

        MenuItems = new ArrayList<>();
        tvName = findViewById(R.id.tvName);
        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);
        tvDistance = findViewById(R.id.tvDistance);
        tvAveTime = findViewById(R.id.tvAveTime);
        tvFullDescrpit = findViewById(R.id.tvFullDescrpit);

        if (getShopItem().getMenuItems() == null) {
            GETMenu(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        }

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

        //Load menu items starting with the Cheapest
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(MenuItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                menuItem = MenuItems.get(position);
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
        startActivity(new Intent(ShopHomeActivity.this, MainActivity.class));
    }

    private void GETMenu(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/MenuItems/" + getShopItem().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        JSONArray jsonArray = response.getJSONArray("menuItems");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject MenuItem = jsonArray.getJSONObject(i);
                            MenuItems.add(new MenuItem(MenuItem.getInt("mID"), MenuItem.getDouble("mPrice"), MenuItem.getString("mList"),false));
                        }
                        getShopItem().setMenuItems(MenuItems);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    rlError.setVisibility(View.VISIBLE);
                    rlLoad.setVisibility(View.GONE);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShopHomeActivity.this, MainActivity.class));
    }

    public void like(View view) {
    }
}
