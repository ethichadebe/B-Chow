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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.MenuItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getIngredientItems;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getMenuItems;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MenuItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MenuItem> MenuItems;
    private static String[] Ingredients = null;
    private static int intPosition;
    private static Double dblPrice;
    private Dialog myDialog;
    private Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MenuItems = new ArrayList<>();
        myDialog = new Dialog(this);
        MenuItems = getMenuItems();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(MenuItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
                Ingredients = MenuItems.get(position).getStrMenu().split(", ");
                dblPrice = MenuItems.get(position).getDblPrice();
                startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
            }

            @Override
            public void onDeleteClick(int position) {
                MenuItems.remove(position);
                mAdapter.notifyItemRemoved(position);
                ButtonVisibility(MenuItems, btnNext);
            }
        });

    }

    public static String[] getIngredients() {
        return Ingredients;
    }

    public static void setIngredients(String[] ingredients) {
        Ingredients = ingredients;
    }

    public static int getIntPosition() {
        return intPosition;
    }

    public static Double getDblPrice() {
        return dblPrice;
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

        tvMessage.setText("All entered ingredients and Menu items will be lost.\nAre you sure?");

        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
        });

        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private int CheckMenuPrices() {
        ArrayList<Double> results = new ArrayList<>();
        ArrayList<MenuItem> menuItems = getMenuItems();
        ArrayList<IngredientItem> ingredientItems = getIngredientItems();

        if (menuItems.size() > 1) {
            //take each menu item's ingredients list and separate them into individual ingredient names
            for (MenuItem menuItem : menuItems) {
                String[] ingredient = menuItem.getStrMenu().split(", ");

                int counter = 0;
                Double TempPrice = menuItem.getDblPrice();
                while (counter < ingredient.length) {
                    //Go through each ingredient and deduct its price from the menu's total price
                    for (IngredientItem ingredientItem : ingredientItems) {
                        if (ingredient[counter].equals(ingredientItem.getStrIngredientName())) {
                            TempPrice -= ingredientItem.getDblPrice();
                        }
                    }
                    counter++;
                }
                results.add(TempPrice);
            }

            //Go through all resulting prices and check if they are equal
            for (int i = 0; i < results.size() - 1; i++) {
                if (!results.get(i).equals(results.get(i + 1))) {
                    return 0;
                }
            }
        }
        return 1;
    }

    public void back(View view) {
        if (MenuItems.isEmpty()) {
            finish();
        } else {
            ShowConfirmationPopup();
        }
    }

    public void next(View view) {
        startActivity(new Intent(MenuActivity.this, NewExtrasActivity.class));
    }

    public void AddMenu(View view) {
        startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
    }
}
