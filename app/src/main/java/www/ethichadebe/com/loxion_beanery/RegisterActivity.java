package www.ethichadebe.com.loxion_beanery;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.HelperMethods;
import util.User;

import static util.Constants.getIpAddress;
import static util.HelperMethods.SHARED_PREFS;
import static util.HelperMethods.ShowLoadingPopup;
import static util.HelperMethods.allFieldsEntered;
import static util.HelperMethods.saveData;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "RegisterActivity";
    private RequestQueue requestQueue;
    private RelativeLayout rellay1;
    private Dialog myDialog;
    private static User newUser;

    public static User getNewUser() {
        return newUser;
    }

    /*  0 Name
        1 Surname
        2 Number
        3 Email
        4 Password
        5 CPassword
        6 DOB*/
    private MaterialEditText[] mTextBoxes = new MaterialEditText[7];
    private CheckBox mCBMale, mCBFemale, mCBOther;

    private String UserSex = "";
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rellay1 = findViewById(R.id.rellay1);
        myDialog = new Dialog(this);
        handler.postDelayed(runnable, 500);

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtPhone);
        mTextBoxes[3] = findViewById(R.id.txtEmail);
        mTextBoxes[4] = findViewById(R.id.txtPass);
        mTextBoxes[5] = findViewById(R.id.txtCPass);
        mTextBoxes[6] = findViewById(R.id.txtDOB);

        saveData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE), "", "");

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);


        //Date p[icker
        mTextBoxes[6].setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        //Handling Checkbox click events
        mCBMale.setOnClickListener(view -> {
            sexIsChecked();
            if (mCBMale.isChecked()) {
                mCBFemale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "male";
            } else {
                mCBMale.setChecked(true);
            }
        });
        mCBOther.setOnClickListener(view -> {
            sexIsChecked();
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
                UserSex = "other";
            } else {
                mCBOther.setChecked(true);
            }
        });
        mCBFemale.setOnClickListener(view -> {
            sexIsChecked();
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "female";
            } else {
                mCBFemale.setChecked(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        String strCurrentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        mTextBoxes[6].setText(strCurrentDate);

    }

    private void POSTRegister() {
        ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                getIpAddress() + "/users/CheckStuff",
                response -> {
                    ShowLoadingPopup(myDialog, false);
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject JSONResponse = new JSONObject(response);

                        switch (JSONResponse.getString("data")) {
                            case "both":
                                mTextBoxes[2].setError("Already exists");
                                mTextBoxes[3].setError("Already exists");
                                break;
                            case "number":
                                mTextBoxes[2].setError("Already exists");
                                break;
                            case "email":
                                mTextBoxes[3].setError("Already exists");
                                break;
                            case "Registered":
                                newUser = new User(1, Objects.requireNonNull(mTextBoxes[0].getText()).toString(),
                                        Objects.requireNonNull(mTextBoxes[1].getText()).toString(),
                                        Objects.requireNonNull(mTextBoxes[6].getText()).toString(), UserSex,
                                        Objects.requireNonNull(mTextBoxes[3].getText()).toString(),
                                        Objects.requireNonNull(mTextBoxes[2].getText()).toString(), 0,
                                        Objects.requireNonNull(mTextBoxes[4].getText()).toString());
                                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, UserTypeActivity.class));
                                break;
                            default:
                                Toast.makeText(RegisterActivity.this, "Something went wrong, try again", Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            ShowLoadingPopup(myDialog, false);
            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uEmail", Objects.requireNonNull(mTextBoxes[3].getText()).toString());
                params.put("uNumber", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                return params;
            }
        };

        requestQueue = Volley.newRequestQueue(this);
        stringRequest.setTag(TAG);
        requestQueue.add(stringRequest);
    }

    private boolean sexIsChecked() {
        if (UserSex.isEmpty()) {
            mCBMale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBFemale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBOther.setTextColor(Color.argb(255, 255, 23, 23));
            return false;
        }
        mCBMale.setTextColor(getResources().getColor(R.color.Grey));
        mCBFemale.setTextColor(getResources().getColor(R.color.Grey));
        mCBOther.setTextColor(getResources().getColor(R.color.Grey));
        return true;
    }

    private boolean passwordMatches() {
        if (!Objects.requireNonNull(mTextBoxes[4].getText()).toString().equals(Objects.requireNonNull(mTextBoxes[5].getText()).toString())) {
            mTextBoxes[4].setError("Password does not match");
            mTextBoxes[5].setError("Password does not match");
            return false;
        }
        return true;
    }

    public void register(View view) {
        if (allFieldsEntered(mTextBoxes) && sexIsChecked() && passwordMatches()) {
            POSTRegister();
        }
    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}