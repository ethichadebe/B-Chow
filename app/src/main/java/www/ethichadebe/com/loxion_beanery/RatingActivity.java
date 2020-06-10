package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static util.Constants.getIpAddress;
import static util.HelperMethods.ShowLoadingPopup;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.MainActivity.setIntFragment;
import static www.ethichadebe.com.loxion_beanery.PastOrderFragmentCustomer.getPastOrderItem;

public class RatingActivity extends AppCompatActivity {
    private static final String TAG = "RatingActivity";
    private RequestQueue requestQueue;
    private Button btnNext;
    private Dialog myDialog;
    private MaterialEditText etFeedback;
    private RatingBar rbRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        btnNext = findViewById(R.id.btnNext);
        etFeedback = findViewById(R.id.etFeedback);
        rbRating = findViewById(R.id.rbRating);
        rbRating.setOnRatingBarChangeListener((ratingBar, intRating, b) -> {
            if (rbRating.getRating() != 0) {
                btnNext.setVisibility(View.VISIBLE);
            } else {
                btnNext.setVisibility(View.GONE);
            }
        });
        btnNext.setOnClickListener(view -> PUTRating());
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