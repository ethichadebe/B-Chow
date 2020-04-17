package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import SingleItem.ShopItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class IngredientsActivity extends AppCompatActivity {

    private IngredientItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CardView btnAddOption;
    private MaterialEditText etName, etPrice;
    private Dialog myDialog;
    private TextView tvEmpty;
    private RelativeLayout rlLoad, rlError;

    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } // Check if user is looged in

        myDialog = new Dialog(this);
        btnNext = findViewById(R.id.btnNext);
        etName = findViewById(R.id.etName);
        btnAddOption = findViewById(R.id.btnAddOption);
        etPrice = findViewById(R.id.etPrice);

        tvEmpty = findViewById(R.id.tvEmpty);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);

        GETIngredients(findViewById(R.id.vLine),findViewById(R.id.vLineGrey));

        ButtonVisibility(getNewShop().getIngredientItems(), btnNext);

        btnAddOption.setOnClickListener(view -> {
            if (Objects.requireNonNull(etName.getText()).toString().isEmpty() &&
                    Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
                etName.setError("required");
                etPrice.setError("required");
            } else if (etName.getText().toString().isEmpty()) {
                etName.setError("required");
            } else if (Objects.requireNonNull(etPrice.getText()).toString().isEmpty()) {
                etPrice.setError("required");
            } else if (!ingredientExists(etName.getText().toString())){
                POSTRegisterShopIngredients();
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemAdapter(getNewShop().getIngredientItems());

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

    private boolean ingredientExists(String name){
        for (IngredientItem ingredientItem:getNewShop().getIngredientItems()){
            if (ingredientItem.getStrIngredientName().toLowerCase().equals(name.toLowerCase())){
                etName.setError("Already exists");
                return true;
            }
        }

        return false;
    }

    public void back(View view) {
            startActivity(new Intent(this, OperatingHoursActivity.class));
    }

    public void next(View view) {
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
                            getNewShop().getIngredientItems().add(new IngredientItem(JSONResponse.getInt("iID"),
                                    JSONResponse.getString("iName"), JSONResponse.getDouble("iPrice")));
                            mAdapter.notifyItemInserted(getNewShop().getIngredientItems().size());
                            etName.setText("");
                            etPrice.setText("");
                            ButtonVisibility(getNewShop().getIngredientItems(), btnNext);
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
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + getNewShop().getIngredientItems().get(position).getIntID(), null,   //+getUser().getuID()
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            getNewShop().getIngredientItems().remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ButtonVisibility(getNewShop().getIngredientItems(), btnNext);
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
                "http://" + getIpAddress() + "/shops/Register/Ingredient/" + getNewShop().getIngredientItems().get(position).getIntID(),
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    getNewShop().getIngredientItems().get(position).setStrIngredientName(IngredientName);
                    getNewShop().getIngredientItems().get(position).setDblPrice(Double.valueOf(Price));
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

        etName.setText(getNewShop().getIngredientItems().get(position).getStrIngredientName());
        etPrice.setText(String.valueOf(getNewShop().getIngredientItems().get(position).getDblPrice()));
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        btnEditOption.setOnClickListener(view -> {
            if (Objects.requireNonNull(etName.getText()).toString().isEmpty() && Objects.requireNonNull(etPrice.getText()).toString().isEmpty()){
                etPrice.setError("required");
                etPrice.setError("required");
            }
            else if (Objects.requireNonNull(etPrice.getText()).toString().isEmpty()){
                etPrice.setError("required");
            }
            else if (Objects.requireNonNull(etName.getText()).toString().isEmpty()){
                etPrice.setError("required");
            }
            else{
                PUTIngredient(position, Objects.requireNonNull(etName.getText()).toString(), Objects.requireNonNull(etPrice.getText()).toString());
            }
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onBackPressed() {
            startActivity(new Intent(this, OperatingHoursActivity.class));
    }

    private void GETIngredients(View vLine, View vLineGrey) {
        getNewShop().setIngredientItems(new ArrayList<>());
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/Ingredients/"+getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("ingredients");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Ingredients = jsonArray.getJSONObject(i);
                                ButtonVisibility(getNewShop().getIngredientItems(), btnNext);
                                getNewShop().getIngredientItems().add(new IngredientItem(Ingredients.getInt("iID"),
                                        Ingredients.getString("iName"), Ingredients.getDouble("iPrice")));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
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

    public void reload(View view) {
        GETIngredients(findViewById(R.id.vLine),findViewById(R.id.vLineGrey));
    }
}
