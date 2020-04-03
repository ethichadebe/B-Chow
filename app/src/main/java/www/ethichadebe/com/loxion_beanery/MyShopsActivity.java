package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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
    private final ArrayList<MyShopItem> shopItems = new ArrayList<>();
    private RelativeLayout rlError, rlLoad;
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
    private View bsbBottomSheet;

    Location location = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shops);

        bsbBottomSheet = findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        location.setLatitude(0.0);
        location.setLongitude(0.0);

        if (isNew()) {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            handler.postDelayed(runnable, 1500);
            setIsNew(false);
        }
        GETShops(findViewById(R.id.vLine),findViewById(R.id.vLineGrey));
        mRecyclerView = findViewById(R.id.recyclerView);
        rlError = findViewById(R.id.rlError);
        rlLoad = findViewById(R.id.rlLoad);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MyShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListerner(position -> {
            //shopItems.get(position).
            startActivity(new Intent(MyShopsActivity.this, OrdersActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyShopsActivity.this, MainActivity.class));
    }

    public void back(View view) {
        startActivity(new Intent(MyShopsActivity.this, MainActivity.class));
    }

    public void Edit(View view) {
        startActivity(new Intent(MyShopsActivity.this, RegisterShopActivity.class));
    }

    private void GETShops(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/"+getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        JSONArray jsonArray = response.getJSONArray("shops");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Shops = jsonArray.getJSONObject(i);
                            Location location = new Location("");
                            String[] strCoord = Shops.getString("sLocation").split(" ");
                            location.setLatitude(23.4);//Double.parseDouble(strCoord[0]));
                            location.setLongitude(32.5);//Double.parseDouble(strCoord[1]));
                            shopItems.add(new MyShopItem(Shops.getInt("sID"), Shops.getString("sName"),
                                    Shops.getString("uPosition"), R.drawable.food, R.drawable.biglogo,
                                    Shops.getString("sShortDescrption"),Shops.getString("sFullDescription"),
                                    location,"10-15 mins", Shops.getInt("sRating"),
                                    Shops.getString("sOperatingHrs")));
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

}
