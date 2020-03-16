package www.ethichadebe.com.loxion_beanery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static util.Constants.getIpAddress;
import static util.HelperMethods.MakeBlack;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    RelativeLayout rellay1;
    private CardView mButtonRegister, mButtonLogin;
    private CardView mImageLogo;
    private MaterialEditText[] mTextBoxes = new MaterialEditText[7];
    private TextView mViewError;
    private CheckBox mCBMale, mCBFemale, mCBOther;

    private static String UserSex, UserName, UserSurname, UserNumber, UserPassword, UserDOB, UserEmail = "";
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };


    /*0 Name
    1 Surname
    2 Number
    3 Email
    4 Password
    5 CPassword
    6 DOB*/
    public static String getUserSex() {
        return UserSex;
    }

    public static String getUserName() {
        return UserName;
    }

    public static String getUserSurname() {
        return UserSurname;
    }

    public static String getUserNumber() {
        return UserNumber;
    }

    public static String getUserPassword() {
        return UserPassword;
    }

    public static String getUserDOB() {
        return UserDOB;
    }

    public static String getUserEmail() {
        return UserEmail;
    }

    //Set up date picker
    //.........................................................................................................
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    //.................................................................................................

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rellay1 = findViewById(R.id.rellay1);

        handler.postDelayed(runnable, 500);

        //Image
        mImageLogo = findViewById(R.id.ivLogo);

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtPhone);
        mTextBoxes[3] = findViewById(R.id.txtEmail);
        mTextBoxes[4] = findViewById(R.id.txtPass);
        mTextBoxes[5] = findViewById(R.id.txtCPass);
        mTextBoxes[6] = findViewById(R.id.txtDOB);

        mButtonLogin = findViewById(R.id.btnLogin);
        //Button
        mButtonRegister = findViewById(R.id.btnRegister);

        //Labels
        mViewError = findViewById(R.id.lblError);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);


        //Date p[icker
        mTextBoxes[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePickeer = new DatePickerFragment();
                datePickeer.show(getSupportFragmentManager(), "date picker");
            }
        });

        //Handling Checkbox click events
        mCBMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCBMale.isChecked()) {
                    mCBFemale.setChecked(false);
                    mCBOther.setChecked(false);
                    UserSex = "male";
                }
            }
        });
        mCBOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCBOther.isChecked()) {
                    mCBMale.setChecked(false);
                    mCBFemale.setChecked(false);
                    UserSex = "other";
                }
            }
        });
        mCBFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCBFemale.isChecked()) {
                    mCBMale.setChecked(false);
                    mCBOther.setChecked(false);
                    UserSex = "female";
                }
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mTextBoxes.length; i++) {
                    if (mTextBoxes[i].getText().toString().isEmpty() || (!mCBMale.isChecked() && !mCBFemale.isChecked() && !mCBOther.isChecked())) {
                        //Check if all textboxes are filled in
                        mViewError.setText("");

                        if (mCBMale.isChecked() || mCBFemale.isChecked() || mCBOther.isChecked()) {
                            mCBMale.setTextColor(getResources().getColor(R.color.Black));
                            mCBFemale.setTextColor(getResources().getColor(R.color.Black));
                            mCBOther.setTextColor(getResources().getColor(R.color.Black));

                        } else {
                            mCBMale.setTextColor(Color.argb(255, 255, 23, 23));
                            mCBFemale.setTextColor(Color.argb(255, 255, 23, 23));
                            mCBOther.setTextColor(Color.argb(255, 255, 23, 23));
                        }

                        MakeBlack(mTextBoxes, i, getResources().getColor(R.color.Black));
                        mViewError.setText(R.string.enter_all_required_details);
                        mTextBoxes[i].setUnderlineColor(getResources().getColor(R.color.Red));
                    } else if (!mTextBoxes[4].getText().toString().equals(mTextBoxes[5].getText().toString())) {
                        //Check if password matches
                        mViewError.setText("");
                        MakeBlack(mTextBoxes, i, getResources().getColor(R.color.Black));
                        mTextBoxes[i].setUnderlineColor(getResources().getColor(R.color.Red));
                        mViewError.setText(R.string.password_does_not_match);
                    } /*else if(!Member.UserExists(requestQueue, mTextBoxes[4].getText().toString())){
                    //Check if username already exists
                    mLines[4].setBackgroundColor(Color.argb(255, 255, 23, 23));
                    mViewError.setText("User already exists");
                }*/ else if (mTextBoxes[2].getText().toString().length() != 10) {
                        //Check if username already exists
                        mTextBoxes[2].setUnderlineColor(getResources().getColor(R.color.Red));
                        mViewError.setText("Phone number should be a 10 digit value");
                    }
                }

                if ((!mTextBoxes[0].getText().toString().isEmpty()) &&
                        (!mTextBoxes[1].getText().toString().isEmpty()) &&
                        (!mTextBoxes[2].getText().toString().isEmpty()) &&
                        (!mTextBoxes[3].getText().toString().isEmpty()) &&
                        (!mTextBoxes[4].getText().toString().isEmpty()) &&
                        (!mTextBoxes[5].getText().toString().isEmpty()) &&
                        (!mTextBoxes[6].getText().toString().isEmpty()) &&
                        (mCBMale.isChecked() || mCBFemale.isChecked() ||
                                mCBOther.isChecked())) {
                    mViewError.setText("");
                    MakeBlack(mTextBoxes, 0, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 1, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 2, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 3, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 4, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 5, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 6, getResources().getColor(R.color.Black));

                    UserName = mTextBoxes[0].getText().toString();
                    UserSurname = mTextBoxes[1].getText().toString();
                    UserNumber = mTextBoxes[2].getText().toString();
                    UserEmail = mTextBoxes[3].getText().toString();
                    UserPassword = mTextBoxes[4].getText().toString();
                    UserDOB = mTextBoxes[6].getText().toString();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    //Manipulate loading and button disabling
                    //startActivity(new Intent(RegistrationActivity.this, AcountAuthActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        String strCurrentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        mTextBoxes[6].setText(strCurrentDate);

    }

    private void POSTUser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://" + getIpAddress() + "/users",
                response -> {
                    Log.d("debug", response);
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uName", getUserName());
                params.put("uSurname", getUserSurname());
                params.put("uDOB", getUserPassword());
                params.put("uSex", getUserEmail());
                params.put("uEmail", getUserSex());
                params.put("uNumber", getUserNumber());
                params.put("uPassword", getUserDOB());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}