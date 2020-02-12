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

import static www.ethichadebe.com.loxion_beanery.MenuCreationActivity.getMenuItems;

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

    Button btnOder;

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
        btnOder = findViewById(R.id.btnOder);
        llAddMenu = findViewById(R.id.llAddMenu);
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MenuItems.isEmpty()){
                    ShowNotificationPopup();
                }else {
                    startActivity(new Intent(MenuActivity.this, HomePageActivity.class));
                }
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
        myDialog.setContentView(R.layout.confirmation_popup);

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
                startActivity(new Intent(MenuActivity.this, MenuCreationActivity.class));
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

    public void ShowNotificationPopup(){
        TextView tvCancel, tvMessage;
        CardView cvOkay;
        myDialog.setContentView(R.layout.notification_popup);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvOkay = myDialog.findViewById(R.id.cvOkay);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("You need to add at least one menu item to continue");

        cvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}
