package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.ExtraItemAdapter;
import SingleItem.ExtraItem;

import static www.ethichadebe.com.loxion_beanery.IngredientsActivity.getMenuItems;
import static www.ethichadebe.com.loxion_beanery.RegisterShopActivity.getNewShop;

public class NewExtrasActivity extends AppCompatActivity {
    private static ArrayList<ExtraItem> extraItems;
    private RecyclerView mRecyclerView;
    private ExtraItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static boolean isNew = false;
    public static boolean isNew() {
        return isNew;
    }

    private MaterialEditText etExtra;

    public static void setIsNew(boolean isNew) {
        NewExtrasActivity.isNew = isNew;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_extras);

        etExtra = findViewById(R.id.etExtra);
        extraItems = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ExtraItemAdapter(extraItems);
        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter.setOnExtraClickListener(position -> {
            extraItems.remove(position);
            mAdapter.notifyItemRemoved(position);
        });
    }

    public void next(View view) {
        getNewShop().setMenuItems(getMenuItems());
        isNew = true;
        startActivity(new Intent(NewExtrasActivity.this, MyShopsActivity.class));
    }

    public void back(View view) {
        finish();
    }

    public void add(View view) {
        if (Objects.requireNonNull(etExtra.getText()).toString().isEmpty()){
            etExtra.setUnderlineColor(getResources().getColor(R.color.Red));
        }else {
            etExtra.setUnderlineColor(getResources().getColor(R.color.Grey));
            extraItems.add(new ExtraItem(1,etExtra.getText().toString()));
            mAdapter.notifyItemInserted(extraItems.size());
            etExtra.setText("");
        }
    }
}
