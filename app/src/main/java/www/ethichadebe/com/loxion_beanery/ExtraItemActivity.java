package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;
import SingleItem.UpcomingOrderItem;

import static android.view.View.VISIBLE;
import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.combineString;
import static util.HelperMethods.handler;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.getUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.OrderActivity.getOrderItem;

public class ExtraItemActivity extends AppCompatActivity {
    private static final String TAG = "ExtraItemActivity";
    private RelativeLayout rlLoad, rlError;
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private Dialog myDialog;
    private CardView cvRetry;
private TextView tvOder;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_item);

        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        rlLoad = findViewById(R.id.rlLoad);
        tvOder = findViewById(R.id.tvOder);
        rlError = findViewById(R.id.rlError);
        cvRetry = findViewById(R.id.cvRetry);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        ingredientItems = new ArrayList<>();
        myDialog = new Dialog(this);

        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        cvRetry.setOnClickListener(v -> GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey)));
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        IngredientItemCheckboxAdapter mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> ingredientItems.get(position).setChecked());

    }

    private void GETIngredients(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(this);

        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
        //Loads shops starting with the one closest to user
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/Extras/" + getShopItem().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("extras");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Extras = jsonArray.getJSONObject(i);
                                ingredientItems.add(new IngredientItemCheckbox(Extras.getInt("eID"),
                                        Extras.getString("eName"), 0.0, false, false,
                                        true));
                            }
                            tvOder.setVisibility(VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    rlError.setVisibility(VISIBLE);
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

    private void POSTOrder() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/orders/Order",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject Orders = new JSONObject(response);
                        setUpcomingOrderItem(new UpcomingOrderItem(Orders.getInt("oID"),
                                Orders.getString("sName"), Orders.getInt("oNumber"),
                                Orders.getString("oCreatedAt"), Orders.getString("oIngredients"),
                                Orders.getString("oExtras"), Orders.getDouble("oPrice"),
                                Orders.getString("oStatus"), new LatLng(Orders.getDouble("sLatitude"),
                                Orders.getDouble("sLongitude")), null, getResources().getColor(R.color.done)));
                        if (getUpcomingOrderItem() != null) {
                            FirebaseMessaging.getInstance().subscribeToTopic(getShopItem().getStrShopName().replaceAll("[^a-zA-Z0-9]","_") +
                                    getShopItem().getIntID());
                            FirebaseMessaging.getInstance().subscribeToTopic(String.valueOf(Orders.getInt("oID")));
                            startActivity(new Intent(this, OrderConfirmationActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oIngredients", getOrderItem().getStrMenu());
                params.put("oPrice", String.valueOf(getOrderItem().getDblPrice()));
                params.put("sID", String.valueOf(getShopItem().getIntID()));
                params.put("uID", String.valueOf(getUser().getuID()));
                params.put("oExtras", combineString(ingredientItems));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void back(View view) {
        finish();
    }

    public void reload(View view) {
        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
    }

    public void Order(View view) {
        POSTOrder();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
