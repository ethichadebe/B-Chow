package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.removeLastComma;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIntPosition;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getDblPrice;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.setIngredients;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.addToList;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getIngredientItems;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.EditMenu;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class NewMenuItemActivity extends AppCompatActivity {

    private static ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Double dblPrice = 0.0;
    private MaterialEditText etPrice;
    private CardView rlTotal;
    private Dialog myDialog;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        btnAdd = findViewById(R.id.btnAdd);
        etPrice = findViewById(R.id.etPrice);
        rlTotal = findViewById(R.id.rlTotal);
        myDialog = new Dialog(this);

        if (getIngredients() != null) {
            ingredientItems = new ArrayList<>();
            etPrice.setText(String.valueOf(getDblPrice()));
            for (int i = 0; i < getIngredients().length; i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredients()[i], 12.5, true, true));
            }

            for (int i = 0; i < getIngredientItems().size(); i++) {
                boolean isThere = false;
                for (int b = 0; b < getIngredients().length; b++) {
                    if (getIngredientItems().get(i).getStrIngredientName().equals(getIngredients()[b])) {
                        isThere = true;
                        break;
                    }
                }
                if (!isThere) {
                    ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), getIngredientItems().get(i).getDblPrice(), false, true));
                }
            }

        } else if (getIngredientItems() != null) {
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < getIngredientItems().size(); i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), getIngredientItems().get(i).getDblPrice(), false, true));
            }
        }

        isChecked();
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

            isChecked();
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void isChecked() {
        boolean isChecked = false;           //Checks if there's at least one Ingredients checkbox selected
        for (IngredientItemCheckbox item : ingredientItems) {
            if (item.getChecked()) {
                isChecked = true;
                break;
            }
        }

        if (isChecked) {
            btnAdd.setVisibility(View.VISIBLE);
            rlTotal.setVisibility(View.VISIBLE);
        } else {
            btnAdd.setVisibility(View.GONE);
            rlTotal.setVisibility(View.GONE);
        }
    }

    private void POSTRegisterShopMenuItems(String MenuLis) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/shops/Register/MenuItem",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        if (JSONResponse.getString("data").equals("saved")) {
                            etPrice.setUnderlineColor(getResources().getColor(R.color.Black));
                            if (getIngredients() != null) {
                                //Edit item
                                EditMenu(getIntPosition(), Double.valueOf(Objects.requireNonNull(etPrice.getText()).toString()), MenuLis);
                                setIngredients(null);
                            } else {
                                //Add new item
                                addToList(Double.valueOf(Objects.requireNonNull(etPrice.getText()).toString()), MenuLis);
                            }
                            etPrice.setText("");
                            startActivity(new Intent(NewMenuItemActivity.this, MenuActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(NewMenuItemActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mList", MenuLis);
                params.put("mPrice", Objects.requireNonNull(etPrice.getText()).toString());
                params.put("sID", String.valueOf(getNewShop().getIntID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void add(View view) {
        StringBuilder MenuList = new StringBuilder();

        //Group all ingredients into one string
        for (int i = 0; i < ingredientItems.size(); i++) {
            if (ingredientItems.get(i).getChecked())
                MenuList.append(ingredientItems.get(i).getStrIngredientName()).append(", ");
        }

        //validate ingredient name and price price
        if (Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
            etPrice.setUnderlineColor(getResources().getColor(R.color.Red));
        } else {
            POSTRegisterShopMenuItems(removeLastComma(String.valueOf(MenuList)));
        }
    }

    public void back(View view) {
        finish();
    }
}
