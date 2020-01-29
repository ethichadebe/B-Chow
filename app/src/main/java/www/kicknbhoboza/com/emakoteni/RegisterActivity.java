package www.kicknbhoboza.com.emakoteni;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
public class RegisterActivity extends AppCompatActivity {
    RelativeLayout rellay1;
    private Button mButtonLogin,mButtonRegister;
    private ImageView mImageLogo;
    private EditText[] mTextBoxes = new EditText[7];
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
    2 Address
    3 Number
    4 Email
    5 Password
    6 CPassword
    7 DOB*/
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
    Calendar calendar;
    DatePickerDialog datePickerDialog;
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
       /* mTextBoxes[6].setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            datePickerDialog = new DatePickerDialog(RegisterActivity.this, R.style.DialogTheme, (datePicker, mYear, mMonth, mDay) -> mTextBoxes[7].setText(mYear + "-" + (mMonth + 1) + "-" + mDay), year, month, day);
            datePickerDialog.show();
        });
*/
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
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

                        MakeBlack(mTextBoxes, i, getResources().getDrawable(R.drawable.et_bg));
                        mViewError.setText(R.string.enter_all_required_details);
                        mTextBoxes[i].setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                    } else if (!mTextBoxes[4].getText().toString().equals(mTextBoxes[5].getText().toString())) {
                        //Check if password matches
                        mViewError.setText("");
                        MakeBlack(mTextBoxes, i, getResources().getDrawable(R.drawable.et_bg));
                        mTextBoxes[i].setBackground(getResources().getDrawable(R.drawable.et_bg_err));
                        mViewError.setText(R.string.password_does_not_match);
                    } /*else if(!Member.UserExists(requestQueue, mTextBoxes[4].getText().toString())){
                    //Check if username already exists
                    mLines[4].setBackgroundColor(Color.argb(255, 255, 23, 23));
                    mViewError.setText("User already exists");
                }*/ else if (mTextBoxes[2].getText().toString().length() != 10) {
                        //Check if username already exists
                        mTextBoxes[2].setBackground(getResources().getDrawable(R.drawable.et_bg_err));
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
                    MakeBlack(mTextBoxes, 0, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 1, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 2, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 3, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 4, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 5, getResources().getDrawable(R.drawable.et_bg));
                    MakeBlack(mTextBoxes, 6, getResources().getDrawable(R.drawable.et_bg));

                    UserName = mTextBoxes[0].getText().toString();
                    UserSurname = mTextBoxes[1].getText().toString();
                    UserNumber = mTextBoxes[2].getText().toString();
                    UserEmail = mTextBoxes[3].getText().toString();
                    UserPassword = mTextBoxes[4].getText().toString();
                    UserDOB = mTextBoxes[6].getText().toString();

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    //Manipulate loading and button disabling
                    //startActivity(new Intent(RegistrationActivity.this, AcountAuthActivity.class));
                }
            }
        });
    }

    /**
     * @param txtBoxes Array of Textboxes
     * @param index    index of Textbox to make underline blue
     * @param clr      Colour
     */
    public static void MakeBlack(EditText[] txtBoxes, int index, Drawable clr) {
        for (int i = 0; i < txtBoxes.length; i++) {
            if (i != index) {
                if (!txtBoxes[i].getText().toString().isEmpty()) {
                    txtBoxes[i].setBackground(clr);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}