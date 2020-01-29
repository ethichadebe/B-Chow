package www.kicknbhoboza.com.emakoteni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import Adapter.IngredientItemAdapter;
import Adapter.ShopItemAdapter;
import SingleItem.IngredientItem;
import SingleItem.ShopItem;

public class MenuCreationActivity extends AppCompatActivity {

    private  ArrayList<IngredientItem> ingredientItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout llBack, llAddOption;
    Button btnOder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_creation);

        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);
        llAddOption = findViewById(R.id.llAddOption);

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
        llAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       ingredientItems = new ArrayList<>();

        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        ingredientItems.add(new IngredientItem("French"));
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
