package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;

import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIngredients;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getIntPosition;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.getDblPrice;
import static www.ethichadebe.com.loxion_beanery.MenuActivity.setIngredients;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.addToList;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getIngredientItems;
import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.EditMenu;

public class NewMenuItemActivity extends AppCompatActivity {

    private static ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Double dblPrice = 0.0;
    private MaterialEditText etPrice;
    private Button btnAdd;
    private CardView rlTotal;

    LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        btnAdd = findViewById(R.id.btnAdd);
        llBack = findViewById(R.id.llBack);
        etPrice = findViewById(R.id.etPrice);
        rlTotal = findViewById(R.id.rlTotal);

        btnAdd.setOnClickListener(new View.OnClickListener() {
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

                //validate ingredient name and price price
                if (etPrice.getText().toString().isEmpty()) {
                    etPrice.setUnderlineColor(getResources().getColor(R.color.Red));
                } else {
                    etPrice.setUnderlineColor(getResources().getColor(R.color.Black));
                    if (getIngredients() != null) {
                        //Edit item
                        EditMenu(getIntPosition(), Double.valueOf(etPrice.getText().toString()), MenuList.toString());
                        setIngredients(null);
                    } else {
                        //Add new item
                        addToList(Double.valueOf(etPrice.getText().toString()), MenuList.toString());
                    }
                    etPrice.setText("");
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
            etPrice.setText(String.valueOf(getDblPrice()));
            for (int i = 0; i < getIngredients().length; i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredients()[i], 12.5, true, true));
            }

            for (int i = 0; i < getIngredientItems().size(); i++) {
                boolean isThere = false;
                for (int b = 0; b < getIngredients().length; b++) {
                    if (getIngredientItems().get(i).getStrIngredientName().equals(getIngredients()[b])) {
                        isThere = true;
                    }
                }
                if (!isThere) {
                    ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), getIngredientItems().get(i).getDblPrice(), false, true));
                }
            }

        } else if (getIngredientItems() != null) {
            ingredientItems = new ArrayList<>();
            for (int i = 0; i < getIngredientItems().size(); i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName(), getIngredientItems().get(i).getDblPrice(), false, true));
            }
        }

        isChecked();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mAdapter.setOnItemClickListener(new IngredientItemCheckboxAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (!ingredientItems.get(position).getChecked()) {
                    ingredientItems.get(position).setChecked(true);
                    dblPrice += ingredientItems.get(position).getDblPrice();
                    etPrice.setText(String.valueOf(dblPrice));
                } else {
                    ingredientItems.get(position).setChecked(false);
                    dblPrice -= ingredientItems.get(position).getDblPrice();
                    etPrice.setText(String.valueOf(dblPrice));
                }

                isChecked();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void isChecked(){
        boolean isChecked = false;           //Checks if there's at least one Ingredients checkbox selected
        for(IngredientItemCheckbox item : ingredientItems){
            if (item.getChecked()){
                isChecked = true;
            }
        }

        if (isChecked){
            btnAdd.setVisibility(View.VISIBLE);
            rlTotal.setVisibility(View.VISIBLE);
        }else {
            btnAdd.setVisibility(View.GONE);
            rlTotal.setVisibility(View.GONE);
        }
    }


}
