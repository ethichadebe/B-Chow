package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.getError;
import static util.HelperMethods.loadData;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;
import static www.ethichadebe.com.loxion_beanery.RegisterActivity.getNewUser;

public class UserTypeActivity extends AppCompatActivity {
    private static final String TAG = "UserTypeActivity";
    private Dialog myDialog;
    private CardView cvOwner, cvEmployee, cvUser;
    private TextView tvRegister, tvOwner, tvEmployee, tvUser;
    private int userType = 3;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }
        myDialog = new Dialog(this);

        cvOwner = findViewById(R.id.cvOwner);
        cvEmployee = findViewById(R.id.cvEmployee);
        cvUser = findViewById(R.id.cvUser);

        tvRegister = findViewById(R.id.tvRegister);

        tvOwner = findViewById(R.id.tvOwner);
        tvEmployee = findViewById(R.id.tvEmployee);
        tvUser = findViewById(R.id.tvUser);

        //When user selects I own a beanery
        cvOwner.setOnClickListener(view -> {
            userType = 1;
            tvOwner.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
            tvEmployee.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            tvUser.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));

            tvOwner.setTextColor(Color.WHITE);
            tvEmployee.setTextColor(Color.GRAY);
            tvUser.setTextColor(Color.GRAY);

            tvRegister.setVisibility(View.VISIBLE);
        });

        //When user selects I am employee at a beanery
        cvEmployee.setOnClickListener(view -> {
            userType = 2;
            tvEmployee.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
            tvOwner.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            tvUser.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));

            tvEmployee.setTextColor(Color.WHITE);
            tvOwner.setTextColor(Color.GRAY);
            tvUser.setTextColor(Color.GRAY);

            tvRegister.setVisibility(View.VISIBLE);
        });

        //When user selects I am a customer
        cvUser.setOnClickListener(view -> {
            userType = 3;
            tvUser.setBackground(getResources().getDrawable(R.drawable.ripple_effect_green));
            tvEmployee.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));
            tvOwner.setBackground(getResources().getDrawable(R.drawable.ripple_effect_white));

            tvUser.setTextColor(Color.WHITE);
            tvOwner.setTextColor(Color.GRAY);
            tvEmployee.setTextColor(Color.GRAY);

            tvRegister.setVisibility(View.VISIBLE);
        });
    }

    private void POSTRegister() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/users/Register",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject JSONResponse = new JSONObject(response);

                        if (JSONResponse.getString("data").equals("Registered")) {
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, UploadPicActivity.class);
                            intent.putExtra("U_ID", JSONResponse.getInt("uID"));
                            startActivity(intent);
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
                params.put("uName", getNewUser().getuName());
                params.put("uSurname", getNewUser().getuSurname());
                params.put("uAddress", getNewUser().getuAddress());
                params.put("uLatitude", String.valueOf(getNewUser().getuLocation().latitude));
                params.put("uLongitude", String.valueOf(getNewUser().getuLocation().longitude));
                params.put("uSex", getNewUser().getuSex());
                params.put("uEmail", getNewUser().getuEmail());
                params.put("uNumber", getNewUser().getuNumber());
                params.put("uPassword", getNewUser().getuPassword());
                params.put("uType", String.valueOf(userType));
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    public void register(View view) {
        POSTRegister();
    }
}
