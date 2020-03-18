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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.HelperMethods;

import static util.Constants.getIpAddress;
import static util.HelperMethods.MakeBlack;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RelativeLayout rellay1;
    private Dialog myDialog;

    /*0 Name
    1 Surname
    2 Number
    3 Email
    4 Password
    5 CPassword
    6 DOB*/
    private MaterialEditText[] mTextBoxes = new MaterialEditText[7];
    private TextView mViewError;
    private CheckBox mCBMale, mCBFemale, mCBOther;

    private static String UserSex = "";
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

        CardView mButtonLogin = findViewById(R.id.btnLogin);
        //Button
        CardView mButtonRegister = findViewById(R.id.btnRegister);

        //Labels
        mViewError = findViewById(R.id.lblError);

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
            if (mCBMale.isChecked()) {
                mCBFemale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "male";
            }
        });
        mCBOther.setOnClickListener(view -> {
            if (mCBOther.isChecked()) {
                mCBMale.setChecked(false);
                mCBFemale.setChecked(false);
                UserSex = "other";
            }
        });
        mCBFemale.setOnClickListener(view -> {
            if (mCBFemale.isChecked()) {
                mCBMale.setChecked(false);
                mCBOther.setChecked(false);
                UserSex = "female";
            }
        });

        mButtonLogin.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        mButtonRegister.setOnClickListener(view -> {

            if (!allFieldsEntered() && !sexIsChecked()) {
                mViewError.setText(R.string.enter_all_required_details);
            } else if (!allFieldsEntered()) {
                mViewError.setText(R.string.enter_all_required_details);
            } else if (!sexIsChecked()) {
                mViewError.setText("Select gender");
            } else if (!passwordMatches()) {
                mViewError.setText("Password doesn't match");
            } else if (allFieldsEntered() && sexIsChecked() && passwordMatches()) {
                POSTRegister();
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
        HelperMethods.ShowLoadingPopup(myDialog, true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/users/Register",
                response -> {
                    HelperMethods.ShowLoadingPopup(myDialog, false);
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }, error -> {
            HelperMethods.ShowLoadingPopup(myDialog, false);
            Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uName", Objects.requireNonNull(mTextBoxes[0].getText()).toString());
                params.put("uSurname", Objects.requireNonNull(mTextBoxes[1].getText()).toString());
                params.put("uDOB", Objects.requireNonNull(mTextBoxes[6].getText()).toString());
                params.put("uSex", UserSex);
                params.put("uEmail", Objects.requireNonNull(mTextBoxes[3].getText()).toString());
                params.put("uNumber", Objects.requireNonNull(mTextBoxes[2].getText()).toString());
                params.put("uPassword", Objects.requireNonNull(mTextBoxes[4].getText()).toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean allFieldsEntered() {
        boolean allEntered = true;
        for (int i = 0; i < mTextBoxes.length; i++) {
            if (Objects.requireNonNull(mTextBoxes[i].getText()).toString().isEmpty()) {
                MakeBlack(mTextBoxes, i, getResources().getColor(R.color.Black));
                mTextBoxes[i].setUnderlineColor(getResources().getColor(R.color.Red));
                allEntered = false;
            }
        }
        for (int i = 0; i < mTextBoxes.length; i++) {
            MakeBlack(mTextBoxes, i, getResources().getColor(R.color.Black));
        }
        return allEntered;
    }

    private boolean sexIsChecked() {
        if (!mCBMale.isChecked() || !mCBFemale.isChecked() || !mCBOther.isChecked()) {
            mCBMale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBFemale.setTextColor(Color.argb(255, 255, 23, 23));
            mCBOther.setTextColor(Color.argb(255, 255, 23, 23));
            return false;
        }
        mCBMale.setTextColor(getResources().getColor(R.color.Black));
        mCBFemale.setTextColor(getResources().getColor(R.color.Black));
        mCBOther.setTextColor(getResources().getColor(R.color.Black));

        return true;
    }

    private boolean passwordMatches() {
        if (Objects.requireNonNull(mTextBoxes[4].getText()).toString().equals(Objects.requireNonNull(mTextBoxes[5].getText()).toString())) {
            mTextBoxes[4].setUnderlineColor(getResources().getColor(R.color.Red));
            mTextBoxes[5].setUnderlineColor(getResources().getColor(R.color.Red));
            return false;
        }
        MakeBlack(mTextBoxes, 4, getResources().getColor(R.color.Black));
        MakeBlack(mTextBoxes, 5, getResources().getColor(R.color.Black));
        return true;
    }


}