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

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;

import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.getIngredientItems;

public class OrderActivity extends AppCompatActivity {
    private  ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private LinearLayout llBack;
    private Button btnOder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ingredientItems = new ArrayList<>();

        if (getIngredientItems() != null){
            for (int i=0; i< getIngredientItems().size(); i++){
                ingredientItems.add(new IngredientItemCheckbox(1,getIngredientItems().get(i).getStrIngredientName()));
            }
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}
