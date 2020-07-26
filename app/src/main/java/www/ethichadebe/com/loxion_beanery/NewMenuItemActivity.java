package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

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
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.combineString;
import static util.HelperMethods.getError;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIntPosition;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getDblPrice;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.setIngredients;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;

public class NewMenuItemActivity extends AppCompatActivity {
    private static final String TAG = "NewMenuItemActivity";
    private RequestQueue requestQueue;
    private static ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Double dblPrice = 0.0;
    private MaterialEditText etPrice;
    private Dialog myDialog;
    private TextView tvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        tvAdd = findViewById(R.id.tvAdd);
        etPrice = findViewById(R.id.etPrice);
        myDialog = new Dialog(this);

        //Check if There are checked ingredients
        if (getIngredients().size() > 0) {
            dblPrice = getDblPrice();
            etPrice.setText(String.valueOf(getDblPrice()));
            tvAdd.setText("Edit");
            ingredientItems = new ArrayList<>();
            etPrice.setText(String.valueOf(getDblPrice()));

            for (int i = 0; i < getIngredients().size(); i++) {
                ingredientItems.add(new IngredientItemCheckbox(getIngredients().get(i), true, true,
                        true));
            }

            for (int i = 0; i < getNewShop().getIngredientItems().size(); i++) {
                boolean isThere = false;
                for (int b = 0; b < getIngredients().size(); b++) {
                    if (getNewShop().getIngredientItems().get(i).getStrIngredientName().
                            equals(getIngredients().get(b).getStrIngredientName())) {
                        isThere = true;
                        break;
                    }
                }
                if (!isThere) {
                    ingredientItems.add(new IngredientItemCheckbox(getNewShop().getIngredientItems().get(i).getIntID(),
                            getNewShop().getIngredientItems().get(i).getStrIngredientName(),
                            getNewShop().getIngredientItems().get(i).getDblPrice(), false, true,
                            true));
                }
            }
        } else if (getNewShop().getIngredientItems() != null) {
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < getNewShop().getIngredientItems().size(); i++) {
                if (getNewShop().getIngredientItems().get(i).getStrIngredientName().toLowerCase().equals("chips")) {
                    ingredientItems.add(new IngredientItemCheckbox(getNewShop().getIngredientItems().get(i),
                            true,true, true));
                    dblPrice = getNewShop().getIngredientItems().get(i).getDblPrice();
                }else {
                    ingredientItems.add(new IngredientItemCheckbox(getNewShop().getIngredientItems().get(i),
                            false, true, true));
                }
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mAdapter.setOnItemClickListener(position -> {
            if (!ingredientItems.get(position).getChecked()) {
                ingredientItems.get(position).setChecked(true);
                dblPrice += ingredientItems.get(position).getDblPrice();
                etPrice.setText(String.valueOf(dblPrice));
            } else {
                ingredientItems.get(position).setChecked(false);
                dblPrice -= ingredientItems.get(position).getDblPrice();
                etPrice.setText(String.valueOf(dblPrice));
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void POSTRegisterShopMenuItems() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/shops/Register/MenuItem",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);
                            etPrice.setUnderlineColor(Color.GRAY);
                            etPrice.setText("");
                            startActivity(new Intent(this, MenuActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, getError(error), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mList", combineString(ingredientItems));
                params.put("mPrice", Objects.requireNonNull(etPrice.getText()).toString());
                params.put("sID", String.valueOf(getNewShop().getIntID()));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void add(View view) {
        //validate ingredient name and price price
        if (Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
            etPrice.setError("required");
        } else {
            if ((getIngredients().size() > 0) && menuExists()) {
                //Edit item
                PUTMenuItem();
            } else if (menuExists()) {
                //Add new item
                POSTRegisterShopMenuItems();
            }
        }
    }

    public void back(View view) {
        setIngredients(new ArrayList<>());
        finish();
    }

    @Override
    public void onBackPressed() {
        setIngredients(new ArrayList<>());
        finish();
    }

    private void PUTMenuItem() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/Register/MenuItems/" +
                        getNewShop().getMenuItems().get(getIntPosition()).getIntID(),
                response -> {
                    //Toast.makeText(NewMenuItemActivity.this, response, Toast.LENGTH_LONG).show();
                    ShowLoadingPopup(myDialog, false);
                    getNewShop().getMenuItems().get(getIntPosition()).
                            EditPriceNMenu(Double.valueOf(Objects.requireNonNull(etPrice.getText()).toString()),
                                    combineString(ingredientItems));
                    setIngredients(new ArrayList<>());
                    startActivity(new Intent(this, MenuActivity.class));
                }, error -> {
            Toast.makeText(this, getError(error), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("mList", combineString(ingredientItems));
                params.put("mPrice", String.valueOf(dblPrice));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private boolean menuExists() {
        if (getNewShop().getMenuItems() != null) {
            for (MenuItem menuItem : getNewShop().getMenuItems()) {
                if (combineString(ingredientItems).equals(menuItem.getStrMenu())) {
                    Toast.makeText(this, "Menu item already exists.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
