package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.MenuItemAdapter;
import SingleItem.ExtraItem;
import SingleItem.MenuItem;
import SingleItem.ShopItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static util.HelperMethods.setStarRating;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class ShopHomeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private static String[] ingredients;
    private RelativeLayout rlLoad, rlError;
    private TextView tvName, tvDistance, tvAveTime, tvFullDescrpit, tvLikes;
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    private static MenuItem menuItem;
    private Dialog myDialog;
    private ImageView ivLike;
    private LinearLayout llLike;

    public static MenuItem getMenuItem() {
        return menuItem;
    }

    public static ArrayList<MenuItem> getMenuItems() {
        return MenuItems;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        MenuItems = new ArrayList<>();
        myDialog = new Dialog(this);
        tvName = findViewById(R.id.tvName);
        ivLike = findViewById(R.id.ivLike);
        llLike = findViewById(R.id.llLike);
        tvLikes = findViewById(R.id.tvLikes);
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

        if(getShopItem() != null){
            GETMenu(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        }

        if (getShopItem().isLiked() == 1) {
            ivLike.setImageResource(R.drawable.ic_favorite_red_24dp);
        }

        tvName.setText(getShopItem().getStrShopName());

        setStarRating(getShopItem().getIntRating(), ivStar1, ivStar2, ivStar3, ivStar4, ivStar5);

        tvLikes.setText(String.valueOf(getShopItem().getIntLikes()));
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
                            MenuItems.add(new MenuItem(MenuItem.getInt("mID"), MenuItem.getDouble("mPrice"), MenuItem.getString("mList"), false));
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
        if (getShopItem().isLiked() == 1) {
            DELETELike();
        } else {
            POSTLike();
        }
    }

    private void POSTLike() {
        llLike.setClickable(false);
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/shoplikes/",
                response -> {
                    llLike.setClickable(true);
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("message").equals("saved")) {
                            ivLike.setImageResource(R.drawable.ic_favorite_red_24dp);
                            tvLikes.setText(String.valueOf(JSONData.getInt("likes")));
                            getShopItem().setLiked(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            llLike.setClickable(true);
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sID", String.valueOf(getShopItem().getIntID()));
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void DELETELike() {
        llLike.setClickable(false);
        HelperMethods.ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://" + getIpAddress() + "/shoplikes/" + getUser().getuID() + "/" + getShopItem().getIntID(), null,
                response -> {
                    llLike.setClickable(true);
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("message").equals("removed")) {
                            ivLike.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            tvLikes.setText(String.valueOf(JSONData.getInt("likes")));
                            getShopItem().setLiked(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Loads shops starting with the one closest to user
                },
                error -> {
                    llLike.setClickable(true);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

    public void Custom(View view) {
        menuItem = MenuItems.get(0);
        ingredients = "Chips".split(", ");
        startActivity(new Intent(ShopHomeActivity.this, OrderActivity.class));
    }
}
