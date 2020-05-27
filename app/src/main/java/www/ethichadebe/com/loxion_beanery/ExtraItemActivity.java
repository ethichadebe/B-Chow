package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
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

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.combineString;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.OrderActivity.getOrderItem;
import static www.ethichadebe.com.loxion_beanery.OrderActivity.oID;

public class ExtraItemActivity extends AppCompatActivity {
    private static final String TAG = "ExtraItemActivity";
    private RelativeLayout rlLoad, rlError;
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private Dialog myDialog;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_item);

        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        ingredientItems = new ArrayList<>();
        myDialog = new Dialog(this);

        //ingredientItems.add(new IngredientItemCheckbox(1,"Extras.getString(eName)",0.0, false, false));
        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        IngredientItemCheckboxAdapter mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> ingredientItems.get(position).setChecked());

    }

    private void GETIngredients(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
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
                                        Extras.getString("eName"), 0.0, false, false));
                            }
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
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    private void POSTOrder() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/orders/Order",
                response -> {
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        oID = JSONResponse.getInt("data");
                        ShowLoadingPopup(myDialog, false);
                        if (oID != -1) {
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
