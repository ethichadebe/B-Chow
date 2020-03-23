package www.ethichadebe.com.loxion_beanery;

import android.content.Intent;
import android.location.Location;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

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
    private TextView tvEmpty, tvSearch;
    private MaterialEditText etSearch;
    private CardView cvRetry;
    final ArrayList<ShopItem> shopItems = new ArrayList<>();

    private RelativeLayout rlLoad, rlError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_home, container, false);


        mRecyclerView = v.findViewById(R.id.recyclerView);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        tvSearch = v.findViewById(R.id.tvSearch);
        etSearch = v.findViewById(R.id.etSearch);
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

        cvRetry.setOnClickListener(view -> {
            rlError.setVisibility(View.GONE);
            handler(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
            rlLoad.setVisibility(View.VISIBLE);
            GETShops();
        });

        tvSearch.setOnClickListener(view -> {
            if (etSearch.getVisibility() == View.GONE) {
                etSearch.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.SlideInRight)
                        .duration(1000)
                        .repeat(0)
                        .playOn(etSearch);
            }
        });

        handler(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
        GETShops();
        return v;
    }

    private void GETShops() {
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/shops", null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        JSONArray jsonArray = response.getJSONArray("shops");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Shops = jsonArray.getJSONObject(i);
                            Location location = new Location("");
                            String[] strCoord = Shops.getString("sLocation").split(" ");
                            location.setLatitude(23.4);//Double.parseDouble(strCoord[0]));
                            location.setLongitude(32.5);//Double.parseDouble(strCoord[1]));
                            shopItems.add(new ShopItem(Shops.getInt("sID"), Shops.getString("sName"), R.drawable.food,
                                    R.drawable.biglogo, Shops.getString("sShortDescrption"),
                                    Shops.getString("sFullDescription"), location, Shops.getString("sLocation"),
                                    Shops.getInt("sRating"), Shops.getString("sOperatingHrs")));
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
