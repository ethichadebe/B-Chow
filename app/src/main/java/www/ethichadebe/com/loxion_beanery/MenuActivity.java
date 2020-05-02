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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.MenuItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<IngredientItem> Ingredients;
    private static int intPosition;
    private static Double dblPrice;
    private TextView tvEmpty;
    private Dialog myDialog;
    private Button btnNext;
    private RelativeLayout rlLoad, rlError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } // Check if user is looged in
        myDialog = new Dialog(this);

        tvEmpty = findViewById(R.id.tvEmpty);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        btnNext = findViewById(R.id.btnNext);

        GETMenuItems(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));

        Ingredients = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(getNewShop().getMenuItems());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if(isEdit){
            btnNext.setText("Edit Extras");
        }
        //Set Button Visibility False if no menu item
        ButtonVisibility(getNewShop().getMenuItems(), btnNext);

        mAdapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onEditClick(int position) {
                intPosition = position;
                String[] IngredientNames = getNewShop().getMenuItems().get(position).getStrMenu().split(", ");
                for (String ingredient : IngredientNames) {
                    for (IngredientItem ingredientItem : getNewShop().getIngredientItems())
                        if (ingredientItem.getStrIngredientName().equals(ingredient)) {
                            Ingredients.add(ingredientItem);
                        }
                }
                dblPrice = getNewShop().getMenuItems().get(position).getDblPrice();
                startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
            }

            @Override
            public void onDeleteClick(int position) {
                ShowPopup(position);
            }
        });

    }

    public static int getIntPosition() {
        return intPosition;
    }

    public static Double getDblPrice() {
        return dblPrice;
    }

    public void back(View view) {
        if (isEdit){
            finish();
        }else {
            startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
        }
    }

    public void next(View view) {
        startActivity(new Intent(MenuActivity.this, NewExtrasActivity.class));
    }

    public void AddMenu(View view) {
        startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
    }

    private void DELETEIngredient(int position) {
        ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://" + getIpAddress() + "/shops/Register/MenuItem/" +
                        getNewShop().getMenuItems().get(position).getIntID() + "/" + getNewShop().getIntID(), null,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            getNewShop().getMenuItems().remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ButtonVisibility(getNewShop().getMenuItems(), btnNext);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    public static ArrayList<IngredientItem> getIngredients() {
        return Ingredients;
    }

    public static void setIngredients(ArrayList<IngredientItem> ingredients) {
        Ingredients = ingredients;
    }

    @Override
    public void onBackPressed() {
        if (isEdit){
            finish();
        }else {
            startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
        }
    }

    private void GETMenuItems(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        getNewShop().setMenuItems(new ArrayList<>());
        handler(vLine, vLineGrey);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/MenuItems/" + getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("menuItems");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Ingredients = jsonArray.getJSONObject(i);
                                ButtonVisibility(getNewShop().getMenuItems(), btnNext);
                                getNewShop().getMenuItems().add(new MenuItem(Ingredients.getInt("mID"),
                                        Ingredients.getDouble("mPrice"), Ingredients.getString("mList"), true));
                            }
                            getNewShop().setMenuItems(getNewShop().getMenuItems());

                            if (getNewShop().getIngredientItems() == null) {
                                GETIngredients();
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
        GETMenuItems(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
    }

    public void ShowPopup(int position) {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvMessage.setText("Are you sure?");
        cvYes.setOnClickListener(view -> {
            DELETEIngredient(position);
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void GETIngredients() {
        getNewShop().setIngredientItems(new ArrayList<>());
        ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops/Ingredients/" + getNewShop().getIntID(), null,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    ShowLoadingPopup(myDialog, false);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }

}
