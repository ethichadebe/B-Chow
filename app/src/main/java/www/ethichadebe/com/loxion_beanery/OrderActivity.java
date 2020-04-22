package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;
import SingleItem.MenuItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.combineString;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.HomeFragment.getShopItem;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getMenuItem;

public class OrderActivity extends AppCompatActivity {
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private Double dblPrice = 0.0;
    static int oID = -1;
    private RelativeLayout rlLoad, rlError;
    private Dialog myDialog;
    private boolean alreadyExists;

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

        dblPrice = getMenuItem().getDblPrice();

        GETIngredients(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            //checkChecked();
            if (!ingredientItems.get(position).getChecked()) {
                ingredientItems.get(position).setChecked(true);
                checkMenu();
                if (alreadyExists) {
                    dblPrice += ingredientItems.get(position).getDblPrice();
                }
            } else {
                ingredientItems.get(position).setChecked(false);
                checkMenu();
                if (alreadyExists) {
                    dblPrice -= ingredientItems.get(position).getDblPrice();
                }
            }
            tvTotal.setText("R" + dblPrice + "0");
        });

    }

    private boolean determinePrice(String string) {
        for (MenuItem menuItem : getShopItem().getMenuItems()) {
            if (menuItem.getStrMenu().equals(string)) {
                dblPrice = menuItem.getDblPrice();
                return false;
            }
        }
        return true;
    }

    private void checkMenu() {
        StringBuilder MenuList = new StringBuilder();
        for (int i = 0; i < ingredientItems.size(); i++) {
            if (ingredientItems.get(i).getChecked()) {
                MenuList.append(ingredientItems.get(i).getStrIngredientName()).append(", ");
                alreadyExists = determinePrice(String.valueOf(MenuList).substring(0, String.valueOf(MenuList).length() - 2));
            }
        }
    }

    private void GETIngredients(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/Ingredients/" + getShopItem().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("ingredients");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Ingredient = jsonArray.getJSONObject(i);
                                if (isChecked(Ingredient.getString("iName"))) {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"), Ingredient.getString("iName"),
                                            Ingredient.getDouble("iPrice"), true, false));
                                } else {
                                    ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"), Ingredient.getString("iName"),
                                            Ingredient.getDouble("iPrice"), false, false));

                                }
                            }
                            tvTotal.setText("R" + dblPrice+"0");
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

    public void back(View view) {
        finish();
    }

    public void Order(View view) {
        POSTOrder();
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
                "http://" + getIpAddress() + "/orders/Order",
                response -> {
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        Toast.makeText(this, JSONResponse.toString(), Toast.LENGTH_LONG).show();
                        oID = JSONResponse.getInt("data");
                        ShowLoadingPopup(myDialog, false);
                        if (oID!=-1){
                            startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("oIngredients", combineString(ingredientItems));
                params.put("oPrice", String.valueOf(dblPrice));
                params.put("sID", String.valueOf(getShopItem().getIntID()));
                params.put("uID", String.valueOf(getUser().getuID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void checkChecked() {
        boolean oneIisChecked = false;
        for (IngredientItemCheckbox ingredientItem : ingredientItems) {
            if (ingredientItem.getChecked()) {
                oneIisChecked = true;
                break;
            }
        }

        if (oneIisChecked) {
            findViewById(R.id.rlBottom).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.rlBottom).setVisibility(View.GONE);
        }
    }
}
