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

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    LinearLayout llBack;
    Button btnOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ArrayList<MenuItem> MenuItems = new ArrayList<>();

        MenuItems.add(new MenuItem("R13","Chips, French, Eggs", R.drawable.ic_edit_black_24dp, R.drawable.ic_delete_black_24dp) );
        MenuItems.add(new MenuItem("R13","Chips, French, Eggs", R.drawable.ic_edit_black_24dp, R.drawable.ic_delete_black_24dp) );
        MenuItems.add(new MenuItem("R13","Chips, French, Eggs", R.drawable.ic_edit_black_24dp, R.drawable.ic_delete_black_24dp) );
        MenuItems.add(new MenuItem("R13","Chips, French, Eggs", R.drawable.ic_edit_black_24dp, R.drawable.ic_delete_black_24dp) );
        MenuItems.add(new MenuItem("R13","Chips, French, Eggs", R.drawable.ic_edit_black_24dp, R.drawable.ic_delete_black_24dp) );
            mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MenuItemAdapter(MenuItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        llBack = findViewById(R.id.llBack);
        btnOder = findViewById(R.id.btnOder);
        btnOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, HomePageActivity.class));
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
