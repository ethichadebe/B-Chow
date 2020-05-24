package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
    private Button btnNext;
    private Dialog myDialog;
    private MaterialEditText etFeedback;
    private String strRating = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        if (getUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        myDialog = new Dialog(this);

        ivStar1 = findViewById(R.id.ivStar1);
        ivStar2 = findViewById(R.id.ivStar2);
        ivStar3 = findViewById(R.id.ivStar3);
        ivStar4 = findViewById(R.id.ivStar4);
        ivStar5 = findViewById(R.id.ivStar5);

        btnNext = findViewById(R.id.btnNext);

        etFeedback = findViewById(R.id.etFeedback);

        btnNext.setOnClickListener(view -> PUTRating());
        ivStar1.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star_empty);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            btnNext.setVisibility(View.VISIBLE);
            strRating = "1";
            //getPastOrderItem(getPosition()).setIntRating(1);
        });

        ivStar2.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star_empty);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            btnNext.setVisibility(View.VISIBLE);
            strRating = "2";
            //getPastOrderItem(getPosition()).setIntRating(2);
        });

        ivStar3.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star_empty);
            ivStar5.setImageResource(R.drawable.star_empty);
            btnNext.setVisibility(View.VISIBLE);
            strRating = "3";
            // getPastOrderItem(getPosition()).setIntRating(3);
        });

        ivStar4.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star);
            ivStar5.setImageResource(R.drawable.star_empty);
            btnNext.setVisibility(View.VISIBLE);
            strRating = "4";
            // getPastOrderItem(getPosition()).setIntRating(4);
        });

        ivStar5.setOnClickListener(view -> {
            ivStar1.setImageResource(R.drawable.star);
            ivStar2.setImageResource(R.drawable.star);
            ivStar3.setImageResource(R.drawable.star);
            ivStar4.setImageResource(R.drawable.star);
            ivStar5.setImageResource(R.drawable.star);
            btnNext.setVisibility(View.VISIBLE);
            strRating = "5";
            //getPastOrderItem(getPosition()).setIntRating(5);
        });
    }

    public void back(View view) {
        finish();
    }

    private void PUTRating() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/orders/Rate/"+ getPastOrderItem().getIntID(),
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
            //myDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("oRating", strRating);
                if (Objects.requireNonNull(etFeedback.getText()).toString().isEmpty()) {
                    params.put("oFeedback", "");
                } else {
                    params.put("oFeedback", Objects.requireNonNull(etFeedback.getText()).toString());
                }
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}