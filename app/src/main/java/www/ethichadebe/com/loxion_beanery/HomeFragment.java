package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapter.ShopItemAdapter;
import SingleItem.PastOrderItem;
import SingleItem.ShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.LoaderMotion;
import static util.HelperMethods.handler;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvClear,tvEmpty;
    private MaterialEditText tvSearch;
    private CardView cvRetry;
    final ArrayList<ShopItem> shopItems = new ArrayList<>();

    private RelativeLayout rlLoad, rlError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);


        mRecyclerView = v.findViewById(R.id.recyclerView);
        tvClear = v.findViewById(R.id.tvClear);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvSearch = v.findViewById(R.id.tvSearch);
        cvRetry = v.findViewById(R.id.cvRetry);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ShopItemAdapter(shopItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(position -> {
            /*shopItems.get(position)*/
            startActivity(new Intent(getActivity(), ShopHomeActivity.class));
        });

        tvClear.setVisibility(View.GONE);
        tvClear.setOnClickListener(view -> {
            tvSearch.setText("");
            tvClear.setVisibility(View.GONE);
        });

        cvRetry.setOnClickListener(view -> {
            rlError.setVisibility(View.GONE);
            handler(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
            rlLoad.setVisibility(View.VISIBLE);
            GETPassedIMeetings();
        });

        tvSearch.addTextChangedListener(new TextWatcher() {
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
        handler(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
        GETPassedIMeetings();
        return v;
    }

    private void GETPassedIMeetings() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/users", null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        JSONArray jsonArray = response.getJSONArray("users");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject User = jsonArray.getJSONObject(i);
                            shopItems.add(new ShopItem(1, User.getString("uName"), R.drawable.food,
                                    "This is a short description about my shop to attract customers", 1,
                                    "2km", "10-15min"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    rlError.setVisibility(View.VISIBLE);
                    rlLoad.setVisibility(View.GONE);
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);

    }
}
