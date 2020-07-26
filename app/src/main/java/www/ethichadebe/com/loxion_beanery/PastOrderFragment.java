package www.ethichadebe.com.loxion_beanery;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemPastAdapter;
import SingleItem.AdminOrderItemPast;

import static util.Constants.getIpAddress;
import static util.HelperMethods.getError;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.OrdersActivity.oID;

public class PastOrderFragment extends Fragment {
    private static final String TAG = "PastOrderFragment";
    private RequestQueue requestQueue;
    private RecyclerView mRecyclerView;
    private AdminOrderItemPastAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminOrderItemPast> orderItems;
    private CardView cvRetry;
    private TextView tvEmpty, tvError;
    private RelativeLayout rlLoad, rlError;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_past_order, container, false);

        tvEmpty = v.findViewById(R.id.tvEmpty);
        cvRetry = v.findViewById(R.id.cvRetry);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        tvError = v.findViewById(R.id.tvError);
        mRecyclerView = v.findViewById(R.id.recyclerView);

        orderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdminOrderItemPastAdapter(orderItems);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        GETOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
        cvRetry.setOnClickListener(v1 -> GETOrders(v1.findViewById(R.id.vLine), v1.findViewById(R.id.vLineGrey)));

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
                                orderItems.add(new AdminOrderItemPast(Orders.getInt("oID"),
                                        Orders.getInt("oNumber"), Orders.getString("oRecievedAt"),
                                        Orders.getString("oIngredients"), Orders.getString("oExtras"),
                                        Orders.getInt("oRating"), Orders.getString("oFeedback"),
                                        Orders.getDouble("oPrice"), getSetSelected(Orders.getInt("oID"))));
                            }
                            scrollToPosition();
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
                    tvError.setText(getError(error));
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

    private void scrollToPosition() {
        if ((oID != -1)) {
            for (int i = 0; i < orderItems.size(); i++) {
                if (orderItems.get(i).getIntID() == oID) {
                    mRecyclerView.scrollToPosition(i);
                    return;
                }
            }
        }
    }

    private Drawable getSetSelected(int oID) {
        if ((OrdersActivity.oID == oID)) {
            return getResources().getDrawable(R.color.colorPrimaryTrans);
        }
        return getResources().getDrawable(R.color.common_google_signin_btn_text_dark_default);
    }
}
