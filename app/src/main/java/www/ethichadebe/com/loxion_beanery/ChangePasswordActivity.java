package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
import static util.HelperMethods.saveData;
import static util.HelperMethods.sharedPrefsIsEmpty;
import static www.ethichadebe.com.loxion_beanery.LoginActivity.getUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private MaterialEditText etOld, etNew, etCNew;
    private Button btnSave;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        myDialog = new Dialog(this);
        etOld = findViewById(R.id.etOldPassword);
        etNew = findViewById(R.id.etNewPassword);
        etCNew = findViewById(R.id.etCNewPassword);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(view -> {
            if (Objects.requireNonNull(etNew.getText()).toString().
                    equals(Objects.requireNonNull(etCNew.getText()).toString())) {
                ChangePassword();
            } else {
                etCNew.setError("Password does not match");
            }
        });

        etOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                CheckPassword();
            }
        });

    }

    private void CheckPassword() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://" + getIpAddress() + "/users/CheckPassword/" +getUser().getuID()+"/"+
                        Objects.requireNonNull(etOld.getText()).toString(), null,
                response -> {
                    //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    try {
                        if (response.getString("message").equals("true")) {
                            btnSave.setVisibility(View.VISIBLE);
                            etOld.setError("correct");
                            etOld.setErrorColor(R.color.done);
                        } else {
                            btnSave.setVisibility(View.GONE);
                            etOld.setError("incorrect");
                            etOld.setErrorColor(R.color.Red);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.toString().equals("com.android.volley.TimeoutError")) {
                        Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(objectRequest);
    }

    private void ChangePassword() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                "http://" + getIpAddress() + "/users/ChangePassword/" + getUser().getuID(),
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    if (!sharedPrefsIsEmpty(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), getUser().getuNumber(), Objects.requireNonNull(etNew.getText()).toString());
                        }
                    }
                    finish();
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            if (error.toString().equals("com.android.volley.TimeoutError")) {
                Toast.makeText(this, "Connection error. Please retry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("uPassword", Objects.requireNonNull(etNew.getText()).toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void back(View view) {
        finish();
    }
}
