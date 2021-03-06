package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.ExtraItemAdapter;
import SingleItem.ExtraItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.getError;
import static util.HelperMethods.handler;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.MyShopsFragment.getNewShop;

public class NewExtrasActivity extends AppCompatActivity {
    private static final String TAG = "NewExtrasActivity";
    private static ArrayList<ExtraItem> extraItems;
    private RecyclerView mRecyclerView;
    private ExtraItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvEmpty, tvError;
    private RelativeLayout rlLoad, rlError;
    private RequestQueue requestQueue;
    private Dialog myDialog;
    private CardView cvRetry;

    private static boolean isNew = false;

    public static boolean isNew() {
        return isNew;
    }

    private MaterialEditText etExtra;

    public static void setIsNew(boolean isNew) {
        NewExtrasActivity.isNew = isNew;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_extras);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        etExtra = findViewById(R.id.etExtra);
        myDialog = new Dialog(this);
        extraItems = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);

        tvEmpty = findViewById(R.id.tvEmpty);
        rlLoad = findViewById(R.id.rlLoad);
        rlError = findViewById(R.id.rlError);
        tvError = findViewById(R.id.tvError);
        cvRetry = findViewById(R.id.cvRetry);

        GETExtras(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
        cvRetry.setOnClickListener(v -> GETExtras(findViewById(R.id.vLine), findViewById(R.id.vLineGrey)));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExtraItemAdapter(extraItems);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnExtraClickListener(new ExtraItemAdapter.OnExtraClickListener() {
            @Override
            public void onRemoveClick(int position) {
                DELETEExtra(position);
            }

            @Override
            public void onEditClick(int position) {
                ShowEditExtraPopup(position);
            }
        });
    }

    public void next(View view) {
        getNewShop().setMenuItems(getNewShop().getMenuItems());
        isNew = true;
        setIntFragment(3);
        startActivity(new Intent(NewExtrasActivity.this, MainActivity.class));
    }

    public void back(View view) {
        finish();
    }

    public void add(View view) {
        if (Objects.requireNonNull(etExtra.getText()).toString().isEmpty()) {
            etExtra.setError("required");
        } else if (!exists()) {
            POSTRegisterShopExtra();
        }
    }

    private boolean exists() {
        for (ExtraItem extraItem : extraItems) {
            if (Objects.requireNonNull(etExtra.getText()).toString().toLowerCase().
                    equals(extraItem.getStrExtraName().toLowerCase())) {
                etExtra.setError("already exists");
                return true;
            }
        }
        return false;
    }

    private void POSTRegisterShopExtra() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/shops/Register/Extra",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response);
                        if (JSONData.getString("data").equals("saved")) {
                            tvEmpty.setVisibility(View.GONE);
                            JSONArray jsonArray = new JSONArray(JSONData.getString("response"));
                            JSONObject JSONResponse = jsonArray.getJSONObject(0);
                            extraItems.add(new ExtraItem(JSONResponse.getInt("eID"),
                                    JSONResponse.getString("eName")));
                            mAdapter.notifyItemInserted(extraItems.size());
                            etExtra.setText("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, getError(error), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("eName", Objects.requireNonNull(etExtra.getText()).toString());
                params.put("sID", String.valueOf(getNewShop().getIntID()));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private void DELETEExtra(int position) {
        ShowLoadingPopup(myDialog, true);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                getIpAddress() + "/shops/Register/Extra/" + extraItems.get(position).getIntID(), null,
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONData = new JSONObject(response.toString());
                        if (JSONData.getString("data").equals("removed")) {
                            extraItems.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            if (extraItems.size() < 1) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Loads shops starting with the one closest to user
                },
                error -> {
                    Toast.makeText(this, getError(error), Toast.LENGTH_LONG).show();
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    private void PUTExtra(int position, String name) {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/shops/Register/Extra/" + extraItems.get(position).getIntID(),
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    extraItems.get(position).setStrExtraName(name);
                    mAdapter.notifyItemChanged(position);
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, getError(error), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("eName", name);
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void ShowEditExtraPopup(int position) {
        MaterialEditText etExtra;
        CardView cvEditOption;
        TextView tvCancel, tvHeading;
        myDialog.setContentView(R.layout.popup_edit_text);

        etExtra = myDialog.findViewById(R.id.etExtra);
        cvEditOption = myDialog.findViewById(R.id.cvEditOption);
        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvHeading = myDialog.findViewById(R.id.tvHeading);

        tvHeading.setText("Edit Extra");
        etExtra.setHint("Extra name");
        etExtra.setText(extraItems.get(position).getStrExtraName());
        tvCancel.setOnClickListener(view -> myDialog.dismiss());

        cvEditOption.setOnClickListener(view -> {
            if (Objects.requireNonNull(etExtra.getText()).toString().isEmpty()) {
                etExtra.setError("required");
            } else {
                PUTExtra(position, Objects.requireNonNull(etExtra.getText()).toString());
            }
        });

        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public void reload(View view) {
        GETExtras(findViewById(R.id.vLine), findViewById(R.id.vLineGrey));
    }

    private void GETExtras(View vLine, View vLineGrey) {
        rlError.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        handler(vLine, vLineGrey);
        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/shops/Extras/" + getNewShop().getIntID(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    rlLoad.setVisibility(View.GONE);
                    //Loads shops starting with the one closest to user
                    try {
                        if (response.getString("message").equals("shops")) {
                            JSONArray jsonArray = response.getJSONArray("extras");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Extras = jsonArray.getJSONObject(i);
                                extraItems.add(new ExtraItem(Extras.getInt("eID"), Extras.getString("eName")));
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
                    tvError.setText(getError(error));
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
