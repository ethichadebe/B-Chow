package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;
import SingleItem.MenuItem;
import SingleItem.UpcomingOrderItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.combineString;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getMenuItem;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getMenuItems;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private RequestQueue requestQueue;
    private static UpcomingOrderItem orderItem;
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private String combos;

    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private Double dblPrice = 0.0;
    private int nExtras = 0;
    static int oID = -1;
    private RelativeLayout rlLoad, rlError;
    private Dialog myDialog;

    public static UpcomingOrderItem getOrderItem() {
        return orderItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        tvTotal = findViewById(R.id.tvTotal);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        mRecyclerView = findViewById(R.id.recyclerView);
        ingredientItems = new ArrayList<>();
        myDialog = new Dialog(this);

        //Get menu item price and display it
        if (getMenuItem() != null) {
            dblPrice = getMenuItem().getDblPrice();
        }
        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey),getMenuItem() != null);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            combos = "";
            ingredientItems.get(position).setChecked();
            storeCombos();
        });

    }

    private void GETIngredients(View vLine, View vLineGrey, boolean isNotCustom) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/Ingredients/" + getShopItem().getIntID(), null,
                response -> {
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("ingredients");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Ingredient = jsonArray.getJSONObject(i);
                                nExtras = Ingredient.getInt("nExtras");
                                if (isChecked(Ingredient.getString("iName"))) {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"),
                                            Ingredient.getString("iName"), Ingredient.getDouble("iPrice"),
                                            true, false));
                                } else {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"),
                                            Ingredient.getString("iName"), Ingredient.getDouble("iPrice"),
                                            false, false));
                                }
                            }

                            

                            tvTotal.setText("R" + dblPrice + "0");
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

    public void back(View view) {
        finish();
    }

    public void Order(View view) {
        if (nExtras > 0) {
            orderItem = new UpcomingOrderItem(getShopItem().getIntID(), "", 1, "", combineString(ingredientItems),
                    "", dblPrice, "", new LatLng(0.0, 0.0));
            startActivity(new Intent(this, ExtraItemActivity.class));
        } else {
            POSTOrder();
        }
    }

    private boolean isChecked(String IngredientName) {
        if (getIngredients() != null) {
            for (int i = 0; i < getIngredients().length; i++) {
                if (getIngredients()[i].equals(IngredientName)) {
                    return true;
                }
            }
        }
        return false;
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
                            startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
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
                params.put("oIngredients", combineString(ingredientItems));
                params.put("oPrice", String.valueOf(dblPrice));
                params.put("sID", String.valueOf(getShopItem().getIntID()));
                params.put("uID", String.valueOf(getUser().getuID()));
                params.put("oExtras", "");
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }


    /* arr[]  ---> Input Array
data[] ---> Temporary array to store current combination
start & end ---> Staring and Ending indexes in arr[]
index  ---> Current index in data[]
r ---> Size of a combination to be printed */
    private void combinationUtil(String[] arr, String[] data, int start, int end, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            String combo = "";
            for (int j = 0; j < r; j++) {
                combo += data[j] + ", ";

            }
            if (combo.toLowerCase().contains("chips")) {
                combos += combineString(combo) + "~";
            }
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    private void printCombination(String[] arr, int n, int r) {
        // A temporary array to store all combination one by one
        String[] data = new String[r];

        // Print all combination using temporary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    private void storeCombos() {
        for (int i = 1; i <= combineString(ingredientItems).split(", ").length; i++) {

            printCombination(combineString(ingredientItems).split(", "),
                    combineString(ingredientItems).split(", ").length, i);
        }

        Log.d(TAG, "storeCombos: Find match");
        for (String combo : combos.split("~")) {
            Log.d(TAG, "storeCombos: " + combo);
            for (MenuItem menuItem : getMenuItems()) {
                if (menuItem.getStrMenu().toLowerCase().equals(combo.toLowerCase())) {
                    dblPrice = menuItem.getDblPrice();
                    tvTotal.setText("R" + dblPrice + "0");
                }
            }
        }

    }

}
