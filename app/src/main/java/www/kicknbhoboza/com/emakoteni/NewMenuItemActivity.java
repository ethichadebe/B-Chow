package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItem;
import SingleItem.IngredientItemCheckbox;
import SingleItem.MenuItem;

import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.addToList;
import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.getIngredientItems;

public class NewMenuItemActivity extends AppCompatActivity {

    private ArrayList<IngredientItemCheckbox> ingredientItems;
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
                StringBuilder MenuLiist = new StringBuilder();

                for (int i = 0; i < ingredientItems.size(); i++) {
                    if (ingredientItems.get(i).getChecked() && (i == ingredientItems.size() - 1)) {
                        MenuLiist.append(ingredientItems.get(i).getStrIngredientName());//MenuLiist.replace(ingredientItems.get(i).getStrIngredientName()+",", "");
                    } else if (ingredientItems.get(i).getChecked()) {
                        MenuLiist.append(ingredientItems.get(i).getStrIngredientName()).append(", ");//MenuLiist.replace(ingredientItems.get(i).getStrIngredientName()+",", "");
                    }
                }

                addToList(Double.valueOf(txtPrice.getText().toString()), MenuLiist.toString());
                startActivity(new Intent(NewMenuItemActivity.this, MenuActivity.class));
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ingredientItems = new ArrayList<>();

        if (getIngredientItems() != null) {
            for (int i = 0; i < getIngredientItems().size(); i++) {
                ingredientItems.add(new IngredientItemCheckbox(1, getIngredientItems().get(i).getStrIngredientName()));
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new IngredientItemCheckboxAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (ingredientItems.get(position).getChecked()) {
                    ingredientItems.get(position).setChecked(false);
                } else {
                    ingredientItems.get(position).setChecked(true);
                }
            }
        });
    }


}
