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
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getIngredientItems;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MenuItem> MenuItems;
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

        MenuItems = new ArrayList<>();
        myDialog = new Dialog(this);
        if (getNewShop().getMenuItems() != null) {
            MenuItems = getNewShop().getMenuItems();
        }
        Ingredients = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(MenuItems);

        tvEmpty = findViewById(R.id.tvEmpty);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if (getNewShop().getMenuItems() == null) {
            GETMenuItems(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        }

        btnNext = findViewById(R.id.btnNext);

        //Set Button Visibility False if no menu item
        ButtonVisibility(MenuItems, btnNext);

        mAdapter.setOnItemClickListener(new MenuItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }

            @Override
            public void onEditClick(int position) {
                intPosition = position;
                String[] IngredientNames = MenuItems.get(position).getStrMenu().split(", ");
                for (String ingredient : IngredientNames) {
                    for (IngredientItem ingredientItem : getIngredientItems())
                        if (ingredientItem.getStrIngredientName().equals(ingredient)) {
                            Ingredients.add(ingredientItem);
                        }
                }
                dblPrice = MenuItems.get(position).getDblPrice();
                startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
            }

            @Override
            public void onDeleteClick(int position) {
                DELETEIngredient(position);
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
        startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
    }

    public void next(View view) {
        startActivity(new Intent(MenuActivity.this, NewExtrasActivity.class));
    }

    public void AddMenu(View view) {
        startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
    }

    private void DELETEIngredient(int position) {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://" + getIpAddress() + "/shops/Register/MenuItem/" + MenuItems.get(position).getIntID()+"/"+getNewShop().getIntID(), null,
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            MenuItems.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            ButtonVisibility(MenuItems, btnNext);
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
        startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
    }

    private void GETMenuItems(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
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
                                ButtonVisibility(MenuItems, btnNext);
                                MenuItems.add(new MenuItem(Ingredients.getInt("mID"),
                                        Ingredients.getDouble("mPrice"), Ingredients.getString("mList"), true));
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
}
