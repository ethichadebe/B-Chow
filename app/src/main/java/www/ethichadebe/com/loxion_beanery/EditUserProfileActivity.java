package www.ethichadebe.com.loxion_beanery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.util.Calendar;

import static util.HelperMethods.MakeBlack;

public class EditUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private MaterialEditText[] mTextBoxes = new MaterialEditText[5];
    private LinearLayout llBack;
    private TextView tvSave;
    private CheckBox mCBMale, mCBFemale, mCBOther;
    private TextView mViewError;
    private static String UserSex, UserName, UserSurname, UserNumber, UserPassword, UserDOB, UserEmail = "";
    private Dialog myDialog;

    /*0 Name
1 Surname
2 Number
3 Email
4 DOB*/
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
        setContentView(R.layout.activity_edit_user_profile);

        llBack = findViewById(R.id.llBack);
        tvSave = findViewById(R.id.tvSave);
        myDialog = new Dialog(this);

        //Labels
        mViewError = findViewById(R.id.lblError);

        //Textboxes
        mTextBoxes[0] = findViewById(R.id.txtName);
        mTextBoxes[1] = findViewById(R.id.txtSurname);
        mTextBoxes[2] = findViewById(R.id.txtPhone);
        mTextBoxes[3] = findViewById(R.id.txtEmail);
        mTextBoxes[4] = findViewById(R.id.txtDOB);

        //CheckBoxes
        mCBMale = findViewById(R.id.cbMale);
        mCBFemale = findViewById(R.id.cbFemale);
        mCBOther = findViewById(R.id.cbOther);

        //Date p[icker
        mTextBoxes[4].setOnClickListener(new View.OnClickListener() {
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

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowConfirmationPopup();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
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
                    } else if (mTextBoxes[2].getText().toString().length() != 10) {
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
                        (mCBMale.isChecked() || mCBFemale.isChecked() ||
                                mCBOther.isChecked())) {
                    mViewError.setText("");
                    MakeBlack(mTextBoxes, 0, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 1, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 2, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 3, getResources().getColor(R.color.Black));
                    MakeBlack(mTextBoxes, 4, getResources().getColor(R.color.Black));

                    UserName = mTextBoxes[0].getText().toString();
                    UserSurname = mTextBoxes[1].getText().toString();
                    UserNumber = mTextBoxes[2].getText().toString();
                    UserEmail = mTextBoxes[3].getText().toString();
                    UserDOB = mTextBoxes[4].getText().toString();

                    startActivity(new Intent(EditUserProfileActivity.this, MainActivity.class));
                    //Manipulate loading and button disabling
                    //startActivity(new Intent(RegistrationActivity.this, AcountAuthActivity.class));
                }
            }
        });
    }

    public void ShowConfirmationPopup() {
        TextView tvCancel, tvMessage;
        CardView cvYes, cvNo;
        myDialog.setContentView(R.layout.popup_confirmation);

        tvCancel = myDialog.findViewById(R.id.tvCancel);
        tvMessage = myDialog.findViewById(R.id.tvMessage);
        cvYes = myDialog.findViewById(R.id.cvYes);
        cvNo = myDialog.findViewById(R.id.cvNo);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });

        tvMessage.setText("All entered information will be lost\nAre you sure?");

        cvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                finish();
            }
        });

        cvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);

        String strCurrentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
        mTextBoxes[4].setText(strCurrentDate);

    }


}
