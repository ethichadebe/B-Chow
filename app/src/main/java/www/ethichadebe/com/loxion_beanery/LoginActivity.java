package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static www.ethichadebe.com.loxion_beanery.ProfileFragment.isLogout;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout rellay1;
    private CheckBox cbRemember;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };
    private static User user;
    private Dialog myDialog;
    private MaterialEditText mTextPassword, mTextUsername;
    private BottomSheetBehavior bsbBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rellay1 = findViewById(R.id.rellay1);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        cbRemember = findViewById(R.id.cbRemember);
        View bsbBottomSheet = findViewById(R.id.bottom_sheet);
        bsbBottomSheetBehavior = BottomSheetBehavior.from(bsbBottomSheet);
        bsbBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        myDialog = new Dialog(this);

        if (isLogout){
            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), "", "");
        }

        //Check if shared prefs are empty
        loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), mTextUsername, mTextPassword);
        if (!Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            PostLogin(false);
        } else {
            handler.postDelayed(runnable, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void PostLogin(boolean isPopup) {
        if (isPopup || (rellay1.getVisibility() == View.VISIBLE)) {
            ShowLoadingPopup(myDialog, true);
        } else {
            YoYo.with(Techniques.FadeIn)
                    .duration(2000)
                    .repeat(2000)
                    .playOn(findViewById(R.id.vLoading));
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/users/Login",
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
                                isLogout=false;
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
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
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
        TextView tvCancel, tvMessage;
        Button btnYes, btnNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        btnYes = myDialog.findViewById(R.id.btnYes);
        btnNo = myDialog.findViewById(R.id.btnNo);

        tvCancel.setVisibility(View.GONE);

        tvMessage.setText("Seems like the user does not exist");

        btnYes.setText("Create account");
        btnYes.setOnClickListener(view -> {
            myDialog.dismiss();
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnNo.setText("Retry");
        btnNo.setOnClickListener(view -> myDialog.dismiss());
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
        PostLogin(false);
    }

    public void login(View view) {
        if (Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setError("Required");
            mTextPassword.setError("Required");
        } else if (mTextUsername.getText().toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextUsername.setError("Required");
        } else if (!mTextUsername.getText().toString().isEmpty() &&
                Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            mTextPassword.setError("Required");
            mTextPassword.setHelperText("Field required");
        } else {
            PostLogin(true);
        }
    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}