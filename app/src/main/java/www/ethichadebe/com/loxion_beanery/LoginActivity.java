package www.ethichadebe.com.loxion_beanery;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.HelperMethods;
import util.User;

import static util.Constants.getIpAddress;

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
    private TextView mViewError;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        mTextUsername = findViewById(R.id.txtUsername);
        mTextPassword = findViewById(R.id.txtPassword);
        cbRemember = findViewById(R.id.cbRemember);

        myDialog = new Dialog(this);

        //Check if shared prefs are empty
        loadData();
        if (!Objects.requireNonNull(mTextUsername.getText()).toString().isEmpty() &&
                !Objects.requireNonNull(mTextPassword.getText()).toString().isEmpty()) {
            PostLogin();
        } else {
            handler.postDelayed(runnable, 3000);
        }

        //Image
        CardView mButtonLogin = findViewById(R.id.btnLogin);
        CardView mButtonRegister = findViewById(R.id.btnRegister);

        mViewError = findViewById(R.id.lblError);

        mButtonLogin.setOnClickListener(view -> {
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
                PostLogin();
            }
        });

        mButtonRegister.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, Objects.requireNonNull(mTextUsername.getText()).toString());
        editor.putString(PASSWORD, Objects.requireNonNull(mTextPassword.getText()).toString());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        mTextPassword.setText(sharedPreferences.getString(PASSWORD, ""));
        mTextUsername.setText(sharedPreferences.getString(USERNAME, ""));
    }

    private void PostLogin() {
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/users/Login",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
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
                            if (cbRemember.isChecked()) {
                                saveData();
                            }
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
}