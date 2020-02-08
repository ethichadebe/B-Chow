package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;

import static www.kicknbhoboza.com.emakoteni.MenuActivity.getIngredients;
import static www.kicknbhoboza.com.emakoteni.MenuActivity.getIntPosition;
import static www.kicknbhoboza.com.emakoteni.MenuActivity.setIngredients;
import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.addToList;
import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.getIngredientItems;
import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.EditMenu;

public class NewMenuItemActivity extends AppCompatActivity {

    private static ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout llBack;
    Button btnOder;
    EditText txtPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);
        txtPrice = findViewById(R.id.txtPrice);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder MenuList = new StringBuilder();

                //Group all ingredients into one string
                for (int i = 0; i < ingredientItems.size(); i++) {
                    if (ingredientItems.get(i).getChecked() && (i == ingredientItems.size() - 1)) {
                        MenuList.append(ingredientItems.get(i).getStrIngredientName());
                    } else if (ingredientItems.get(i).getChecked()) {
                        MenuList.append(ingredientItems.get(i).getStrIngredientName()).append(", ");//MenuLiist.replace(ingredientItems.get(i).getStrIngredientName()+",", "");
                    }
                }

                //get price
                if (txtPrice.getText().toString().isEmpty()) {

                    txtPrice.setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                } else {
                    txtPrice.setBackground(getResources().getDrawable(R.drawable.et_bg));
                    if (getIngredients() != null) {
                        //Edit item
                        EditMenu(getIntPosition(), Double.valueOf(txtPrice.getText().toString()), MenuList.toString());
                        setIngredients(null);
                    } else {
                        //Add new item
                        addToList(Double.valueOf(txtPrice.getText().toString()), MenuList.toString());
                    }
                    txtPrice.setText("");
                    startActivity(new Intent(NewMenuItemActivity.this, MenuActivity.class));
                }
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (getIngredients() != null) {
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < getIngredients().length; i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredients()[i], true));
            }

            for (int i = 0; i < getIngredientItems().size(); i++) {
                boolean isThere = false;
                for (int b = 0; b < getIngredients().length; b++) {
                    if (getIngredientItems().get(i).getStrIngredientName().equals(getIngredients()[b])) {
                        isThere = true;
                    }
                }
                if (!isThere){
                    ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), false));
                }
            }

        } else if (getIngredientItems() != null) {
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < getIngredientItems().size(); i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), false));
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mAdapter.setOnItemClickListener(new IngredientItemCheckboxAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (!ingredientItems.get(position).getChecked()) {
                    ingredientItems.get(position).setChecked(true);
                } else {
                    ingredientItems.get(position).setChecked(false);
                }
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}