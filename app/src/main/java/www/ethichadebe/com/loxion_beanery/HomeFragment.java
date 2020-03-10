package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import Adapter.ShopItemAdapter;
import SingleItem.PastOrderItem;
import SingleItem.ShopItem;

import static util.HelperMethods.LoaderMotion;

public class HomeFragment extends Fragment {
    private Handler handler = new Handler();
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvClear;
    private MaterialEditText txtUsername;
    private static ArrayList<PastOrderItem> pastOrderItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);

        final ArrayList<ShopItem> shopItems = new ArrayList<>();

        //Loads shops starting with the one closest to user
        shopItems.add(new ShopItem(1, "Shop name", R.drawable.food,
                "This is a short description about my shop to attract customers", 1,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1, "Shop name", R.drawable.food,
                "This is a short description about my shop to attract customers", 3,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1, "Shop name", R.drawable.food,
                "This is a short description about my shop to attract customers", 2,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1, "Shop name", R.drawable.food,
                "This is a short description about my shop to attract customers", 5,
                "2km", "10-15min"));
        mRecyclerView = v.findViewById(R.id.recyclerView);
        tvClear = v.findViewById(R.id.tvClear);
        txtUsername = v.findViewById(R.id.txtUsername);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ShopItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*shopItems.get(position)*/
                startActivity(new Intent(getActivity(), ShopHomeActivity.class));
            }
        });

        tvClear.setVisibility(View.GONE);
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUsername.setText("");
                tvClear.setVisibility(View.GONE);
            }
        });

        txtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    tvClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        handler.postDelayed(LoaderMotion(v.findViewById(R.id.vLineGrey)), 0);
        handler.postDelayed(LoaderMotion(v.findViewById(R.id.vLine)), 500);

        return v;
    }

    static void DisplayPastOrders() {
        //Loads past orders
        pastOrderItems.add(new PastOrderItem(1, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 3));
        pastOrderItems.add(new PastOrderItem(3, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 4));
        pastOrderItems.add(new PastOrderItem(5, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 1));
        pastOrderItems.add(new PastOrderItem(4, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, 5));
        pastOrderItems.add(new PastOrderItem(2, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, -1));
        pastOrderItems.add(new PastOrderItem(-1, "Shop name", 315,
                "13 Jan 2020,15:45", "French, bacon, egg, russian", 19.50, -1));
    }

    static ArrayList<PastOrderItem> getPastOrderItems() {
        return pastOrderItems;
    }

    static PastOrderItem getPastOrderItem(int position) {
        return pastOrderItems.get(position);
    }
}
