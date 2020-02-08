package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    LinearLayout llBack;
    Button btnOder, btnAddOption;
    EditText txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        ingredientItems = new ArrayList<>();
        MenuItems= new ArrayList<>();
        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);
        txtName = findViewById(R.id.txtName);
        btnAddOption = findViewById(R.id.btnAddOption);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuCreationActivity.this, MenuActivity.class));
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtName.getText().toString().isEmpty()) {

                    txtName.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                } else {
                    txtName.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    ingredientItems.add(new IngredientItem(1, txtName.getText().toString()));
                    mAdapter.notifyItemInserted(ingredientItems.size());
                    txtName.setText("");
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
                mAdapter.notifyItemRemoved(ingredientItems.size());
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
}
