package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.IngredientItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.MenuItem;
import SingleItem.ShopItem;

public class MenuCreationActivity extends AppCompatActivity {

    private static ArrayList<IngredientItem> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<MenuItem> MenuItems;
    private CardView btnAddOption;
    private EditText etName, etPrice;
    private Dialog myDialog;

    LinearLayout llBack;
    Button btnOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        myDialog = new Dialog(this);
        ingredientItems = new ArrayList<>();
        MenuItems= new ArrayList<>();
        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);
        etName = findViewById(R.id.etName);
        btnAddOption = findViewById(R.id.btnAddOption);
        etPrice = findViewById(R.id.etPrice);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredientItems.isEmpty()){
                    ShowNotificationPopup();
                }else {
                    startActivity(new Intent(MenuCreationActivity.this, MenuActivity.class));
                }
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ingredientItems.isEmpty()){
                    ShowConfirmationPopup();
                }else {
                    finish();
                }
            }
        });
        btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().isEmpty() && etPrice.getText().toString().isEmpty()) {
                    etName.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                } else if (etName.getText().toString().isEmpty()) {
                    etName.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_bg));
                } else if (etPrice.getText().toString().isEmpty()) {
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    etName.setBackground(getResources().getDrawable(R.drawable.et_bg));
                } else {
                    etName.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    etPrice.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    ingredientItems.add(new IngredientItem(1, etName.getText().toString(), Double.valueOf(etPrice.getText().toString())));
                    mAdapter.notifyItemInserted(ingredientItems.size());
                    etName.setText("");
                    etPrice.setText("");
                }
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
                ingredientItems.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

    }

    public static ArrayList<IngredientItem> getIngredientItems() {
        return ingredientItems;
    }

    public static ArrayList<MenuItem> getMenuItems() {
        return MenuItems;
    }

    public static void addToList(Double Price, String ingredients) {
        MenuItems.add(new MenuItem(1, Price, ingredients, R.drawable.ic_edit_black_24dp,
                R.drawable.ic_delete_black_24dp, View.VISIBLE));
    }
    public static void EditMenu(int position, Double Price, String ingredients) {
        MenuItems.get(position).EditPriceNMenu(Price, ingredients);
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

        tvMessage.setText("All entered ingredients will be lost\nAre you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                finish();
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

        tvMessage.setText("You need to add at least one ingredient to continue");

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
