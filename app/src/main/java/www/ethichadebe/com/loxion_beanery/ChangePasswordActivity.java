package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.checkData;
import static util.HelperMethods.getError;
import static util.HelperMethods.loadData;
import static util.HelperMethods.saveData;
import static util.HelperMethods.sharedPrefsIsEmpty;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.setUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private MaterialEditText etOld, etNew, etCNew;
    private TextView tvSave;
    private Dialog myDialog;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private JsonObjectRequest objectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //heck if user is logged in
        if (checkData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
            setUser(loadData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));
        }

        myDialog = new Dialog(this);
        etOld = findViewById(R.id.etOldPassword);
        etNew = findViewById(R.id.etNewPassword);
        etCNew = findViewById(R.id.etCNewPassword);
        tvSave = findViewById(R.id.tvSave);

        tvSave.setOnClickListener(view -> {
            if (Objects.requireNonNull(etNew.getText()).toString().
                    equals(Objects.requireNonNull(etCNew.getText()).toString())) {
                ChangePassword();
            } else {
                etCNew.setError("Password does not match");
            }
        });

        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 3) {
                    CheckPassword();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void CheckPassword() {
        requestQueue = Volley.newRequestQueue(this);
        objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                getIpAddress() + "/users/CheckPassword/" + getUser().getuID() + "/" +
                        Objects.requireNonNull(etOld.getText()).toString(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        if (response.getString("message").equals("true")) {
                            tvSave.setClickable(true);
                            tvSave.setBackground(getResources().getDrawable(R.drawable.ripple_effect));
                            etOld.setError("correct");
                            etOld.setErrorColor(R.color.done);
                        } else {
                            tvSave.setClickable(false);
                            tvSave.setBackground(getResources().getDrawable(R.color.Transparent_DarkGrey));
                            etOld.setError("incorrect");
                            etOld.setErrorColor(R.color.RedColor);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                        Toast.makeText(this, getError(error), Toast.LENGTH_SHORT).show();
                });
        objectRequest.setTag(TAG);
        requestQueue.add(objectRequest);
    }

    private void ChangePassword() {
        ShowLoadingPopup(myDialog, true);
        stringRequest = new StringRequest(Request.Method.PUT,
                getIpAddress() + "/users/ChangePassword/" + getUser().getuID(),
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    if (!sharedPrefsIsEmpty(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), getUser(), true);
                        }
                    }
                    finish();
                }, error -> {
            ShowLoadingPopup(myDialog, false);
                Toast.makeText(this, getError(error), Toast.LENGTH_SHORT).show();

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uPassword", Objects.requireNonNull(etNew.getText()).toString());
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
}
