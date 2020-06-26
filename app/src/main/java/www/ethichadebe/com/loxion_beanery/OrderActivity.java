package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItem;
import SingleItem.IngredientItemCheckbox;
import SingleItem.MenuItem;
import SingleItem.UpcomingOrderItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.combineString;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.getUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getMenuItems;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.setIngredients;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    private RequestQueue requestQueue;
    private static UpcomingOrderItem orderItem;
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private String combos = "";

    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private Double dblPrice = 0.0;
    private int nExtras = 0;
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

        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey), getIngredients() == null);

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

    private void GETIngredients(View vLine, View vLineGrey, boolean isCustom) {
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
                                            true, true,
                                            isCompulsory(Ingredient.getString("iName"))));
                                } else if (isCustom) {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"),
                                            Ingredient.getString("iName"), Ingredient.getDouble("iPrice"),
                                            !isCompulsory(Ingredient.getString("iName")), true,
                                            isCompulsory(Ingredient.getString("iName"))));
                                } else {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"),
                                            Ingredient.getString("iName"), Ingredient.getDouble("iPrice"),
                                            false, true,
                                            isCompulsory(Ingredient.getString("iName"))));
                                }
                            }
                            storeCombos();
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
        setIngredients(null);
        finish();
    }

    @Override
    public void onBackPressed() {
        setIngredients(null);
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
                    ShowLoadingPopup(myDialog, false);
                    try {
                        Log.d(TAG, "POSTOrder: Response " + response);
                        JSONObject Orders = new JSONObject(response);
                        setUpcomingOrderItem(new UpcomingOrderItem(Orders.getInt("oID"),
                                Orders.getString("sName"), Orders.getInt("oNumber"),
                                Orders.getString("createdAt"), Orders.getString("oIngredients"),
                                Orders.getString("oExtras"), Orders.getDouble("oPrice"),
                                Orders.getString("oStatus"), new LatLng(Orders.getDouble("sLatitude"),
                                Orders.getDouble("sLongitude"))));
                        if (getUpcomingOrderItem() != null) {
                            Log.d(TAG, "POSTOrder: Starting activity");
                            startActivity(new Intent(this, OrderConfirmationActivity.class));
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "POSTOrder: exception " +e.toString());
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
            combos += combineString(combo) + "~";
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
        Log.d(TAG, "printCombination: it gets here");
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    private void storeCombos() {
        Log.d(TAG, "storeCombos: combineString(ingredientItems).contains(\", \")");
        for (int i = combineString(ingredientItems).split(", ").length; i >= 1; i--) {
            Log.d(TAG, "storeCombos: it gets here");
            printCombination(combineString(ingredientItems).split(", "),
                    combineString(ingredientItems).split(", ").length, i);
        }
        findMatch();
    }

    private void findMatch() {
        Log.d(TAG, "findMatch: finding match");
        for (String combo : combos.split("~")) {
            for (MenuItem menuItem : getMenuItems()) {
                Log.d(TAG, "findMatch: it gets here combo: " + combo);
                if (menuItem.getStrMenu().toLowerCase().equals(combo.toLowerCase())) {
                    Log.d(TAG, "findMatch: match gotten");
                    //Check if the combo is == to the menu selected
                    System.out.println("--------------------------------------------------------------");
                    System.out.println("Combo: " + combo);
                    System.out.println("Menu item: " + menuItem.getStrMenu());
                    System.out.println("Extra ingredients: " + extraIngredients(menuItem));
                    dblPrice = menuItem.getDblPrice();
                    if (!extraIngredients(menuItem).isEmpty()) {
                        addExtras(menuItem);
                    }
                    tvTotal.setText("R" + dblPrice + "0");
                    return;
                }
            }
        }
    }

    private String extraIngredients(MenuItem menuItem) {
        String leftIngredientItems = combineString(ingredientItems);
        for (String ingredient : menuItem.getStrMenu().split(", ")) {
            Log.d(TAG, "extraIngredients: it gets here");
            leftIngredientItems = leftIngredientItems.replace(ingredient, "");
        }

        return leftIngredientItems.trim();
    }

    private void addExtras(MenuItem menuItem) {
        for (String extra : extraIngredients(menuItem).split(", ")) {
            for (IngredientItemCheckbox ingredientItem : ingredientItems) {
                if (ingredientItem.getStrIngredientName().toLowerCase().equals(extra.toLowerCase())) {
                    dblPrice += ingredientItem.getDblPrice();
                }
            }
        }
    }

    private boolean isCompulsory(String ingredient) {
        for (MenuItem menuItem : getMenuItems()) {
            if (!menuItem.getStrMenu().contains(ingredient))
                return true;
        }
        return false;
    }
}
