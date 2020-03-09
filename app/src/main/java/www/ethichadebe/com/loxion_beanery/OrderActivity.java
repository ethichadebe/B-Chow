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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import Adapter.IngredientItemCheckboxAdapter;
import SingleItem.IngredientItemCheckbox;

import static www.ethichadebe.com.loxion_beanery.ShopHomeActivity.getIngredients;

public class OrderActivity extends AppCompatActivity {
    private ArrayList<IngredientItemCheckbox> ingredientItems;
    private RecyclerView mRecyclerView;
    private IngredientItemCheckboxAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private Double dblPrice = 0.0;
    private LinearLayout llBack;
    private Button btnOder;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        myDialog = new Dialog(this);
        btnOder = findViewById(R.id.btnOder);
        llBack = findViewById(R.id.llBack);
        tvTotal = findViewById(R.id.tvTotal);

        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = false;
                for (int i = 0; i < ingredientItems.size(); i++){
                    if (ingredientItems.get(i).getChecked()){
                        isChecked = true;
                    }
                }

                if (isChecked){
                    startActivity(new Intent(OrderActivity.this, OrderConfirmationActivity.class));
                }else {
                    //ShowNotificationPopup();
                }

            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ingredientItems = new ArrayList<>();

        //Display menu
        for (int i = 0; i < getIngredients().length; i++) {
            ingredientItems.add(new IngredientItemCheckbox(1, getIngredients()[i], 12.5, true, false));
        }

        //Add initial total price
        for (int i = 0; i < ingredientItems.size(); i++) {
            if (ingredientItems.get(i).getChecked()){
                dblPrice += ingredientItems.get(i).getDblPrice();
            };
        }
        tvTotal.setText("R" + dblPrice);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngredientItemCheckboxAdapter(ingredientItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new IngredientItemCheckboxAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                if (!ingredientItems.get(position).getChecked()) {
                    ingredientItems.get(position).setChecked(true);
                    dblPrice += ingredientItems.get(position).getDblPrice();
                } else {
                    ingredientItems.get(position).setChecked(false);
                    dblPrice -= ingredientItems.get(position).getDblPrice();
                }
                tvTotal.setText("R" + dblPrice);
            }
        });

    }

}
