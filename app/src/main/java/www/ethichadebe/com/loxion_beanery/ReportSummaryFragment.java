package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
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

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.CancelledOrderItemAdapter;
import Adapter.MenuStatItemAdapter;
import SingleItem.CancelledOrderItem;
import SingleItem.MenuStatItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;

public class ReportSummaryFragment extends Fragment {
    private static final String TAG = "ReportSummaryFragment";
    private Dialog myDialog;
    private RequestQueue requestQueue;
    private TextView tvTotal, tvnSold, tvnCancelled;

    private ArrayList<MenuStatItem> orderItems;
    private ArrayList<CancelledOrderItem> cOrderItems;
    private RelativeLayout rlmHeading, rlcHeading;
    private ExpandableLayout[] elm = new ExpandableLayout[2];
    private MenuStatItemAdapter mAdapter;
    private CancelledOrderItemAdapter cAdapter;
    private RecyclerView[] mRecyclerView = new RecyclerView[2];
    private RecyclerView.LayoutManager[] mLayoutManager = new RecyclerView.LayoutManager[2];

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_summary, container, false);

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        tvTotal = v.findViewById(R.id.tvTotal);
        tvnSold = v.findViewById(R.id.tvnSold);
        mRecyclerView[0] = v.findViewById(R.id.rvMenu);
        mRecyclerView[1] = v.findViewById(R.id.rvnCancelled);
        rlmHeading = v.findViewById(R.id.rlmHeading);
        rlcHeading = v.findViewById(R.id.rlcHeading);
        tvnCancelled = v.findViewById(R.id.tvnCancelled);
        elm[0] = v.findViewById(R.id.elm);
        elm[1] = v.findViewById(R.id.elc);
        orderItems = new ArrayList<>();
        cOrderItems = new ArrayList<>();
        mLayoutManager[0] = new LinearLayoutManager(getActivity());
        mLayoutManager[1] = new LinearLayoutManager(getActivity());
        mAdapter = new MenuStatItemAdapter(orderItems);
        cAdapter = new CancelledOrderItemAdapter(cOrderItems);


        mRecyclerView[0].setHasFixedSize(false);
        mRecyclerView[0].setLayoutManager(mLayoutManager[0]);
        mRecyclerView[0].setAdapter(mAdapter);

        mRecyclerView[1].setHasFixedSize(false);
        mRecyclerView[1].setLayoutManager(mLayoutManager[1]);
        mRecyclerView[1].setAdapter(cAdapter);

        rlmHeading.setOnClickListener(v1 -> {
            if (elm[0].isExpanded()) {
                elm[0].collapse();
            } else {
                elm[0].expand();
            }
        });
        rlcHeading.setOnClickListener(v1 -> {
            if (elm[1].isExpanded()) {
                elm[1].collapse();
            } else {
                elm[1].expand();
            }
        });
        GETSuccOrders();

        return v;
    }


    private void GETSuccOrders() {
        ShowLoadingPopup(myDialog, true);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, getIpAddress() + "/orders/DailyReport/"
                + getNewShop().getIntID(), null,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            double totalPrice = 0;
                            int nSold = 1, nCancelled = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                Log.d(TAG, "GETSuccOrders: " + Orders.getString("oIngredients"));
                                if (Orders.getString("oStatus").equals("Cancelled")) {
                                    nCancelled++;
                                    cOrderItems.add(new CancelledOrderItem(Orders.getDouble("oPrice"), Orders.getString("oIngredients"),
                                            Orders.getString("oFeedback")));
                                } else if (Orders.getString("oStatus").equals("Collected")) {
                                    tvnSold.setText(String.valueOf(nSold++));
                                    if (orderItems.size() > 0) {
                                        boolean isNew = false;
                                        for (int a = 0; a < orderItems.size(); a++) {
                                            if (orderItems.get(a).getStrMenu().equals(Orders.getString("oIngredients"))) {
                                                orderItems.get(a).setIntnItems(orderItems.get(a).getIntnItems() + 1);
                                                isNew=false;
                                                break;
                                            } else {
                                                isNew = true;
                                            }
                                        }

                                        if (isNew) {
                                            orderItems.add(new MenuStatItem(Orders.getDouble("oPrice"), Orders.getString("oIngredients")
                                                    , 1));
                                        }
                                    } else {
                                        orderItems.add(new MenuStatItem(Orders.getDouble("oPrice"), Orders.getString("oIngredients")
                                                , 1));
                                    }
                                    totalPrice += Orders.getDouble("oPrice");
                                }
                                mAdapter.notifyDataSetChanged();
                                cAdapter.notifyDataSetChanged();
                                tvTotal.setText("R" + totalPrice + "0");
                                tvnCancelled.setText(String.valueOf(nCancelled));
                            }
                            elm[0].expand();
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "GETSuccOrders: " + e.toString());
                    }
                },
                error -> {
                    ShowLoadingPopup(myDialog, false);
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
