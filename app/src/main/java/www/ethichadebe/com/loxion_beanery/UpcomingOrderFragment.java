package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.AdminOrderItemAdapter;
import SingleItem.AdminOrderItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.MainActivity.getUpcomingOrderItem;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;
import static www.ethichadebe.com.loxion_beanery.OrdersActivity.oID;

public class UpcomingOrderFragment extends Fragment {
    private static final String TAG = "UpcomingOrderFragment";
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private RecyclerView mRecyclerView;
    private AdminOrderItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AdminOrderItem> orderItems;
private CardView cvRetry;
    private TextView tvEmpty;
    private RelativeLayout rlLoad, rlError;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming_order, container, false);

        tvEmpty = v.findViewById(R.id.tvEmpty);
        rlLoad = v.findViewById(R.id.rlLoad);
        rlError = v.findViewById(R.id.rlError);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        cvRetry = v.findViewById(R.id.cvRetry);
        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        orderItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdminOrderItemAdapter(orderItems);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdminOrderItemAdapter.OnItemClickListener() {

            @Override
            public void onCancelClick(int position) {
                ShowConfirmationPopup(position);
            }

            @Override
            public void onDoneClick(int position) {
                PUTReady(position, getNewShop().getIntID());
            }

            @Override
            public void onCollectedClick(int position) {
                PUTCollected(position, myDialog);
            }
        });

        GETOrders(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));

        cvRetry.setOnClickListener(v1 -> GETOrders(v1.findViewById(R.id.vLine), v1.findViewById(R.id.vLineGrey)));
        return v;
    }

    private void ShowConfirmationPopup(final int position) {
        TextView tvCancel, tvMessage, tvHeading, btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        tvHeading = myDialog.findViewById(R.id.tvHeading);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        tvHeading.setText("Cancel order");
        tvMessage.setText("Are you sure?");

        btnYes.setOnClickListener(view -> PUTCancel(position, myDialog));

        btnNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void GETOrders(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/orders/" + getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    try {
                        if (response.getString("message").equals("orders")) {
                            JSONArray jsonArray = response.getJSONArray("orders");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Orders = jsonArray.getJSONObject(i);
                                orderItems.add(new AdminOrderItem(Orders.getInt("oID"), Orders.getInt("oNumber"),
                                        Orders.getString("oRecievedAt"), Orders.getString("oIngredients"),
                                        Orders.getString("oExtras"), Orders.getString("oStatus"),
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
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    private void PUTCancel(int position, Dialog myDialog) {
        ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Cancel/" + orderItems.get(position).getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    myDialog.dismiss();
                    orderItems.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    ShowLoadingPopup(myDialog, false);
                }, error -> {
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void PUTReady(int position, int sID) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Ready/" + orderItems.get(position).getIntID() + "/" + sID,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    orderItems.get(position).setStrStatus("Ready for collection");
                    mAdapter.notifyItemChanged(position);
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void PUTCollected(int position, Dialog myDialog) {
        ShowLoadingPopup(this.myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Collected/" + orderItems.get(position).getIntID(),
                response -> {
                    //Toast.makeText(this, response, Toast.LENGTH_LONG).s  ();
                    myDialog.dismiss();
                    orderItems.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    ShowLoadingPopup(myDialog, false);
                }, error -> {
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(getActivity(), "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
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
