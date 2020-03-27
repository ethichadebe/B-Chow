package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.SharedPreferences;

import util.HelperMethods;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.User;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.handler;
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static www.ethichadebe.com.loxion_beanery.ProfileFragment.isLogout;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout rellay1, rellay2;
    private CheckBox cbRemember;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };
    private static User user;
    private Dialog myDialog;
    private MaterialEditText mTextPassword, mTextUsername;
    private RelativeLayout rlLoad;
    private BottomSheetBehavior bsbBottomSheetBehavior;
    private TextView mViewError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        cbRemember = findViewById(R.id.cbRemember);
        rlLoad = findViewById(R.id.rlLoad);
        View bsbBottomSheet = findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        myDialog = new Dialog(this);

        if (isLogout) {
            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), "", "");
        }

        //Check if shared prefs are empty
        loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), mTextUsername, mTextPassword);
        if (!Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            PostLogin(findViewById(R.id.vLine), findViewById(R.id.vLineGrey), false);
        } else {
            handler.postDelayed(runnable, 3000);
        }

        mViewError = findViewById(R.id.lblError);
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void PostLogin(View vLine, View vLineGrey, boolean isPopup) {
        rlLoad.setVisibility(View.VISIBLE);
        if (isPopup || (rellay1.getVisibility() == View.VISIBLE)) {
            ShowLoadingPopup(myDialog, true);
        } else {
            handler(vLine, vLineGrey);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/users/Login",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    try {
                        JSONObject JSONResponse = new JSONObject(response);

                        if (JSONResponse.getString("data").equals("false")) {
                            ShowConfirmationPopup();
                        } else {
                            JSONObject ObjData = new JSONObject(response);
                            JSONArray ArrData = ObjData.getJSONArray("data");
                            JSONObject userData = ArrData.getJSONObject(0);
                            //Toast.makeText(LoginActivity.this, userData.toString(), Toast.LENGTH_LONG).show();
                            user = new User(userData.getInt("uID"), userData.getString("uName"),
                                    userData.getString("uSurname"), userData.getString("uDOB"),
                                    userData.getString("uSex"), userData.getString("uEmail"),
                                    userData.getString("uNumber"));
                            if (cbRemember.isChecked()) {//Check if remember me is checked
                                saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE),
                                        Objects.requireNonNull(mTextUsername.getText()).toString(),
                                        Objects.requireNonNull(mTextPassword.getText()).toString());
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }, error -> {
            rlLoad.setVisibility(View.GONE);
            ShowLoadingPopup(myDialog, false);
            bsbBottomSheetBehavior.setHideable(false);
            bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uNumber", Objects.requireNonNull(mTextUsername.getText()).toString());
                params.put("uPassword", Objects.requireNonNull(mTextPassword.getText()).toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage, tvYes, tvNo;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);
        tvYes = myDialog.findViewById(R.id.tvYes);
        tvNo = myDialog.findViewById(R.id.tvNo);

        tvCancel.setVisibility(View.GONE);

        tvMessage.setText("Seems like the user does not exist");

        tvYes.setText("Create account");
        cvYes.setOnClickListener(view -> {
            myDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        tvNo.setText("Retry");
        cvNo.setOnClickListener(view -> myDialog.dismiss());
        Objects.requireNonNull(myDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
    }

    public static User getUser() {
        return user;
    }

    public void Retry(View view) {
        bsbBottomSheetBehavior.setHideable(true);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        PostLogin(findViewById(R.id.vLine), findViewById(R.id.vLineGrey), false);
    }

    public void login(View view) {
        if (Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setUnderlineColor(getResources().getColor(R.color.Red));
            mTextPassword.setUnderlineColor(getResources().getColor(R.color.Red));
            mViewError.setText("Enter Both fields");
        } else if (mTextUsername.getText().toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setUnderlineColor(getResources().getColor(R.color.Red));
            mTextPassword.setUnderlineColor(getResources().getColor(R.color.Black));
            mViewError.setText("Enter Username");
        } else if (!mTextUsername.getText().toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextPassword.setUnderlineColor(getResources().getColor(R.color.Red));
            mTextUsername.setUnderlineColor(getResources().getColor(R.color.Black));
            mViewError.setText("Enter Password");
        } else {
            PostLogin(findViewById(R.id.vLine), findViewById(R.id.vLineGrey), true);
        }
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}