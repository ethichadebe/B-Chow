package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import Adapter.ShopItemAdapter;
import SingleItem.ShopItem;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvClear;
    private MaterialEditText txtUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home,container, false);

        final ArrayList<ShopItem> shopItems = new ArrayList<>();

        //Loads shops starting with the one closest to user
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,
                "This is a short description about my shop to attract customers",1,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,
                "This is a short description about my shop to attract customers",3,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,
                "This is a short description about my shop to attract customers",2,
                "2km", "10-15min"));
        shopItems.add(new ShopItem(1,"Shop name",R.drawable.food,
                "This is a short description about my shop to attract customers",5,
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
                if (charSequence.length() != 0){
                    tvClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }
}
