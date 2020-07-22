package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Adapter.AdminOrderItemPastAdapter;
import Adapter.IngreRateItemAdapter;
import SingleItem.AdminOrderItemPast;
import SingleItem.IngredientItem;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.PastOrderFragmentCustomer.getPastOrderItem;

public class RatingActivity extends AppCompatActivity {
    private static final String TAG = "RatingActivity";
    private RequestQueue requestQueue;
    private TextView tvNext;
    private Dialog myDialog;
    private MaterialEditText etFeedback;
    private RatingBar rbRating;

    /*private RecyclerView mRecyclerView;
    private IngreRateItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<IngredientItem> ingredientItems;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);

        tvNext = findViewById(R.id.tvNext);
        etFeedback = findViewById(R.id.etFeedback);
        rbRating = findViewById(R.id.rbRating);
        rbRating.setOnRatingBarChangeListener((ratingBar, intRating, b) -> {
            if (rbRating.getRating() != 0) {
                tvNext.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
            } else {
                tvNext.setBackground(getResources().getDrawable(R.color.Transparent_DarkGrey));
            }
        });
        tvNext.setOnClickListener(view -> PUTRating());

        /*mRecyclerView = findViewById(R.id.recyclerView);

        ingredientItems = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new IngreRateItemAdapter(ingredientItems);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);*/

        /*for (String ingredient : getPastOrderItem().getStrMenu().split(", ")){
            ingredientItems.add(new IngredientItem(ingredient));
        }*/

    }

    public void back(View view) {
        finish();
    }

    private void PUTRating() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Rate/" + getPastOrderItem().getIntID() + "/" + getPastOrderItem().getsID(),
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONResponse = new JSONObject(response);
                        startActivity(new Intent(this, MainActivity.class));
                        setIntFragment(1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("oRating", String.valueOf(rbRating.getRating()));
                if (Objects.requireNonNull(etFeedback.getText()).toString().isEmpty()) {
                    params.put("oFeedback", "");
                } else {
                    params.put("oFeedback", Objects.requireNonNull(etFeedback.getText()).toString());
                }
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}