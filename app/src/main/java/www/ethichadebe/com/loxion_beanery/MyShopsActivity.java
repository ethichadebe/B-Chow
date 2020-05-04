package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.MyShopItemAdapter;
import SingleItem.MyShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.isNew;
import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.setIsNew;

public class MyShopsActivity extends AppCompatActivity {
    private ArrayList<MyShopItem> shopItems;
    private RelativeLayout rlError, rlLoad;
    private RecyclerView mRecyclerView;
    private MyShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior bsbBottomSheetBehavior;
    private static MyShopItem newShop;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    };
    private View bsbBottomSheet;
    private TextView tvEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        newShop = null;
        bsbBottomSheet = findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        if (isNew() && !isEdit) {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            handler.postDelayed(runnable, 1500);
            setIsNew(false);
        }
        isEdit = false;

        shopItems = new ArrayList<>();
        rlError = findViewById(R.id.rlError);
        rlLoad = findViewById(R.id.rlLoad);
        tvEmpty = findViewById(R.id.tvEmpty);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyShopItemAdapter(shopItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        Location location = new Location("");
        location.setLatitude(23.4);//Double.parseDouble(strCoord[0]));
        location.setLongitude(32.5);//Double.parseDouble(strCoord[1]));
        GETShops(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));

        mAdapter.setOnItemClickListerner(new MyShopItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                newShop = shopItems.get(position);
                startActivity(new Intent(MyShopsActivity.this, OrdersActivity.class));

            }

            @Override
            public void onItemClickResumeRegistration(int position) {
                newShop = shopItems.get(position);
                startActivity(new Intent(MyShopsActivity.this, RegisterShopActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UserSettingsActivity.class));
    }

    public void back(View view) {
        startActivity(new Intent(this, UserSettingsActivity.class));
    }

    public void createShop(View view) {
        startActivity(new Intent(MyShopsActivity.this, RegisterShopActivity.class));
    }

    private void GETShops(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/MyShops/" + getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("shops");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Shops = jsonArray.getJSONObject(i);
                                Location location = new Location("");
                                String[] strCoord = Shops.getString("sLocation").split(" ");
                                Drawable bgStatus = null;
                                location.setLatitude(Double.parseDouble(strCoord[0]));
                                location.setLongitude(Double.parseDouble(strCoord[1]));
                                boolean isActive = false;
                                if (Shops.getInt("isActive") == 1) {
                                    isActive = true;
                                }
                                switch (Shops.getString("sStatus")){
                                    case "Open":
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open);
                                        break;
                                    case "Unavailable":
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_unavailable);
                                        break;
                                    case "Closed":
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open_closed);
                                        break;
                                }
                                shopItems.add(new MyShopItem(Shops.getInt("sID"), Shops.getString("sName"),
                                        Shops.getString("uRole"), R.drawable.food, R.drawable.biglogo,
                                        Shops.getString("sShortDescrption"), Shops.getString("sFullDescription"),
                                        location, "10-15 mins", Shops.getInt("sRating"),
                                        Shops.getString("sOperatingHrs"), isActive, bgStatus));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
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

    public void reload(View view) {
        GETShops(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
    }

    public static MyShopItem getNewShop() {
        return newShop;
    }

    public static void setNewShop(MyShopItem newShop) {
        MyShopsActivity.newShop = newShop;
    }

}
