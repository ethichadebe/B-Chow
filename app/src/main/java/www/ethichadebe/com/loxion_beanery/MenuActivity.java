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

import java.util.ArrayList;

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;

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
    private CardView llAddMenu, llBack;
    private static boolean  isNew = false;

    public static boolean isNew() {
        return isNew;
    }

    public static void setIsNew(boolean isNew) {
        MenuActivity.isNew = isNew;
    }

    Button btnFinish;

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

        llBack = findViewById(R.id.llBack);
        btnFinish = findViewById(R.id.btnFinish);
        llAddMenu = findViewById(R.id.llAddMenu);

        //Set Button Visibility False if no menu item
        finishButtonVisibility();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewShop().setMenuItems(getMenuItems());
                isNew = true;
                startActivity(new Intent(MenuActivity.this, MyShopsActivity.class));
            }
        });

        llAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, NewMenuItemActivity.class));
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MenuItems.isEmpty()){
                    finish();
                }else {
                    ShowConfirmationPopup();
                }
            }
        });

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
                finishButtonVisibility();
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

    public void ShowConfirmationPopup(){
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("All entered ingredients and Menu items will be lost.\nAre you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                startActivity(new Intent(MenuActivity.this, IngredientsActivity.class));
            }
        });

        cvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * Only display finish button when there's a menu item added
     */
    private void finishButtonVisibility(){
        if (MenuItems.isEmpty()){
            btnFinish.setVisibility(View.GONE);
        }else {
            btnFinish.setVisibility(View.VISIBLE);
        }
    }
}
