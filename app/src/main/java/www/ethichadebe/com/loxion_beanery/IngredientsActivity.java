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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
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

import Adapter.IngredientItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class IngredientsActivity extends AppCompatActivity {

    private static ArrayList<IngredientItem> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private CardView btnAddOption;
    private MaterialEditText etName, etPrice;
    private Dialog myDialog;

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } // Check if user is looged in

        myDialog = new Dialog(this);
        ingredientItems = new ArrayList<>();
        MenuItems = new ArrayList<>();
        btnNext = findViewById(R.id.btnNext);
        etName = findViewById(R.id.etName);
        btnAddOption = findViewById(R.id.btnAddOption);
        etPrice = findViewById(R.id.etPrice);

        ButtonVisibility(ingredientItems, btnNext);
        btnAddOption.setOnClickListener(view -> {
            if (Objects.requireNonNull(etName.getText()).toString().isEmpty() &&
                    Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
                etName.setUnderlineColor(getResources().getColor(R.color.Red));
                etPrice.setUnderlineColor(getResources().getColor(R.color.Red));
            } else if (etName.getText().toString().isEmpty()) {
                etName.setUnderlineColor(getResources().getColor(R.color.Red));
                etPrice.setUnderlineColor(getResources().getColor(R.color.Black));
            } else if (Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
                etName.setUnderlineColor(getResources().getColor(R.color.Black));
                etPrice.setUnderlineColor(getResources().getColor(R.color.Red));
            } else {
                POSTRegisterShopIngredients();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnIngredientClickListener(new IngredientItemAdapter.OnIngredientClickListener() {
            @Override
            public void onRemoveClick(int position) {
                DELETEIngredient(position);
            }

            @Override
            public void onEditClick(int position) {
                ShowEditIngredientPopup(position);
            }
        });
    }

    public static ArrayList<IngredientItem> getIngredientItems() {
        return ingredientItems;
    }

    public static ArrayList<MenuItem> getMenuItems() {
        return MenuItems;
    }

    public static void addToList(int ID, Double Price, String ingredients) {
        MenuItems.add(new MenuItem(ID, Price, ingredients));
    }

    public static void EditMenu(int position, Double Price, String ingredients) {
        MenuItems.get(position).EditPriceNMenu(Price, ingredients);
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

        tvMessage.setText("All entered ingredients will be lost\nAre you sure?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            finish();
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void back(View view) {
        if (!ingredientItems.isEmpty()) {
            ShowConfirmationPopup();
        } else {
            startActivity(new Intent(this, RegisterShopActivity.class));
        }
    }

    public void next(View view) {
        getNewShop().setIngredientItems(ingredientItems);
        startActivity(new Intent(IngredientsActivity.this, MenuActivity.class));
    }

    private void POSTRegisterShopIngredients() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/shops/Register/Ingredient",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);
                            etName.setUnderlineColor(getResources().getColor(R.color.Black));
                            etPrice.setUnderlineColor(getResources().getColor(R.color.Black));
                            ingredientItems.add(new IngredientItem(JSONResponse.getInt("iID"),
                                    JSONResponse.getString("iName"), JSONResponse.getDouble("iPrice")));
                            mAdapter.notifyItemInserted(ingredientItems.size());
                            etName.setText("");
                            etPrice.setText("");
                            ButtonVisibility(ingredientItems, btnNext);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(IngredientsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("iName", Objects.requireNonNull(etName.getText()).toString());
                params.put("iPrice", Objects.requireNonNull(etPrice.getText()).toString());
                params.put("sID", String.valueOf(getNewShop().getIntID()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void DELETEIngredient(int position) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + ingredientItems.get(position).getIntID(), null,   //+getUser().getuID()
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            ingredientItems.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ButtonVisibility(ingredientItems, btnNext);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Loads shops starting with the one closest to user
                },
                error -> {
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

    private void PUTIngredient(int position, String IngredientName, String Price) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + ingredientItems.get(position).getIntID(),
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    ingredientItems.get(position).setStrIngredientName(IngredientName);
                    ingredientItems.get(position).setDblPrice(Double.valueOf(Price));
                    mAdapter.notifyItemChanged(position);
                }, error -> {
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("iName", IngredientName);
                params.put("iPrice", Price);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ShowEditIngredientPopup(int position) {
        MaterialEditText etName, etPrice;
        CardView btnEditOption;
        TextView tvCancel;
        myDialog.setContentView(R.layout.popup_ingredient_edit);

        etName = myDialog.findViewById(R.id.etName);
        etPrice = myDialog.findViewById(R.id.etPrice);
        btnEditOption = myDialog.findViewById(R.id.btnEditOption);
        tvCancel = myDialog.findViewById(R.id.tvCancel);

        etName.setText(ingredientItems.get(position).getStrIngredientName());
        etPrice.setText(String.valueOf(ingredientItems.get(position).getDblPrice()));
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        btnEditOption.setOnClickListener(view -> {
            PUTIngredient(position, Objects.requireNonNull(etName.getText()).toString(),
                    Objects.requireNonNull(etPrice.getText()).toString());
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

}
