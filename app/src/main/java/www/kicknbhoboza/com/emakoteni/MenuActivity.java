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

import Adapter.MenuItemAdapter;
import SingleItem.MenuItem;

import static www.kicknbhoboza.com.emakoteni.MenuCreationActivity.getMenuItems;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MenuItem> MenuItems;

    LinearLayout llBack, llAddMenu;
    Button btnOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MenuItems = new ArrayList<>();

        MenuItems = getMenuItems();
        /*if (getMenuItems() != null){
            for (int i=0; i< getMenuItems().size(); i++){
                MenuItems.add(new MenuItem(1,getMenuItems().get(i).getDblPrice(),getMenuItems().get(i).getStrMenu(), R.drawable.ic_edit_black_24dp,
                        R.drawable.ic_delete_black_24dp, View.VISIBLE) );
            }
        }*/
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
                startActivity(new Intent(MenuActivity.this, HomePageActivity.class));
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
                finish();
            }
        });
    }

}
