package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import java.util.Objects;

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
    private Double dblPrice, foundPrice = 0.0;
    private RelativeLayout rlLoad, rlError;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        if (getUser() == null){
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
            if (!ingredientItems.get(position).getChecked()) {
                ingredientItems.get(position).setChecked(true);
                dblPrice += ingredientItems.get(position).getDblPrice();
            } else {
                ingredientItems.get(position).setChecked(false);
                dblPrice -= ingredientItems.get(position).getDblPrice();
            }
            tvTotal.setText("R" + dblPrice);
        });

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
                        JSONArray jsonArray = response.getJSONArray("menuItems");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Ingredient = jsonArray.getJSONObject(i);
                            if (isChecked(Ingredient.getString("iName"))) {
                                ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"), Ingredient.getString("iName"), Ingredient.getDouble("iPrice"), true));
                            }
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Ingredient = jsonArray.getJSONObject(i);
                            if (!isChecked(Ingredient.getString("iName"))) {
                                ingredientItems.add(new IngredientItemCheckbox(Ingredient.getInt("iID"), Ingredient.getString("iName"), Ingredient.getDouble("iPrice"), false));
                            }
                        }
                        tvTotal.setText("R" + dblPrice);
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
        if (alreadyExists()) {
            ShowConfirmationPopup();
        }else {
            POSTOrder();
        }
    }

    private boolean isChecked(String IngredientName) {
        for (int i = 0; i < getIngredients().length; i++) {
            if (getIngredients()[i].equals(IngredientName)) {
                return true;
            }
        }
        return false;
    }

    private void POSTOrder() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/orders/Register",
                response -> {
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        ShowLoadingPopup(myDialog, false);
                        startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(OrderActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private boolean alreadyExists() {
        boolean isThere = false;
        String[] mIngredients = combineString(ingredientItems).split(", ");
        for (MenuItem menuItem : getShopItem().getMenuItems()) {
            String[] ingredients = menuItem.getStrMenu().split(", ");

            for (String mIngredient : mIngredients) {
                isThere = false;
                for (String ingredient : ingredients) {
                    if (mIngredient.equals(ingredient)) {
                        isThere = true;
                        break;
                    }
                }
            }

            if (isThere) {
                if (!dblPrice.equals(foundPrice)){
                    foundPrice = menuItem.getDblPrice();
                    return true;
                }
            }
        }
        return false;
    }

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Selected kota already exists in menu. If you wish to continue you'll be charged R" + foundPrice + ". Do you wish to continue?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            POSTOrder();
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
