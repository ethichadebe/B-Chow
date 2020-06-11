package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Adapter.MyShopItemAdapter;
import SingleItem.MyShopItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ButtonVisibility;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.handler;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.isNew;
import static www.ethichadebe.com.loxion_beanery.NewExtrasActivity.setIsNew;
import static www.ethichadebe.com.loxion_beanery.ShopSettingsActivity.isEdit;

public class MyShopsFragment extends Fragment {
    private static final String TAG = "MyShopsFragment";
    private Dialog myDialog;
    private ArrayList<MyShopItem> shopItems;
    private RelativeLayout rlError, rlLoad;
    private LinearLayout llNewShop;
    private RecyclerView mRecyclerView;
    private MyShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BottomSheetBehavior bsbBottomSheetBehavior;
    private static MyShopItem newShop;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    };
    private View bsbBottomSheet;
    private TextView tvEmpty;
    private RequestQueue requestQueue;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frame_my_shops, container, false);

        if (getUser() == null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }//If app crashes, return to login


        newShop = null;

        //Initialise bottom sheet
        bsbBottomSheet = v.findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Display bottom sheet when shop has been registered
        if (isNew() && !isEdit) {
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            handler.postDelayed(runnable, 1500);
            setIsNew(false);
        }

        //If a certain shop was being edited, return is change shop edit status to false
        isEdit = false;

        myDialog = new Dialog(Objects.requireNonNull(getActivity()));
        shopItems = new ArrayList<>();
        rlError = v.findViewById(R.id.rlError);
        rlLoad = v.findViewById(R.id.rlLoad);
        tvEmpty = v.findViewById(R.id.tvEmpty);
        llNewShop = v.findViewById(R.id.llNewShop);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MyShopItemAdapter(shopItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //Get User Shops
        GETShops(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey));
        rlLoad.setOnClickListener(view -> GETShops(v.findViewById(R.id.vLine), v.findViewById(R.id.vLineGrey)));

        if (getUser().getuType() == 2) {
            llNewShop.setVisibility(View.GONE);
        }//If user is an employee then they cant add a shop

        llNewShop.setOnClickListener(view -> {
            newShop = null;
            startActivity(new Intent(getActivity(), RegisterShopActivity.class));
        });

        mAdapter.setOnItemClickListerner(new MyShopItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                newShop = shopItems.get(position);
                startActivity(new Intent(getActivity(), OrdersActivity.class));
            }

            @Override
            public void onItemDelete(int position) {
                DELETEShop(position);
            }

            @Override
            public void onItemClickResumeRegistration(int position) {
                newShop = shopItems.get(position);
                startActivity(new Intent(getActivity(), RegisterShopActivity.class));
            }
        });
        return v;
    }

    private void GETShops(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/MyShops/" + getUser().getuID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("shops");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Shops = jsonArray.getJSONObject(i);
                                boolean isActive = false;
                                if (Shops.getInt("isActive") == 1) {
                                    isActive = true;
                                }
                                Drawable bgStatus = null;
                                switch (Shops.getInt("sStatus")) {
                                    case 1:
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open);
                                        break;
                                    case 0:
                                        bgStatus = getResources().getDrawable(R.drawable.empty_btn_bg_open_closed);
                                        break;
                                }
                                shopItems.add(new MyShopItem(Shops.getInt("sID"), Shops.getString("sName"),
                                        Shops.getString("uRole"),
                                        Shops.getString("sSmallPicture"),
                                        Shops.getString("sBigPicture"),
                                        Shops.getString("sShortDescrption"),
                                        Shops.getString("sFullDescription"),
                                        new LatLng(Shops.getDouble("sLatitude"),
                                                Shops.getDouble("sLongitude")),
                                        Shops.getString("sAddress"), "10-15 mins",
                                        Shops.getInt("sRating"), Shops.getString("sOperatingHrs"), isActive,
                                        Shops.getInt("sStatus"), bgStatus, Shops.getInt("nOrders")));
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

    public static MyShopItem getNewShop() {
        return newShop;
    }

    static void setNewShop(MyShopItem newShop) {
        MyShopsFragment.newShop = newShop;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    private void DELETEShop(int position) {
        ShowLoadingPopup(myDialog, true);
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getIpAddress() + "/shops/Delete/" + shopItems.get(position).getIntID(), null,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            shopItems.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            Toast.makeText(getActivity(), "Shop Deleted", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
}
