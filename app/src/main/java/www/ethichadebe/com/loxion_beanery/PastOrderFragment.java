package www.ethichadebe.com.loxion_beanery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemPastAdapter;
import SingleItem.AdminOrderItemPast;

import static util.Constants.getIpAddress;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.MyShopsActivity.getNewShop;

public class PastOrderFragment extends Fragment {
    private static final String TAG = "PastOrderFragment";
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private AdminOrderItemPastAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminOrderItemPast> OrderItems;

    private TextView tvEmpty;
    private RelativeLayout rlLoad, rlError;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_order, container, false);

        tvEmpty = v.findViewById(R.id.tvEmpty);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        OrderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdminOrderItemPastAdapter(OrderItems);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        GETOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));


        return v;

    }

    private void GETOrders(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/orders/AdminPastOrders/" + getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                OrderItems.add(new AdminOrderItemPast(Orders.getInt("oID"), Orders.getInt("oID"),
                                        Orders.getString("oRecievedAt"), Orders.getString("oIngredients"),
                                        Orders.getString("oExtras"), Orders.getInt("oRating"),
                                        Orders.getString("oFeedback"), Orders.getDouble("oPrice")));
                            }
                        } else if (response.getString("message").equals("empty")) {
                            tvEmpty.setVisibility(View.VISIBLE);
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
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
