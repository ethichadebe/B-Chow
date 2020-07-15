package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.DisplayImage;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.checkData;
import static util.HelperMethods.handler;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;

public class ShopHomeActivity extends AppCompatActivity {
    private static final String TAG = "ShopHomeActivity";
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private static String[] ingredients;
    private RelativeLayout rlLoad, rlError,rlShop;
    private TextView tvDistance, tvAveTime, tvFullDescrpit;
    private ImageView ivBig;
    private FloatingActionButton fabCustom;
    private RatingBar rbRating;
    private LinearLayout llShop;
    private CardView cvRetry;

    public static ArrayList<MenuItem> getMenuItems() {
        return MenuItems;
    }

    private LottieAnimationView lottieAnimationView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        Toolbar toolbar = findViewById(R.id.tToolBar);
        toolbar.setNavigationOnClickListener(view -> {
           finish();
        });
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.ctlCollapsingToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBarLayout = findViewById(R.id.aplAppBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    lottieAnimationView.setVisibility(View.GONE);
                    isShow = true;
                } else if (isShow) {
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        lottieAnimationView = findViewById(R.id.lottieLike);

        MenuItems = new ArrayList<>();
        ivBig = findViewById(R.id.ivBig);
        rbRating = findViewById(R.id.rbRating);
        fabCustom = findViewById(R.id.fabCustom);
        rlLoad = findViewById(R.id.rlLoad);
        rlShop = findViewById(R.id.rlShop);
        cvRetry = findViewById(R.id.cvRetry);
        llShop = findViewById(R.id.llShop);
        rlError = findViewById(R.id.rlError);
        tvDistance = findViewById(R.id.tvDistance);
        tvAveTime = findViewById(R.id.tvAveTime);
        tvFullDescrpit = findViewById(R.id.tvFullDescrpit);

        if (getShopItem().getIntStatus() != 1) {
            fabCustom.setVisibility(View.GONE);
        }
        if (getShopItem() != null) {
            GETMenu(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        }
        rlShop.setOnClickListener(v -> GETMenu(findViewById(R.id.vLine), findViewById(R.id.vLineGrey)));
        if (Objects.requireNonNull(getShopItem()).isLiked() == 1) {
            lottieAnimationView.setSpeed(1);
            lottieAnimationView.playAnimation();
            //ivLike.setImageResource(R.drawable.ic_favorite_red_24dp);
        }
        collapsingToolbarLayout.setTitle(getShopItem().getStrShopName());
        tvDistance.setText(getShopItem().getStrAddress());
        tvAveTime.setText(getShopItem().getStrAveTime());
        tvFullDescrpit.setText(getShopItem().getStrFullDescript());
        rbRating.setRating(Float.parseFloat(String.valueOf(getShopItem().getIntRating())));
        DisplayImage(ivBig, getShopItem().getStrLogoBig());

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
                if (getShopItem().getIntStatus() == 1) {
                    ingredients = MenuItems.get(position).getStrMenu().split(", ");
                    startActivity(new Intent(ShopHomeActivity.this, OrderActivity.class));
                }
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

    public static void setIngredients(String[] ingredients) {
        ShopHomeActivity.ingredients = ingredients;
    }

    public void back(View view) {
        startActivity(new Intent(ShopHomeActivity.this, MainActivity.class));
    }

    private void GETMenu(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/MenuItems/" + getShopItem().getIntID(), null,
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
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ShopHomeActivity.this, MainActivity.class));
    }
    // Back arrow click event to go to the parent Activity
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void like(View view) {
        if (getShopItem().isLiked() == 1) {
            DELETELike();
        } else {
            POSTLike();
        }
    }

    private void POSTLike() {
        lottieAnimationView.setClickable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/shoplikes/",
                response -> {
                    lottieAnimationView.setClickable(true);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("message").equals("saved")) {
                            lottieAnimationView.setSpeed(1);
                            lottieAnimationView.playAnimation();
                            getShopItem().setLiked(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            lottieAnimationView.setClickable(true);
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

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void DELETELike() {
        lottieAnimationView.setClickable(false);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getIpAddress() + "/shoplikes/" + getUser().getuID() + "/" + getShopItem().getIntID(), null,
                response -> {
                    lottieAnimationView.setClickable(true);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("message").equals("removed")) {
                            lottieAnimationView.setSpeed(-1);
                            lottieAnimationView.playAnimation();
                            getShopItem().setLiked(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Loads shops starting with the one closest to user
                },
                error -> {
                    lottieAnimationView.setClickable(true);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    public void Custom(View view) {
        startActivity(new Intent(ShopHomeActivity.this, OrderActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
