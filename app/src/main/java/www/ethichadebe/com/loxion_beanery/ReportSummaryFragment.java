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
import java.util.Collections;
import java.util.Objects;

import Adapter.CancelledOrderItemAdapter;
import Adapter.IngredientReportItemAdapter;
import Adapter.MenuStatItemAdapter;
import SingleItem.CancelledOrderItem;
import SingleItem.IngredientReportItem;
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
    private ArrayList<IngredientReportItem> ingredientItems;
    private ArrayList<IngredientReportItem> extraItems;
    private RelativeLayout rlmHeading, rlcHeading, rliHeading, rleHeading;
    private ExpandableLayout[] elm = new ExpandableLayout[4];
    private RecyclerView[] mRecyclerView = new RecyclerView[4];
    private RecyclerView.LayoutManager[] mLayoutManager = new RecyclerView.LayoutManager[4];
    private MenuStatItemAdapter mAdapter;
    private CancelledOrderItemAdapter cAdapter;
    private IngredientReportItemAdapter iAdapter;
    private IngredientReportItemAdapter eAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report_summary, container, false);

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));

        tvTotal = v.findViewById(R.id.tvTotal);
        tvnSold = v.findViewById(R.id.tvnSold);
        tvnCancelled = v.findViewById(R.id.tvnCancelled);

        mRecyclerView[0] = v.findViewById(R.id.rvMenu);
        mRecyclerView[1] = v.findViewById(R.id.rvnCancelled);
        mRecyclerView[2] = v.findViewById(R.id.rvnIngredient);
        mRecyclerView[3] = v.findViewById(R.id.rvExtra);

        elm[0] = v.findViewById(R.id.elm);
        elm[1] = v.findViewById(R.id.elc);
        elm[2] = v.findViewById(R.id.eli);
        elm[3] = v.findViewById(R.id.ele);

        rlmHeading = v.findViewById(R.id.rlmHeading);
        rlcHeading = v.findViewById(R.id.rlcHeading);
        rliHeading = v.findViewById(R.id.rliHeading);
        rleHeading = v.findViewById(R.id.rleHeading);

        mLayoutManager[0] = new LinearLayoutManager(getActivity());
        mLayoutManager[1] = new LinearLayoutManager(getActivity());
        mLayoutManager[2] = new LinearLayoutManager(getActivity());
        mLayoutManager[3] = new LinearLayoutManager(getActivity());

        orderItems = new ArrayList<>();
        cOrderItems = new ArrayList<>();
        ingredientItems = new ArrayList<>();
        extraItems = new ArrayList<>();

        mAdapter = new MenuStatItemAdapter(orderItems);
        cAdapter = new CancelledOrderItemAdapter(cOrderItems);
        iAdapter = new IngredientReportItemAdapter(ingredientItems);
        eAdapter = new IngredientReportItemAdapter(extraItems);


        mRecyclerView[0].setHasFixedSize(false);
        mRecyclerView[0].setLayoutManager(mLayoutManager[0]);
        mRecyclerView[0].setAdapter(mAdapter);

        mRecyclerView[1].setHasFixedSize(false);
        mRecyclerView[1].setLayoutManager(mLayoutManager[1]);
        mRecyclerView[1].setAdapter(cAdapter);

        mRecyclerView[2].setHasFixedSize(false);
        mRecyclerView[2].setLayoutManager(mLayoutManager[2]);
        mRecyclerView[2].setAdapter(iAdapter);

        mRecyclerView[3].setHasFixedSize(false);
        mRecyclerView[3].setLayoutManager(mLayoutManager[3]);
        mRecyclerView[3].setAdapter(eAdapter);

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

        rliHeading.setOnClickListener(v1 -> {
            if (elm[2].isExpanded()) {
                elm[2].collapse();
            } else {
                elm[2].expand();
            }
        });

        rleHeading.setOnClickListener(v1 -> {
            if (elm[3].isExpanded()) {
                elm[3].collapse();
            } else {
                elm[3].expand();
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
                            int nSold = 1, nCancelled = 1;
                            boolean isNew = false;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                Log.d(TAG, "GETSuccOrders: " + Orders.getString("oIngredients"));
                                if (Orders.getString("oStatus").equals("Cancelled")) {
                                    tvnCancelled.setText(String.valueOf(nCancelled++));
                                    cOrderItems.add(new CancelledOrderItem(Orders.getDouble("oPrice"), Orders.getString("oIngredients"),
                                            Orders.getString("oFeedback")));
                                } else if (Orders.getString("oStatus").equals("Collected")) {
                                    tvnSold.setText(String.valueOf(nSold++));
                                    ingredients(Orders.getString("oIngredients"));
                                    extras(Orders.getString("oExtras"));
                                    if (orderItems.size() > 0) {
                                        for (int a = 0; a < orderItems.size(); a++) {
                                            if (orderItems.get(a).getStrMenu().equals(Orders.getString("oIngredients"))) {
                                                orderItems.get(a).setIntnItems(orderItems.get(a).getIntnItems() + 1);
                                                isNew = false;
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
                                Collections.sort(orderItems);
                                mAdapter.notifyDataSetChanged();
                                cAdapter.notifyDataSetChanged();
                                tvTotal.setText("R" + totalPrice + "0");
                            }
                            elm[0].expand();
                            iAdapter.notifyDataSetChanged();
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

    private void ingredients(String menu) {
        boolean isNew = false;
        //In each menu item, get all the ingredients
        String[] strIngredients = menu.split(", ");
        for (String strIngredient : strIngredients) {
            //Check if the arrayList is not empty
            if (ingredientItems.size() > 0) {
                //Loop through all the arrayList elements to check if any of the elements match the ingredient
                for (int i = 0; i < ingredientItems.size(); i++) {
                    //If it matches then increment the ingredient count in the arrayList
                    if (ingredientItems.get(i).getStrName().equals(strIngredient)) {
                        ingredientItems.get(i).setIntCount(ingredientItems.get(i).getIntCount() + 1);
                        isNew = false;
                        break;
                    } else {
                        isNew = true;
                    }

                }

                if (isNew) {
                    ingredientItems.add(new IngredientReportItem(strIngredient, 1));
                }
            } else {
                ingredientItems.add(new IngredientReportItem(strIngredient, 1));
            }
        }
        Collections.sort(ingredientItems);
    }

    private void extras(String extras) {
        boolean isNew = false;
        if (!extras.isEmpty()) {
            //In each menu item, get all the ingredients
            String[] strExtras = extras.split(", ");
            for (String strExtra : strExtras) {
                //Check if the arrayList is not empty
                if (extraItems.size() > 0) {
                    //Loop through all the arrayList elements to check if any of the elements match the ingredient
                    for (int i = 0; i < extraItems.size(); i++) {
                        //If it matches then increment the ingredient count in the arrayList
                        if (extraItems.get(i).getStrName().equals(strExtra)) {
                            extraItems.get(i).setIntCount(extraItems.get(i).getIntCount() + 1);
                            isNew = false;
                            break;
                        } else {
                            isNew = true;
                        }

                    }

                    if (isNew) {
                        extraItems.add(new IngredientReportItem(strExtra, 1));
                    }
                } else {
                    extraItems.add(new IngredientReportItem(strExtra, 1));
                }
            }
        }
        Collections.sort(extraItems);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
